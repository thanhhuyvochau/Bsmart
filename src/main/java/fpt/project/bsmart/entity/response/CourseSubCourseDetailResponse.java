package fpt.project.bsmart.entity.response;


import fpt.project.bsmart.entity.dto.CategoryDto;
import fpt.project.bsmart.entity.dto.SubjectDto;

import java.util.ArrayList;
import java.util.List;


public class CourseSubCourseDetailResponse {

    private Long id;
    private String name;

    private String code;


    private String description;

    private List<CourseSubCourseDetailResponse> subCourses ;

    private SubjectDto subject;

    private List<CategoryDto> categoryDtoList = new ArrayList<>();



    private Long mentorId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

//    public List<SubCourseDetailResponse> getSubCourses() {
//        return subCourses;
//    }
//
//    public void setSubCourses(List<SubCourseDetailResponse> subCourses) {
//        this.subCourses = subCourses;
//    }

    public SubjectDto getSubject() {
        return subject;
    }

    public void setSubject(SubjectDto subject) {
        this.subject = subject;
    }

    public List<CategoryDto> getCategoryDtoList() {
        return categoryDtoList;
    }

    public void setCategoryDtoList(List<CategoryDto> categoryDtoList) {
        this.categoryDtoList = categoryDtoList;
    }

    public Long getMentorId() {
        return mentorId;
    }

    public void setMentorId(Long mentorId) {
        this.mentorId = mentorId;
    }
}
