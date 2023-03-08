package fpt.project.bsmart.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalTime;

public class SlotDto implements Serializable {
//    {
//            "name":"Slot 1",
//            "code":"S1",
//            "startTime":"09:00",
//            "endTime":"10:00"
//    }


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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime startTime;
    @Schema(description = "Thời gian kết thúc của slot")
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
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
