package com.springboot3security.config;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class SecurityConfigTest {

    @Autowired
    private SecurityConfig securityConfig;

    @Test
    void testSecurityFilterChain() throws Exception {
        HttpSecurity http = Mockito.mock(HttpSecurity.class);
        SecurityFilterChain securityFilterChain = securityConfig.securityFilterChain(http);
        assertNotNull(securityFilterChain, "SecurityFilterChain should not be null");
    }

    @Test
    void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        assertNotNull(passwordEncoder, "PasswordEncoder should not be null");
    }

    @Test
    void testAuthenticationProvider() {
        AuthenticationProvider authenticationProvider = securityConfig.authenticationProvider();
        assertNotNull(authenticationProvider, "AuthenticationProvider should not be null");
    }

    @Test
    void testAuthenticationManager() throws Exception {
        AuthenticationConfiguration configuration = Mockito.mock(AuthenticationConfiguration.class);
        AuthenticationManager authenticationManager = securityConfig.authenticationManager(configuration);
        assertNotNull(authenticationManager, "AuthenticationManager should not be null");
    }
}
