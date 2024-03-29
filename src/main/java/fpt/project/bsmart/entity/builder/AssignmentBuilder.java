package fpt.project.bsmart.entity.builder;

import fpt.project.bsmart.entity.Activity;
import fpt.project.bsmart.entity.Assignment;
import fpt.project.bsmart.entity.AssignmentFile;
import fpt.project.bsmart.entity.constant.EAssignmentStatus;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class AssignmentBuilder {
    private String description;
    private Integer editBeForSubmitMin = 0;
    private Integer maxFileSubmit = 1;
    private Integer maxFileSize = 5; // Dd
    private EAssignmentStatus status;
    private Activity activity;
    private List<AssignmentFile> assignmentFiles = new ArrayList<>();
    private Long passPoint = 5L; // Default 5
    private String password;

    public AssignmentBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public AssignmentBuilder setEditBeForSubmitMin(Integer editBeForSubmitMin) {
        this.editBeForSubmitMin = editBeForSubmitMin;
        return this;
    }

    public AssignmentBuilder setMaxFileSubmit(Integer maxFileSubmit) {
        this.maxFileSubmit = maxFileSubmit;
        return this;
    }

    public AssignmentBuilder setMaxFileSize(Integer maxFileSize) {
        this.maxFileSize = maxFileSize;
        return this;
    }

    public AssignmentBuilder setStatus(EAssignmentStatus status) {
        this.status = status;
        return this;
    }

    public AssignmentBuilder setActivity(Activity activity) {
        this.activity = activity;
        return this;
    }

    public AssignmentBuilder setAssignmentFiles(List<AssignmentFile> assignmentFiles) {
        this.assignmentFiles = assignmentFiles;
        return this;
    }

    public AssignmentBuilder setPassPoint(Long passPoint) {
        this.passPoint = passPoint;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Assignment build() {
        // TODO: Validation
        Assignment assignment = new Assignment(description, editBeForSubmitMin, maxFileSubmit, maxFileSize, status, activity, assignmentFiles, passPoint,password);
        return assignment;
    }

    public static AssignmentBuilder getBuilder() {
        return new AssignmentBuilder();
    }
}
