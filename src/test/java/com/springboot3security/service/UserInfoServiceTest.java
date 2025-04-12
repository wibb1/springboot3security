package com.springboot3security.service;

import com.springboot3security.entity.UserInfo;
import com.springboot3security.repository.UserInfoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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

    private UserInfoService userInfoService;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        userInfoService = new UserInfoService(repository, encoder);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void testLoadUserByUsername_UserExists() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername("testUser");
        userInfo.setPassword("password");
        userInfo.setRole("ROLE_TEST");
        when(repository.findByUsername("testUser")).thenReturn(Optional.of(userInfo));

        UserInfoDetails userDetails = (UserInfoDetails) userInfoService.loadUserByUsername("testUser");

        assertNotNull(userDetails);
        assertEquals("testUser", userDetails.getUsername());
        verify(repository, times(1)).findByUsername("testUser");
    }

    @Test
    void testLoadUserByUsername_UserDoesNotExist() {
        when(repository.findByUsername("nonExistentUser")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userInfoService.loadUserByUsername("nonExistentUser"));
        verify(repository, times(1)).findByUsername("nonExistentUser");
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

        assertEquals("Password does not meet security requirements", result);
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
