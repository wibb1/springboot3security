package com.springboot3security.controller;

import com.springboot3security.config.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import({TestSecurityConfig.class})
public class UserControllerPreauthorizedTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "ADMIN")
    void testAdminOnlyEndpoint_WithAdminRole() throws Exception {
        mockMvc.perform(get("/auth/adminOnly"))
                .andExpect(status().isOk())
                .andExpect(content().string("This endpoint is accessible only to ADMIN role."));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testAdminOnlyEndpoint_WithUserRole() throws Exception {
        mockMvc.perform(get("/auth/adminOnly"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testUserOnlyEndpoint_WithUserRole() throws Exception {
        mockMvc.perform(get("/auth/userOnly"))
                .andExpect(status().isOk())
                .andExpect(content().string("This endpoint is accessible only to USER role."));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUserOnlyEndpoint_WithAdminRole() throws Exception {
        mockMvc.perform(get("/auth/userOnly"))
                .andExpect(content().string("This endpoint is accessible only to USER role."));
    }
}