package com.springboot3security.util;

import com.springboot3security.Springboot3SecurityApplication;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest(classes = Springboot3SecurityApplication.class)
class JwtUtilTest {


    @Autowired
    private JwtUtil jwtUtil;

    @Mock
    private UserDetails userDetails;

    @Test
    void testGenerateToken() {
        String token = jwtUtil.generateToken("testUser");
        assertNotNull(token);
    }

    @Test
    void testValidateToken() {
        String token = jwtUtil.generateToken("testUser");
        when(userDetails.getUsername()).thenReturn("testUser");
        assertTrue(jwtUtil.validateToken(token, userDetails));
    }

    @Test
    void testExtractUsername() {
        String token = jwtUtil.generateToken("testUser");
        String username = jwtUtil.extractUsername(token);
        assertEquals("testUser", username);
    }
}
