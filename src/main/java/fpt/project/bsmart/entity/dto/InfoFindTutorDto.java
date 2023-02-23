package fpt.project.bsmart.entity.dto;


import fpt.project.bsmart.entity.common.EClassLevel;

import java.util.List;


public class InfoFindTutorDto {



    private String fullName;


    private String email;


    private String phone;



    private String address;


    private EClassLevel classLevel;



    private String description;



    private List<Long> teacherId;

    private List<Long> subjectId;

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

    public List<Long> getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(List<Long> teacherId) {
        this.teacherId = teacherId;
    }

    public List<Long> getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(List<Long> subjectId) {
        this.subjectId = subjectId;
    }
}
