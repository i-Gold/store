package com.company.storehouse.service.impl;

import com.company.storehouse.model.Authority;
import com.company.storehouse.model.Role;
import com.company.storehouse.model.User;
import com.company.storehouse.model.enumeration.AuthorityName;
import com.company.storehouse.model.enumeration.RoleName;
import com.company.storehouse.repository.UserRepository;
import com.company.storehouse.service.UserService;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceImplTest {

    private static final Long TEST_ID = 1L;
    private static final String TEST_USERNAME = "doe123";

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService = new UserServiceImpl();

    @BeforeEach
    public void setup() {
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(getTestUser());
        when(userRepository.findById(TEST_ID)).thenReturn(getTestUser());
        when(userRepository.findAll()).thenReturn(getListOfUsers());
        when(userRepository.getCurrentUser()).thenReturn(getTestUser().orElse(null));
    }

    @Test
    public void shouldFindUserByUsername() {
        User actual = userService.findByUsername(TEST_USERNAME);
        verify(userRepository).findByUsername(TEST_USERNAME);
        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(TEST_ID).isEqualTo(actual.getId());
    }
    @Test
    public void shouldFailFindUserByUsername() {
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(null);
        ThrowableAssert.ThrowingCallable throwingCallable = () -> userService.findByUsername(TEST_USERNAME);
        Assertions.assertThatExceptionOfType(NullPointerException.class).isThrownBy(throwingCallable);
    }

    @Test
    public void shouldFindUserById() {
        User actual = userService.findById(TEST_ID);
        verify(userRepository).findById(TEST_ID);
        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(TEST_USERNAME).isEqualTo(actual.getUsername());
    }

    @Test
    public void shouldFailFindUserById() {
        when(userRepository.findById(TEST_ID)).thenReturn(null);
        ThrowableAssert.ThrowingCallable throwingCallable = () -> userService.findById(TEST_ID);
        Assertions.assertThatExceptionOfType(NullPointerException.class).isThrownBy(throwingCallable);
    }

    @Test
    public void shouldGetAllUsers() {
        List<User> actual = userService.getAll();
        verify(userRepository).findAll();
        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(3).isEqualTo(actual.size());
    }

    @Test
    public void shouldGetCurrentUser() {
        User actual = userService.current();
        verify(userRepository).getCurrentUser();
        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(TEST_ID).isEqualTo(actual.getId());
    }

    private Optional<User> getTestUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername(TEST_USERNAME);
        user.setEmail("john.doe@gmail.com");
        user.setEnabled(true);
        user.setRole(getTestRole());
        return Optional.of(user);
    }

    private Role getTestRole() {
        Authority authority = new Authority();
        authority.setName(AuthorityName.APPROVE_ON_IMPORT_EXPORT);
        Role role = new Role(RoleName.MANAGER);
        role.setAuthorities(Collections.singleton(authority));
        return role;
    }

    private List<User> getListOfUsers() {
        List<User> result = new ArrayList<>();
        User first = getTestUser().orElse(null);

        User second = getTestUser().orElse(null);
        Objects.requireNonNull(second).setId(2L);
        second.setUsername("alex");
        second.setEmail("alex123@gmail.com");

        User third = getTestUser().orElse(null);
        third.setId(3L);
        third.setUsername("david");
        third.setEmail("david123@gmail.com");

        Collections.addAll(result, first, second, third);
        return result;
    }

}
