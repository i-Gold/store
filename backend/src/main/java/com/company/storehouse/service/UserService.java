package com.company.storehouse.service;

import com.company.storehouse.model.User;
import com.company.storehouse.model.enumeration.RoleName;

import java.security.Principal;
import java.util.List;

public interface UserService {

    User findById(Long id);

    User findByUsername(String username);

    List<User> getAll();

    User current();

    RoleName getCurrentUserRole(Principal principal);

}
