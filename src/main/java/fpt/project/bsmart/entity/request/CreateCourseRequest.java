package fpt.project.bsmart.entity.request;

import java.util.List;

public class CreateCourseRequest {

    private String name;
    private Long categoryId;
    private Long subjectId;
    private String description;

    private List<CreateSubCourseRequest> subCourseRequests ;


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
