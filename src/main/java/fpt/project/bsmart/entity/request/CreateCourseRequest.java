package fpt.project.bsmart.entity.request;

import fpt.project.bsmart.entity.constant.ECourseLevel;
import fpt.project.bsmart.entity.constant.ETypeLearn;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class CreateCourseRequest {
    private String code;
    private String name;
    private Long categoryId;
    private Long subjectId;
    private String description;

    private List<CreateSubCourseRequest> subCourseRequests ;

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

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<CreateSubCourseRequest> getSubCourseRequests() {
        return subCourseRequests;
    }

    public void setSubCourseRequests(List<CreateSubCourseRequest> subCourseRequests) {
        this.subCourseRequests = subCourseRequests;
    }
}
