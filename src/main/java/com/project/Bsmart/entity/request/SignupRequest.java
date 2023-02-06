package com.project.Bsmart.entity.request;

import com.project.Bsmart.entity.common.ERole;

import java.util.List;

public class SignupRequest {

    private String userName;
    private String email;
    private String password;

    private List<String> roles ;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
