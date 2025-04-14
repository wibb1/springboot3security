package com.springboot3security.service;

import com.springboot3security.entity.UserInfo;
import com.springboot3security.repository.UserInfoRepository;
import com.springboot3security.util.JwtUtil;
import com.springboot3security.util.PasswordValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserInfoService {

    private static final Logger logger = LoggerFactory.getLogger(UserInfoService.class);
    private final UserInfoRepository repository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder encoder;
    private final UserDetailsService userDetailsService;

    public UserInfoService(UserInfoRepository repository, PasswordEncoder encoder, JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.repository = repository;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    public String addUser(UserInfo userInfo) {
        logger.info("Attempting to add new user: {}", userInfo.getUsername());
        if (!PasswordValidationUtil.isValidPassword(userInfo.getPassword())) {
            String errorMessage = PasswordValidationUtil.getPasswordRequirements();
            logger.error("Password validation failed for user: {}. {}", userInfo.getUsername(), errorMessage);
            return errorMessage;
        }
        if (repository.findByUsername(userInfo.getUsername()).isPresent()) {
            logger.warn("User already exists: {}", userInfo.getUsername());
            return "User already exists";
        }
        // Encode password before saving the user
        userInfo.setPassword(encoder.encode(userInfo.getPassword()));
        repository.save(userInfo);
        logger.info("User successfully added: {}", userInfo.getUsername());
        return "User Added Successfully";
    }

    public String authenticateAndGetToken(String username) {
        logger.info("Authenticating user: {}", username);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return jwtUtil.generateToken(userDetails);
    }

    public String getUserInfo(String username) {
        logger.info("Fetching user info for username: {}", username);
        Optional<UserInfo> userDetail = repository.findByUsername(username);
        if (userDetail.isPresent()) {
            logger.info("User info retrieved successfully for username: {}", username);
            return "User Info: " + userDetail.get();
        } else {
            logger.error("User not found: {}", username);
            return "User not found";
        }
    }


}
