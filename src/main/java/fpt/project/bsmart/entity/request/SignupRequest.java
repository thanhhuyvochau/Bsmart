package fpt.project.bsmart.entity.request;

import fpt.project.bsmart.entity.constant.EUserRole;

public class SignupRequest {
    private String email ;
    private String password ;
    private EUserRole role;

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

    public EUserRole getRole() {
        return role;
    }

    public void setRole(EUserRole role) {
        this.role = role;
    }
}
