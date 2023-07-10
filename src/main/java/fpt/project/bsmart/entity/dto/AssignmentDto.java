package fpt.project.bsmart.entity.dto;

import fpt.project.bsmart.config.json.View;
import fpt.project.bsmart.entity.BaseEntity;
import fpt.project.bsmart.entity.constant.EAssignmentStatus;
import org.codehaus.jackson.map.annotate.JsonView;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


public class AssignmentDto extends BaseEntity {
    private Long id;

    private String description;

    private Instant startDate;

    private Instant endDate;
    @JsonView(View.Teacher.class)
    private Integer editBeForSubmitMin = 0;
    @JsonView(View.Teacher.class)
    private Integer maxFileSubmit = 1;
    @JsonView(View.Teacher.class)
    private Integer maxFileSize = 5; // Dd
    private EAssignmentStatus status;
    @JsonView(View.Teacher.class)
    private List<AssignmentFileDto> assignmentFiles = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Integer getEditBeForSubmitMin() {
        return editBeForSubmitMin;
    }

    public void setEditBeForSubmitMin(Integer editBeForSubmitMin) {
        this.editBeForSubmitMin = editBeForSubmitMin;
    }

    public Integer getMaxFileSubmit() {
        return maxFileSubmit;
    }

    public void setMaxFileSubmit(Integer maxFileSubmit) {
        this.maxFileSubmit = maxFileSubmit;
    }

    public Integer getMaxFileSize() {
        return maxFileSize;
    }

    public void setMaxFileSize(Integer maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    public EAssignmentStatus getStatus() {
        return status;
    }

    public void setStatus(EAssignmentStatus status) {
        this.status = status;
    }

    public List<AssignmentFileDto> getAssignmentFiles() {
        return assignmentFiles;
    }

    public void setAssignmentFiles(List<AssignmentFileDto> assignmentFiles) {
        this.assignmentFiles = assignmentFiles;
    }
}
