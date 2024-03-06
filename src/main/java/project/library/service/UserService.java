package project.library.service;

import project.library.entity.User;

import java.util.List;


import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
public interface UserService extends UserDetailsService {

    User save(User user);


    User findById(int id);

    List<User> findAll();

    User findByUsername(String username);

     void updateUserPassword(User user ,String newPassword);

    boolean isPasswordCorrect(User user, String password);

    void edit(int id, User user, String newPassword);

    void deleteUser(User user);

    void changepass(int id, User user);


    void disable(User user);

    void enable( User user);




}