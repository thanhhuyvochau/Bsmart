package fpt.project.bsmart.entity.request;

import java.util.List;

public class MentorCreateClassRequest {

    private String name;
    private Long categoryId;
    private Long subjectId;
    private String description;

    private List<CreateClassInformationRequest> createClassRequest ;


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

    public List<CreateClassInformationRequest> getCreateClassRequest() {
        return createClassRequest;
    }

    public void setCreateClassRequest(List<CreateClassInformationRequest> createClassRequest) {
        this.createClassRequest = createClassRequest;
    }
}
