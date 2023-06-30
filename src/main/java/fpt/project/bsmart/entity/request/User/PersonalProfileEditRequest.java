package fpt.project.bsmart.entity.request.User;

import fpt.project.bsmart.entity.constant.EGenderType;

import java.time.Instant;

public class PersonalProfileEditRequest {
    private String fullname;
    private Instant birthday;
    private String address;
    private String phone;

    private EGenderType gender ;

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Instant getBirthday() {
        return birthday;
    }

    public void setBirthday(Instant birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public EGenderType getGender() {
        return gender;
    }

    public void setGender(EGenderType gender) {
        this.gender = gender;
    }
}
