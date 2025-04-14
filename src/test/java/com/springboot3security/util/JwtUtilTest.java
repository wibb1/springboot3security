package com.springboot3security.util;

import com.springboot3security.Springboot3SecurityApplication;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest(classes = Springboot3SecurityApplication.class)
class JwtUtilTest {


    @Autowired
    private JwtUtil jwtUtil;

    @Mock
    private UserDetails testUser;

    void setUpTestUser() {
        testUser = Mockito.mock(UserDetails.class);

        Collection<GrantedAuthority> authorities = Arrays.asList(
                new SimpleGrantedAuthority("ROLE_TEST")
        );

        Mockito.when(testUser.getUsername()).thenReturn("testUser");
        Mockito.when(testUser.getPassword()).thenReturn("password");
        Mockito.doReturn(authorities).when(testUser).getAuthorities();
    }

    @Test
    void testGenerateToken() {
        setUpTestUser();
        when(testUser.getUsername()).thenReturn("testUser");
        when(testUser.getPassword()).thenReturn("password");
        String token = jwtUtil.generateToken(testUser);
        assertNotNull(token);
    }

    @Test
    void testValidateToken() {
        setUpTestUser();
        String token = jwtUtil.generateToken(testUser);
        when(testUser.getUsername()).thenReturn("testUser");
        assertTrue(jwtUtil.validateToken(token, testUser));
    }

    @Test
    void testExtractUsername() {
        setUpTestUser();
        String token = jwtUtil.generateToken(testUser);
        when(testUser.getUsername()).thenReturn("testUser");
        String username = jwtUtil.extractUsername(token);
        assertEquals("testUser", username);
    }
}
