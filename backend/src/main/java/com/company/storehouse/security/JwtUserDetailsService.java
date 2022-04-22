package com.company.storehouse.security;

import com.company.storehouse.model.User;
import com.company.storehouse.repository.UserRepository;
import com.company.storehouse.security.jwt.JwtUserFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public JwtUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * The method generates the User that implements UserDetails based on the User found by username
     *
     * @param username input username
     * @return User in terms of Spring Security.
     * @throws UsernameNotFoundException would be thrown exception if credentials were incorrect
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username or password."));
        return JwtUserFactory.create(user);
    }
}
