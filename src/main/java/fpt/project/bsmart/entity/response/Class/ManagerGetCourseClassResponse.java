package fpt.project.bsmart.entity.response.Class;

import fpt.project.bsmart.entity.constant.ECourseClassStatus;
import fpt.project.bsmart.entity.constant.ECourseLevel;
import fpt.project.bsmart.entity.dto.*;
import fpt.project.bsmart.entity.dto.mentor.MentorDto;
import fpt.project.bsmart.entity.response.ClassDetailResponse;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ManagerGetCourseClassResponse {
    private Long id;
    private String code;
    private String name;

    private String description;

    private ECourseLevel level ;
    private CategoryDto categoryResponse;

    private SubjectDto subjectResponse;

    private ECourseClassStatus status;

    private MentorDto mentor;
    private List<ClassDetailResponse> classes;

    private List<ActivityDto> activities = new ArrayList<>();

    private Instant timeSendRequest;
    private Integer count;
    private Boolean isApproved;
    private String classURL;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ECourseLevel getLevel() {
        return level;
    }

    public void setLevel(ECourseLevel level) {
        this.level = level;
    }

    public CategoryDto getCategoryResponse() {
        return categoryResponse;
    }

    public void setCategoryResponse(CategoryDto categoryResponse) {
        this.categoryResponse = categoryResponse;
    }

    public SubjectDto getSubjectResponse() {
        return subjectResponse;
    }

    public void setSubjectResponse(SubjectDto subjectResponse) {
        this.subjectResponse = subjectResponse;
    }

    public ECourseClassStatus getStatus() {
        return status;
    }

    public void setStatus(ECourseClassStatus status) {
        this.status = status;
    }

    public MentorDto getMentor() {
        return mentor;
    }

    public void setMentor(MentorDto mentor) {
        this.mentor = mentor;
    }

    public List<ClassDetailResponse> getClasses() {
        return classes;
    }

    public void setClasses(List<ClassDetailResponse> classes) {
        this.classes = classes;
    }

    public List<ActivityDto> getActivities() {
        return activities;
    }

    public void setActivities(List<ActivityDto> activities) {
        this.activities = activities;
    }

    public Instant getTimeSendRequest() {
        return timeSendRequest;
    }

    public void setTimeSendRequest(Instant timeSendRequest) {
        this.timeSendRequest = timeSendRequest;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Boolean getApproved() {
        return isApproved;
    }

    public void setApproved(Boolean approved) {
        isApproved = approved;
    }

    public String getClassURL() {
        return classURL;
    }

    public void setClassURL(String classURL) {
        this.classURL = classURL;
    }
}
