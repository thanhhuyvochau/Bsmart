package fpt.project.bsmart.entity.dto;


import fpt.project.bsmart.entity.Class;



public class StudentClassDto {

    private Long id;

    private Class clazz;

    private UserDto student;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public UserDto getStudent() {
        return student;
    }

    public void setStudent(UserDto student) {
        this.student = student;
    }
}
