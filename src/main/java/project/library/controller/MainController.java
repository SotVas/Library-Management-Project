package project.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import project.library.entity.*;
import project.library.service.*;

import java.util.List;

@Controller
public class MainController {

    private final NotificationService notificationService;

    private final UserService userService;
    private final RoleService roleService;
    private final BookService bookService;

    private final OrderService orderService;

    @Autowired
    public MainController(UserService userService, RoleService roleService, BookService bookService, OrderService orderService,NotificationService notificationService) {
        this.userService = userService;
        this.roleService = roleService;
        this.bookService = bookService;
        this.orderService = orderService;
        this.notificationService = notificationService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/index")
    public String index(Model model) {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String username = loggedInUser.getName();
        User loggedInUserEntity = userService.findByUsername(username);
        List<User> users = userService.findAll();
        List<Role> roles = roleService.findAll();
        List<Book> books = bookService.findAll();

        List<Order> orders = orderService.findAll();

        model.addAttribute("loggedInUserEntity", loggedInUserEntity);
        model.addAttribute("users", users);
        model.addAttribute("roles", roles);
        model.addAttribute("books", books);
        model.addAttribute("orders", orders);

        return "index";
    }

    @GetMapping("/error")
    public String error() {
        return "error";
    }

    @GetMapping("/accessDenied")
    public String accessDenied(){return "accessDenied";}

    @GetMapping("/")
    public String any() {
        return "redirect:/index";
    }
}

