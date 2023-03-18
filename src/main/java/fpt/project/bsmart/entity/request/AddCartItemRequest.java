package fpt.project.bsmart.entity.request;


import java.io.Serializable;

public class AddCartItemRequest implements Serializable {

    private Long subCourseId;

    public Long getSubCourseId() {
        return subCourseId;
    }

    public void setSubCourseId(Long subCourseId) {
        this.subCourseId = subCourseId;
    }
}
