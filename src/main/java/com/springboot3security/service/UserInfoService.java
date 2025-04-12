package com.springboot3security.service;

import com.springboot3security.entity.UserInfo;
import com.springboot3security.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserInfoService implements UserDetailsService {

    private final UserInfoRepository repository;

    private final PasswordEncoder encoder;

    public UserInfoService(UserInfoRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserInfo> userDetail = repository.findByUsername(username);

        // Converting UserInfo to UserDetails
        return userDetail.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    public String addUser(UserInfo userInfo) {
        if(!isValidPassword(userInfo.getPassword())) {
            return "Password does not meet security requirements";
        }
        if (repository.findByUsername(userInfo.getUsername()).isPresent()) {
            return "User already exists";
        }
        // Encode password before saving the user
        userInfo.setPassword(encoder.encode(userInfo.getPassword()));
        repository.save(userInfo);
        return "User Added Successfully";
    }

    public String getUserInfo(String username) {
        Optional<UserInfo> userDetail = repository.findByUsername(username);
        if (userDetail.isPresent()) {
            return "User Info: " + userDetail.get();
        } else {
            return "User not found";
        }
    }

    private boolean isValidPassword(String password) {
        // Example: Minimum 8 characters, at least one uppercase, one lowercase, and one number
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$");
    }
}
