package fpt.project.bsmart.entity.response;


import fpt.project.bsmart.entity.constant.ECourseLevel;
import fpt.project.bsmart.entity.constant.ECourseStatus;
import fpt.project.bsmart.entity.constant.ECourseType;
import fpt.project.bsmart.entity.dto.CategoryDto;
import fpt.project.bsmart.entity.dto.ImageDto;
import fpt.project.bsmart.entity.dto.SubjectDto;
import fpt.project.bsmart.entity.dto.TimeInWeekDTO;
import fpt.project.bsmart.entity.dto.activity.SectionDto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class CourseClassResponse {

    private Long id;
    private String code;
    private String name;

    private String description;

    private CategoryDto categoryResponse;

    private SubjectDto subjectResponse;


    private ECourseStatus status;


    private String mentorName;
    private List<ClassDetailResponse> classes;

    private List<SectionDto> sections;

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

    public ECourseStatus getStatus() {
        return status;
    }

    public void setStatus(ECourseStatus status) {
        this.status = status;
    }

    public String getMentorName() {
        return mentorName;
    }

    public void setMentorName(String mentorName) {
        this.mentorName = mentorName;
    }

    public List<ClassDetailResponse> getClasses() {
        return classes;
    }

    public void setClasses(List<ClassDetailResponse> classes) {
        this.classes = classes;
    }

    public List<SectionDto> getSections() {
        return sections;
    }

    public void setSections(List<SectionDto> sections) {
        this.sections = sections;
    }
}
