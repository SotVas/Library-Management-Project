package project.library.service;

import project.library.entity.Role;

import java.util.List;

public interface RoleService {

    Role findById(int id);

    List<Role> findAll();

    Role findByName (String name);
}
