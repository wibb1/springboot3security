package com.springboot3security.service;

import com.springboot3security.entity.UserInfo;
import com.springboot3security.repository.UserInfoRepository;
import com.springboot3security.util.JwtUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserInfoServiceTest {

    @Mock
    private UserInfoRepository repository;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserDetailsService userDetailsService;

    private UserInfoService userInfoService;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        userInfoService = new UserInfoService(repository, encoder, jwtUtil, userDetailsService);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void testAddUser_Success() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername("newUser");
        userInfo.setPassword("ValidPassword1");
        userInfo.setRole("ROLE_TEST");
        when(repository.findByUsername("newUser")).thenReturn(Optional.empty());
        when(encoder.encode("ValidPassword1")).thenReturn("encodedPassword");

        String result = userInfoService.addUser(userInfo);

        assertEquals("User Added Successfully", result);
        verify(repository, times(1)).save(userInfo);
        assertEquals("encodedPassword", userInfo.getPassword());
    }

    @Test
    void testAddUser_UserAlreadyExists() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername("existingUser");
        userInfo.setPassword("ValidPassword1");
        when(repository.findByUsername("existingUser")).thenReturn(Optional.of(userInfo));

        String result = userInfoService.addUser(userInfo);

        assertEquals("User already exists", result);
        verify(repository, never()).save(any());
    }

    @Test
    void testAddUser_InvalidPassword() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername("newUser");
        userInfo.setPassword("weak");
        userInfo.setRole("ROLE_TEST");

        String result = userInfoService.addUser(userInfo);

        assertEquals("Password must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, and one number.", result);
        verify(repository, never()).save(any());
    }

    @Test
    void testGetUserInfo_UserExists() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername("testUser");
        when(repository.findByUsername("testUser")).thenReturn(Optional.of(userInfo));

        String result = userInfoService.getUserInfo("testUser");

        assertTrue(result.contains("User Info:"));
        verify(repository, times(1)).findByUsername("testUser");
    }

    @Test
    void testGetUserInfo_UserDoesNotExist() {
        when(repository.findByUsername("nonExistentUser")).thenReturn(Optional.empty());

        String result = userInfoService.getUserInfo("nonExistentUser");

        assertEquals("User not found", result);
        verify(repository, times(1)).findByUsername("nonExistentUser");
    }
}
