package com.company.storehouse.service.impl;

import com.company.storehouse.model.User;
import com.company.storehouse.model.enumeration.RoleName;
import com.company.storehouse.repository.RoleRepository;
import com.company.storehouse.repository.UserRepository;
import com.company.storehouse.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Override
    @Transactional(readOnly = true)
    public User current() {
        return userRepository.getCurrentUser();
    }

    @Override
    @Transactional(readOnly = true)
    public RoleName getCurrentUserRole(Principal principal) {
        return roleRepository.findByUserUsername(principal.getName())
                .getName();
    }

    @Override
    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        if (username == null) {
            return null;
        }
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            log.error("IN findByUsername — no user found by username: {}", username);
        }
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(Long id) {
        if (id == null) {
            return null;
        }
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            log.error("IN findById — no user found by id: {}", id);
        }
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

}
