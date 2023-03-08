package fpt.project.bsmart.entity.request;


import fpt.project.bsmart.entity.constant.EGenderType;
import fpt.project.bsmart.entity.constant.EUserRole;

import java.io.Serializable;
import java.time.Instant;

public class AccountEditRequest implements Serializable {

    private String firstName;
    private String lastName;
    private Instant birthDay;
    private String phone;

    private String mail;

    private EUserRole role;

    private boolean isActive;
    private EGenderType gender;


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Instant getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Instant birthDay) {
        this.birthDay = birthDay;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public EUserRole getRole() {
        return role;
    }

    public void setRole(EUserRole role) {
        this.role = role;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public EGenderType getGender() {
        return gender;
    }

    public void setGender(EGenderType gender) {
        this.gender = gender;
    }
}
