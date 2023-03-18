package fpt.project.bsmart.entity.request;


import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class AddCartItemRequest implements Serializable {
    @NotNull
    private Long subCourseId;

    public Long getSubCourseId() {
        return subCourseId;
    }

    public void setSubCourseId(Long subCourseId) {
        this.subCourseId = subCourseId;
    }
}
