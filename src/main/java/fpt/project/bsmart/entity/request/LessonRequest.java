package fpt.project.bsmart.entity.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class LessonRequest extends ActivityRequest {
    @NotEmpty
    @NotNull
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
