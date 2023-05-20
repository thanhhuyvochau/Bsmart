package fpt.project.bsmart.entity.response;


import fpt.project.bsmart.entity.constant.ETypeLearn;
import fpt.project.bsmart.entity.dto.ImageDto;

import java.util.List;

public class CourseResponse {

    private Long id;

    private List<ImageDto>images ;

    private String courseCode;
    private String courseName;

    private Long categoryId;
    private String categoryName;

    private Long subjectId;
    private String subjectName;


    private String courseDescription;

    private int totalSubCourse ;

    private List<ETypeLearn> learns ;

    private List<String> mentorName ;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ImageDto> getImages() {
        return images;
    }

    public void setImages(List<ImageDto> images) {
        this.images = images;
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

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }



    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public int getTotalSubCourse() {
        return totalSubCourse;
    }

    public void setTotalSubCourse(int totalSubCourse) {
        this.totalSubCourse = totalSubCourse;
    }

    public List<ETypeLearn> getLearns() {
        return learns;
    }

    public void setLearns(List<ETypeLearn> learns) {
        this.learns = learns;
    }

    public List<String> getMentorName() {
        return mentorName;
    }

    public void setMentorName(List<String> mentorName) {
        this.mentorName = mentorName;
    }
}
