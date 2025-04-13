package com.springboot3security.service;

import com.springboot3security.entity.UserInfo;
import com.springboot3security.repository.UserInfoRepository;
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
public class UserInfoService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserInfoService.class);
    private final UserInfoRepository repository;

    private final PasswordEncoder encoder;

    public UserInfoService(UserInfoRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Attempting to load user by username: {}", username);
        Optional<UserInfo> userDetail = repository.findByUsername(username);

        // Converting UserInfo to UserDetails
        return userDetail.map(UserInfoDetails::new)
                .orElseThrow(() -> {
                    logger.error("User not found: {}", username);
                    return new UsernameNotFoundException("User not found: " + username);
                });
    }

    public String addUser(UserInfo userInfo) {
        logger.info("Attempting to add new user: {}", userInfo.getUsername());
        if(!PasswordValidationUtil.isValidPassword(userInfo.getPassword())) {
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
        logger.info("User added successfully: {}", userInfo.getUsername());
        return "User Added Successfully";
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
