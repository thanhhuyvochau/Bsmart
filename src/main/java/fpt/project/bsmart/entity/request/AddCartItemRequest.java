package fpt.project.bsmart.entity.request;


import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class AddCartItemRequest implements Serializable {
    @NotNull
    private Long classId;

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }
}
