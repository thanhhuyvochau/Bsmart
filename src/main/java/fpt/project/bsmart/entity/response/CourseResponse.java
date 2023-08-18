package fpt.project.bsmart.entity.response;


import fpt.project.bsmart.entity.constant.ECourseLevel;
import fpt.project.bsmart.entity.constant.ECourseStatus;
import fpt.project.bsmart.entity.dto.CategoryDto;
import fpt.project.bsmart.entity.dto.ImageDto;
import fpt.project.bsmart.entity.dto.SubjectDto;

import java.util.List;

public class CourseResponse {

    private Long id;

    private List<ImageDto> images;

    private String courseCode;
    private String courseName;

    private CategoryDto categoryResponse;

    private SubjectDto subjectResponse;

    private ECourseLevel level;

    private ECourseStatus status;
    private String courseDescription;

    private int totalClass;
    private List<String> mentorName;
    private Double averageRate = 0.0;
    private Integer submissionCount = 0;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ECourseStatus getStatus() {
        return status;
    }

    public void setStatus(ECourseStatus status) {
        this.status = status;
    }

    public List<ImageDto> getImages() {
        return images;
    }

    public void setImages(List<ImageDto> images) {
        this.images = images;
    }

    public ECourseLevel getLevel() {
        return level;
    }

    public void setLevel(ECourseLevel level) {
        this.level = level;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
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

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public int getTotalClass() {
        return totalClass;
    }

    public void setTotalClass(int totalClass) {
        this.totalClass = totalClass;
    }


    public List<String> getMentorName() {
        return mentorName;
    }

    public void setMentorName(List<String> mentorName) {
        this.mentorName = mentorName;
    }

    public Double getAverageRate() {
        return averageRate;
    }

    public void setAverageRate(Double averageRate) {
        this.averageRate = averageRate;
    }

    public Integer getSubmissionCount() {
        return submissionCount;
    }

    public void setSubmissionCount(Integer submissionCount) {
        this.submissionCount = submissionCount;
    }
}
