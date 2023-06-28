package fpt.project.bsmart.entity.request;

import fpt.project.bsmart.entity.constant.EGenderType;
import fpt.project.bsmart.entity.constant.EUserRole;

import javax.validation.constraints.NotNull;
import java.time.Instant;

public class CreateAccountRequest {
    @NotNull
    private String fullName;
    @NotNull
    private String email;
    @NotNull
    private String phone;
    @NotNull
    private String password;
    @NotNull
    private EUserRole role;

    private EGenderType gender;
    @NotNull
    private Instant birthDay;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public EGenderType getGender() {
        return gender;
    }

    public void setGender(EGenderType gender) {
        this.gender = gender;
    }

    public Instant getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Instant birthDay) {
        this.birthDay = birthDay;
    }
}
