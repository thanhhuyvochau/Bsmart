package fpt.project.bsmart.entity.request;

import fpt.project.bsmart.entity.common.EAccountRole;

public class CreateAccountRequest {

    private String email;

    private String password;
    private EAccountRole role;

    public EAccountRole getRole() {
        return role;
    }

    public void setRole(EAccountRole role) {
        this.role = role;
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
}
