package fpt.project.bsmart.entity.response;


import fpt.project.bsmart.entity.common.EClassLevel;

import java.util.List;


public class InfoFindTutorResponse {



    private String fullName;


    private String email;


    private String phone;



    private String address;


    private EClassLevel classLevel;



    private String description;



    private List<AccountResponse> teachers;

    private List<SubjectResponse> subjects;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public EClassLevel getClassLevel() {
        return classLevel;
    }

    public void setClassLevel(EClassLevel classLevel) {
        this.classLevel = classLevel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<AccountResponse> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<AccountResponse> teachers) {
        this.teachers = teachers;
    }

    public List<SubjectResponse> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<SubjectResponse> subjects) {
        this.subjects = subjects;
    }
}
