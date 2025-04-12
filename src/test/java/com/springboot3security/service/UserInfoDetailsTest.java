package com.springboot3security.service;

import com.springboot3security.entity.UserInfo;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserInfoDetailsTest {

    @Test
    void testUserInfoDetails_ValidUserInfo() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername("testUser");
        userInfo.setPassword("password");
        userInfo.setRole("ROLE_TEST");

        UserInfoDetails userInfoDetails = new UserInfoDetails(userInfo);

        assertEquals("testUser", userInfoDetails.getUsername());
        assertEquals("password", userInfoDetails.getPassword());

        Collection<? extends GrantedAuthority> authorities = userInfoDetails.getAuthorities();
        assertEquals(1, authorities.size());
        assertTrue(authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_TEST")));

        assertTrue(userInfoDetails.isAccountNonExpired());
        assertTrue(userInfoDetails.isAccountNonLocked());
        assertTrue(userInfoDetails.isCredentialsNonExpired());
        assertTrue(userInfoDetails.isEnabled());
    }

    @Test
    void testUserInfoDetails_NullRole() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername("testUser");
        userInfo.setPassword("password");
        userInfo.setRole(null);

        UserInfoDetails userInfoDetails = new UserInfoDetails(userInfo);

        assertEquals("testUser", userInfoDetails.getUsername());
        assertEquals("password", userInfoDetails.getPassword());

        Collection<? extends GrantedAuthority> authorities = userInfoDetails.getAuthorities();
        assertTrue(authorities.isEmpty());
    }
}
