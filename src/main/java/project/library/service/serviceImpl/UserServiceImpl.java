package project.library.service.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import project.library.entity.Role;
import project.library.entity.User;
import project.library.repository.RoleRepository;
import project.library.repository.UserRepository;
import project.library.service.RoleService;
import project.library.service.UserService;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private RoleService roleService;

    public boolean isPasswordCorrect(User user, String password) {
        return passwordEncoder.matches(password, user.getPassword());
    }


    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        super();
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }


    @EventListener(ApplicationReadyEvent.class)
    public User createAdmin() {
        if (userRepository.findByUsername("admin") == null) {


            User admin = new User("admin", "admin", 1, passwordEncoder.encode("Admin!1234"), "admin",
                    roleRepository.findByName("SuperAdmin"));
            return userRepository.save(admin);
        }

        return null;
    }

    @EventListener(ApplicationReadyEvent.class)
    public User createUser() {
        if (userRepository.findByUsername("user1") == null) {


            User user = new User("user1", "user1", 1, passwordEncoder.encode("User1!1234"), "user1",
                    roleRepository.findByName("User"));
            return userRepository.save(user);
        }

        return null;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User authenticatedUser = userRepository.findByUsername(username);
        // this.authenticatedUser = authenticatedUser;
        if (authenticatedUser == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(authenticatedUser.getUsername(), authenticatedUser.getPassword(), getAuthorities(authenticatedUser.getRoleByUserrole()));
    }

    @Override
    @Transactional
    public void updateUserPassword(User user, String newPassword) {
        // Encode the new password
        String encodedPassword = passwordEncoder.encode(newPassword);

        // Set the encoded password to the user
        user.setPassword(encodedPassword);

        // Save the updated user to the database
        userRepository.save(user);
    }

    public Collection<? extends GrantedAuthority> getAuthorities(Role role) {

        return Arrays.asList(new SimpleGrantedAuthority(role.getName()));
    }


    @Override
    public User save(User user) {

        String lastname = user.getLastname().trim();
        String firstname = user.getFirstname().trim();
        String username = user.getUsername().trim();
        String code = passwordEncoder.encode(user.getPassword());
        Role userRole = roleService.findById(user.getRoleByUserrole().getId());
        if (userRepository.findByUsername(username) == null) {

           User newUser= new User(lastname,firstname,1,code,username,userRole);
           return userRepository.save(newUser);

        } else return null;


    }

    @Override
    public User findById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void edit(int id, User user, String newPassword) {
        User found = userRepository.findById(id);


        found.setFirstname(user.getFirstname());
        found.setLastname(user.getLastname());
        found.setRoleByUserrole(user.getRoleByUserrole());
        found.setEnable(user.getEnable());

        if (newPassword != "") {

            found.setPassword(passwordEncoder.encode(newPassword));
        }

        userRepository.save(found);
    }




    @Override
    public void changepass(int id, User user) {
        User found = userRepository.findById(id);
        found.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(found);
    }

    @Override
    public void disable(User user) {

        user.setEnable(0);
        userRepository.save(user);
    }

    @Override
    public void enable(User user) {
        user.setEnable(1);
        userRepository.save(user);
    }


    @Override
    public void deleteUser(User user) {
        userRepository.delete(user);
    }



}