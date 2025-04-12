package com.springboot3security.controller;

import com.springboot3security.entity.AuthRequest;
import com.springboot3security.entity.UserInfo;
import com.springboot3security.service.UserInfoService;
import com.springboot3security.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserInfoService service;
    private final JwtUtil jwtUtil;

    private final AuthenticationManager authenticationManager;

    public UserController(AuthenticationManager authenticationManager, UserInfoService service, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.service = service;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome this endpoint is not secure";
    }

    @PostMapping("/addNewUser")
    public String addNewUser(@RequestBody UserInfo userInfo) {
        return service.addUser(userInfo);
    }

    @PostMapping("/generateToken")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );
        if (authentication.isAuthenticated()) {
            return jwtUtil.generateToken(authRequest.getUsername());
        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }

    @GetMapping("/user")
    public String userPage() {
        return "This is the user page. Access granted!";
    }

    @GetMapping("/admin")
    public String adminPage() {
        return "This is the admin page. Access granted!";
    }
}
