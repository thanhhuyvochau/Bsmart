package fpt.project.bsmart.entity.request;


import java.time.Instant;

public class StudentRequest {

    private String userName  ;
    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String phone;

    private Instant birthDay;

    private String gender;


    private String currentAddress;

    private String schoolName;

//    private List<Long> subjects ;

//    private Long classLevel ;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Instant getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Instant birthDay) {
        this.birthDay = birthDay;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }



    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

//    public List<Long> getSubjects() {
//        return subjects;
//    }
//
//    public void setSubjects(List<Long> subjects) {
//        this.subjects = subjects;
//    }
//
//    public Long getClassLevel() {
//        return classLevel;
//    }
//
//    public void setClassLevel(Long classLevel) {
//        this.classLevel = classLevel;
//    }
}
