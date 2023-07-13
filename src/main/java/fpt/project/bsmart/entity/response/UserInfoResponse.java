package fpt.project.bsmart.entity.response;

import fpt.project.bsmart.entity.Role;
import fpt.project.bsmart.entity.constant.EGenderType;
import fpt.project.bsmart.entity.dto.RoleDto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class UserInfoResponse {
    private Long id;
    private String fullName;
    private String email;
    private Instant birthDay;
    private String phone;
    private Boolean status;
    List<RoleDto> roles = new ArrayList<>();
    private EGenderType genderType;
    private Boolean isVerified = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Instant getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Instant birthDay) {
        this.birthDay = birthDay;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<RoleDto> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleDto> roles) {
        this.roles = roles;
    }

    public EGenderType getGenderType() {
        return genderType;
    }

    public void setGenderType(EGenderType genderType) {
        this.genderType = genderType;
    }

    public Boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Boolean verified) {
        isVerified = verified;
    }
}
