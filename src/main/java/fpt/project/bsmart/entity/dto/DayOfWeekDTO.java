package fpt.project.bsmart.entity.dto;

import fpt.project.bsmart.entity.constant.EDayOfWeekCode;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;

public class DayOfWeekDTO {
    @Schema(description = "Id của thứ", readOnly = true)
    private Long id;
    @Schema(description = "Tên thứ")
    @NotNull
    private String name;
    @Schema(description = "Code của thứ")
    @NotNull
    private EDayOfWeekCode code;

    public DayOfWeekDTO() {
    }

    public DayOfWeekDTO(Long id, String name, EDayOfWeekCode code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

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

    public EDayOfWeekCode getCode() {
        return code;
    }

    public void setCode(EDayOfWeekCode code) {
        this.code = code;
    }
}
