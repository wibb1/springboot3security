package com.springboot3security.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/preauthorize")
public class PreauthorizeController {
    @GetMapping("/adminOnly")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminOnlyEndpoint() {
        return "This endpoint is accessible only to ADMIN role.";
    }

    @GetMapping("/userOnly")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String userOnlyEndpoint() {
        return "This endpoint is accessible only to USER or ADMIN role.";
    }
}
