package project.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.library.entity.*;
import project.library.service.OrderService;
import project.library.service.RoleService;
import project.library.service.UserService;

import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static java.lang.Integer.parseInt;

@Controller
@RequestMapping("/users")
public class UserController {
    public final static int recordsByPage = 10;
    private final UserService userService;
    private final RoleService roleService;

    private final OrderService orderService;

    public UserController(UserService userService, RoleService roleService, OrderService orderService) {
        this.userService = userService;
        this.roleService = roleService;
        this.orderService= orderService;
    }
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private List<User> findAllUsers() {
        return userService.findAll();
    }

    private List<Role> getRole() {
        return roleService.findAll();
    }


    @GetMapping(value = { "/viewAllUsers" })
    public String viewAllUsers(Model model, @AuthenticationPrincipal UserDetails currentUser) throws IOException {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String username = loggedInUser.getName();
        User user = userService.findByUsername(username);
        String role = loggedInUser.getAuthorities().toString();

        if (role.equals("[SuperAdmin]")) {
            List<User> allUsers = getAllUsers();
            allUsers.sort(Comparator.comparing(User::getLastname, Comparator.nullsLast(Comparator.naturalOrder())));

            model.addAttribute("allusers", allUsers);
            model.addAttribute("user", user);
            model.addAttribute("records", allUsers.size());

            return "users";
        } else {
            return "redirect:/error";
        }
    }


    private List<User> getAllUsers() {
        return userService.findAll();
    }


    private List<Role> getUserRole() {
        return roleService.findAll();
    }



    @PostMapping("/deleteUser")
    public String deleteUser(@RequestParam("id") Integer userId,RedirectAttributes redirectAttributes){
        User userToDelete = userService.findById(userId);


        if (userToDelete != null) {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getName().equals(userToDelete.getUsername())) {

                SecurityContextHolder.clearContext();
            }
            orderService.deleteOrdersByUser(userToDelete);
            userService.deleteUser(userToDelete);
        }
        redirectAttributes.addFlashAttribute("successMessage", "User deleted successfully.");
        return "redirect:/users/viewAllUsers";
    }




    @GetMapping(value = "/new")
    public String createUser(Model model) {

        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String username = loggedInUser.getName();  // username χρηστη
        User user = userService.findByUsername(username);  // ευρεση του χρηστη
        String role = loggedInUser.getAuthorities().toString();  // ρολος χρηστη

        if (role.equals("[SuperAdmin]")) { // "SuperAdmin"


            List<Role> roles = roleService.findAll();
            model.addAttribute("userNewForm", new User());
            model.addAttribute("role", roles);
            model.addAttribute("user", user);
            return "newUser";

        } else {

            return "redirect:/error";

        }
    }

    @PostMapping(value = "/new")
    public String createUser(@ModelAttribute("userNewForm") User userNewForm,
                             @RequestParam(value = "newpassword") String newpassword,
                             @RequestParam(value = "firstname") String firstname,
                             @RequestParam(value = "lastname") String lastname,
                             @RequestParam(value = "username") String username,
                             BindingResult bindingResult, Model model,
                             RedirectAttributes redirectAttributes) {

        if (newpassword != null && !(newpassword.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).{5,}$"))) {
            redirectAttributes.addFlashAttribute("errorMessage", "Password must meet certain criteria (e.g., include at least one lowercase, one uppercase, one digit, and one special character).");
            return "redirect:/users/new";
        }

        if (username.trim().length() < 5) {
            redirectAttributes.addFlashAttribute("errorMessage", "Username must be at least 5 characters long.");
            return "redirect:/users/new";
        }

        // Check if the username already exists
        if (userService.findByUsername(username.trim()) == null) {
            try {
                // Set attributes for the user entity
                userNewForm.setPassword(newpassword);
                userNewForm.setEnable(1); // Assuming 1 means enabled, modify if necessary
                userNewForm.setFirstname(firstname);
                userNewForm.setLastname(lastname);
                userNewForm.setUsername(username);
                userNewForm.setRoleByUserrole(roleService.findByName("User"));

                userService.save(userNewForm);

                redirectAttributes.addFlashAttribute("successMessage", "User created successfully.");
                return "redirect:/users/viewAllUsers";
            } catch (Exception e) {
                e.printStackTrace(System.err);
                redirectAttributes.addFlashAttribute("errorMessage", "Error creating user. Please try again.");
                return "redirect:/error";
            }
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Username already exists.");
            return "redirect:/users/new";
        }
    }


    @GetMapping("/changePassword/{id}")
    public String showChangePasswordForm(@PathVariable("id") Integer userId, Model model) {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String username = loggedInUser.getName();
        User loggedInUserEntity = userService.findByUsername(username);
        User user = userService.findById(userId);
        if (loggedInUserEntity != null &&
                ((loggedInUserEntity.getRoleByUserrole().getName().equals("User")
                        && loggedInUser.getName().equals(user.getUsername()))
                        || loggedInUserEntity.getRoleByUserrole().getName().equals("SuperAdmin"))) {
            model.addAttribute("user", user);
            return "changePassword";
        } else {
      
            return "redirect:/accessDenied";
        }
    }



    @PostMapping("/changePassword/{id}")
    public String changePassword(@PathVariable("id") Integer userId,
                                 @RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 RedirectAttributes redirectAttributes) {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String username = loggedInUser.getName();
        User loggedInUserEntity = userService.findByUsername(username);

        String role = loggedInUser.getAuthorities().toString();
        User user = userService.findById(userId);

        // Check if the old password matches
        if (userService.isPasswordCorrect(user, oldPassword)) {
            // Check if the new password and confirmation match
            if (newPassword.equals(confirmPassword)) {
                // Update the user's password
                userService.updateUserPassword(user, newPassword);
                redirectAttributes.addFlashAttribute("successMessage", "Password changed successfully.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "New password and confirmation do not match.");
            }
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Old password is incorrect.");
        }
        if (user != null) {
            if (loggedInUserEntity.getRoleByUserrole().getName().equals("User")) {
                return "redirect:/users/show/" + userId;
            } else if (loggedInUserEntity.getRoleByUserrole().getName().equals("SuperAdmin")) {
                return "redirect:/users/edit/" + userId;
            }
        }
        return "redirect:/error";
    }






    // απενεργοποιηση χρηστη
    @PostMapping(value = "/disable")
    public String disableUser(@RequestParam(value = "disable", required = true) int disable,RedirectAttributes redirectAttributes) {

        User found =userService.findById(disable);
        userService.disable(found);
        redirectAttributes.addFlashAttribute("successMessage", "User disabled successfully.");
        return "redirect:/users/viewAllUsers";

    }




    // ενεργοποιηση χρηστη
    @PostMapping(value = "/enable")
    public String enableUser(@RequestParam(value = "enable", required = true) int enable,RedirectAttributes redirectAttributes) {
        userService.enable(userService.findById(enable));
        redirectAttributes.addFlashAttribute("successMessage", "User enabled successfully.");
        return "redirect:/users/viewAllUsers";
    }



    @GetMapping(value = "/show/{id}")
    public String showUserDetails(@PathVariable("id") int id, Model model) {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String username = loggedInUser.getName();
        User loggedInUserEntity = userService.findByUsername(username);
        String role = loggedInUser.getAuthorities().toString();

        // Check if the role is "[SuperAdmin]" and the user is trying to access any user's details
        if (role.equals("[SuperAdmin]")) {
            try {
                User userToShow = userService.findById(id);

                if (userToShow != null) {
                    model.addAttribute("userToShow", userToShow);
                    model.addAttribute("resetPasswordUserId", userToShow.getId());
                    model.addAttribute("loggedInUser", loggedInUserEntity);
                    return "userDetails";
                } else {
                    model.addAttribute("errorMessage", "User not found");
                    return "error";
                }
            } catch (Exception e) {
                e.printStackTrace();
                model.addAttribute("errorMessage", "An error occurred while fetching user details");
                return "error";
            }
        }

        // Check if the role is "[User]" and the user is trying to access their own details
        if (role.equals("[User]") && loggedInUserEntity != null && loggedInUserEntity.getId() == id) {
            try {
                User userToShow = userService.findById(id);

                if (userToShow != null) {
                    model.addAttribute("userToShow", userToShow);
                    model.addAttribute("resetPasswordUserId", userToShow.getId());
                    model.addAttribute("loggedInUser", loggedInUserEntity);
                    return "userDetails";
                } else {
                    model.addAttribute("errorMessage", "User not found");
                    return "error";
                }
            } catch (Exception e) {
                e.printStackTrace();
                model.addAttribute("errorMessage", "An error occurred while fetching user details");
                return "error";
            }
        }

        // Redirect to an error page for other cases
        return "redirect:/accessDenied";
    }


    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable("id") Integer id, Model model) {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String username = loggedInUser.getName();
        User currentUser = userService.findByUsername(username);

        // Check if the logged-in user is allowed to edit
        if (currentUser != null && currentUser.getRoleByUserrole().getName().equals("SuperAdmin")) {
            User userToEdit = userService.findById(id);

            if (userToEdit != null) {
                List<Role> roles = roleService.findAll();
                model.addAttribute("userToEdit", userToEdit);
                model.addAttribute("roles", roles);
                return "editUser";
            }
        }


        return "redirect:/accessDenied";
    }



    @PostMapping("/edit/{id}")
    public String editUser(@PathVariable("id") Integer id,
                           @ModelAttribute("userToEdit") User editableUser,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {
        try {
            Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
            String username = loggedInUser.getName();
            User currentUser = userService.findByUsername(username);

            // Check if the logged-in user is allowed to edit
            if (currentUser != null && currentUser.getRoleByUserrole().getName().equals("SuperAdmin")) {
                User userToEdit = userService.findById(id);
                userToEdit.setFirstname(editableUser.getFirstname());
                userToEdit.setLastname(editableUser.getLastname());
                userToEdit.setUsername(editableUser.getUsername());
                userToEdit.setRoleByUserrole(editableUser.getRoleByUserrole());
                userService.save(userToEdit);
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "You do not have permission to edit this user.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating user.");
        }
        redirectAttributes.addFlashAttribute("successMessage","User details Updated");
        return "redirect:/users/viewAllUsers";
    }

}


