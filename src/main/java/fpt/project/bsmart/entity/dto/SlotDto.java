package fpt.project.bsmart.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import java.time.LocalTime;

public class SlotDto {
    @Schema(description = "Id của slot", readOnly = true)
    private Long id;
    @Schema(description = "Tên slot")
    @NotNull
    private String name;
    @Schema(description = "Code slot")
    @NotNull
    private String code;
    @Schema(description = "Thời gian bắt đầu của slot")
    @NotNull
    private LocalTime startTime;
    @Schema(description = "Thời gian kết thúc của slot")
    @NotNull
    private LocalTime endTime;

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

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
}
