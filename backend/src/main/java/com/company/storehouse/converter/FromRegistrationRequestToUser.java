package com.company.storehouse.converter;

import com.company.storehouse.controller.request.RegistrationRequest;
import com.company.storehouse.model.Authority;
import com.company.storehouse.model.Role;
import com.company.storehouse.model.User;
import com.company.storehouse.model.enumeration.AuthorityName;
import com.company.storehouse.model.enumeration.RoleName;
import org.modelmapper.AbstractConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class FromRegistrationRequestToUser extends AbstractConverter<RegistrationRequest, User> {

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public FromRegistrationRequestToUser(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected User convert(RegistrationRequest request) {
        return User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(createRole(request))
                .build();
    }

    private Role createRole(RegistrationRequest request) {
        RoleName roleName = RoleName.valueOf(request.getRoleName());
        Role role = new Role(roleName);
        Authority authority = new Authority();
        if (roleName.equals(RoleName.MANAGER)) {
            authority.setName(AuthorityName.APPROVE_ON_IMPORT_EXPORT);
        } else {
            authority.setName(AuthorityName.IMPORT_EXPORT);
        }
        authority.setRole(role);
        role.getAuthorities().add(authority);
        return role;
    }

}
