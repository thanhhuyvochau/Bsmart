package fpt.project.bsmart.entity.request.User;

import fpt.project.bsmart.entity.constant.EUserRole;

public class UserSearchRequest {
    private String q;
    private EUserRole role;
    private Boolean isVerified;

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public EUserRole getRole() {
        return role;
    }

    public void setRole(EUserRole role) {
        this.role = role;
    }

    public Boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Boolean verified) {
        isVerified = verified;
    }
}
