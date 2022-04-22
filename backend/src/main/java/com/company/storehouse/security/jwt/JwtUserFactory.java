package com.company.storehouse.security.jwt;

import com.company.storehouse.model.Role;
import com.company.storehouse.model.User;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This class generates a User in terms of Spring Security.
 */
@NoArgsConstructor
public final class JwtUserFactory {

    public static JwtUser create(User user) {
        return new JwtUser(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                mapToGrantedAuthorities(user.getRole())
        );
    }

    /**
     * Conversion of User's Role to Granted Authorities
     *
     * @param role User's Role
     * @return mapped result
     */
    private static List<GrantedAuthority> mapToGrantedAuthorities(Role role) {
        return role.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName().name()))
                .collect(Collectors.toList());
    }
}
