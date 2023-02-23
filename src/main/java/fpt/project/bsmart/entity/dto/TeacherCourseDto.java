package fpt.project.bsmart.entity.dto;


import io.swagger.v3.oas.annotations.media.Schema;

public class TeacherCourseDto {


    private Long topicId;


    private Long teacherId;

    private String firstName;
    private String lastName;


    @Schema(nullable = true)
    private Boolean isAllowed;

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public Boolean getIsAllowed() {
        return isAllowed;
    }

    public void setIsAllowed(Boolean allowed) {
        isAllowed = allowed;
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

    public Boolean getAllowed() {
        return isAllowed;
    }

    public void setAllowed(Boolean allowed) {
        isAllowed = allowed;
    }
}
