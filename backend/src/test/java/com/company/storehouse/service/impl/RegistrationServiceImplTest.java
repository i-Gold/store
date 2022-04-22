package com.company.storehouse.service.impl;

import com.company.storehouse.model.Authority;
import com.company.storehouse.model.Role;
import com.company.storehouse.model.User;
import com.company.storehouse.model.enumeration.AuthorityName;
import com.company.storehouse.model.enumeration.RoleName;
import com.company.storehouse.repository.UserRepository;
import com.company.storehouse.service.RegistrationService;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Collections;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class RegistrationServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private JavaMailSender javaMailSender;
    @InjectMocks
    private RegistrationService registrationService = new RegistrationServiceImpl();

    @BeforeEach
    public void setup() {
        User testUser = getTestUser();
        when(userRepository.save(testUser)).thenReturn(testUser);
        doNothing().when(javaMailSender).send(getMessage(testUser));
    }

    @Test
    public void shouldSaveNewUser() {
        User actual = getTestUser();
        Assertions.assertThat(actual.isEnabled()).isFalse();
        registrationService.createUser(actual);
        verify(userRepository).save(actual);
        Assertions.assertThat(actual.isEnabled()).isTrue();
        verify(javaMailSender, times(1)).send(getMessage(actual));
    }

    @Test
    public void shouldFailSaveNewUser() {
        ThrowableAssert.ThrowingCallable throwingCallable = () -> registrationService.createUser(null);
        Assertions.assertThatExceptionOfType(NullPointerException.class).isThrownBy(throwingCallable);
    }

    private User getTestUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("doe123");
        user.setEmail("john.doe@gmail.com");
        user.setRole(getTestRole());
        return user;
    }

    private Role getTestRole() {
        Authority authority = new Authority();
        authority.setName(AuthorityName.APPROVE_ON_IMPORT_EXPORT);
        Role role = new Role(RoleName.MANAGER);
        role.setAuthorities(Collections.singleton(authority));
        return role;
    }

    private SimpleMailMessage getMessage(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Registration at Grocery Storehouse");
        message.setText("Congratulations, " + user.getUsername() + "!\n\nYou are successfully registered");
        return message;
    }

}
