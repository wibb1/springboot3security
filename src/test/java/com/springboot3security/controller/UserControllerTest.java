package com.springboot3security.controller;

import com.springboot3security.entity.AuthRequest;
import com.springboot3security.entity.UserInfo;
import com.springboot3security.service.UserInfoService;
import com.springboot3security.util.JwtUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest {

    @Mock
    private UserInfoService userInfoService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private UserController userController;

    AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void testWelcome() {
        String response = userController.welcome();
        assertEquals("Welcome this endpoint is not secure", response);
    }

    @Test
    void testAddNewUser() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername("testUser");
        userInfo.setPassword("Test@123");

        when(userInfoService.addUser(userInfo)).thenReturn("User added successfully");

        String response = userController.addNewUser(userInfo);
        assertEquals("User added successfully", response);
        verify(userInfoService, times(1)).addUser(userInfo);
    }

    @Test
    void testAuthenticateAndGetToken_Success() {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername("testUser");
        authRequest.setPassword("Test@123");

        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(userInfoService.authenticateAndGetToken("testUser")).thenReturn("mockToken");

        String token = userController.authenticateAndGetToken(authRequest);
        assertEquals("mockToken", token);
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userInfoService, times(1)).authenticateAndGetToken("testUser");
    }

    @Test
    void testAuthenticateAndGetToken_Failure() {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername("testUser");
        authRequest.setPassword("WrongPassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new UsernameNotFoundException("Invalid user request!"));

        assertThrows(UsernameNotFoundException.class, () -> userController.authenticateAndGetToken(authRequest));
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void testUserPage() {
        String response = userController.userPage();
        assertEquals("This is the user page. Access granted!", response);
    }

    @Test
    void testAdminPage() {
        String response = userController.adminPage();
        assertEquals("This is the admin page. Access granted!", response);
    }
}
