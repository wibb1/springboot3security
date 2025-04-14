package com.springboot3security.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {

    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> role;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Collection<? extends GrantedAuthority> getRole() {
        return role;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        Collection<? extends GrantedAuthority> roles = List.of(new SimpleGrantedAuthority("ROLE_" + role));
        this.role = roles;
    }
}
