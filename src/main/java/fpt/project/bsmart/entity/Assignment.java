package fpt.project.bsmart.entity;

import fpt.project.bsmart.entity.constant.EAssignmentStatus;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "assignment")
public class Assignment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "description", columnDefinition = "BLOB")
    private String description;
    @Column(name = "startDate")
    private Instant startDate;
    @Column(name = "endDate")
    private Instant endDate;
    @Column(name = "edit_before_submit_min")
    private Integer editBeForSubmitMin = 0;
    @Column(name = "max_file_submit")
    private Integer maxFileSubmit = 1;
    @Column(name = "max_file_size")
    private Integer maxFileSize = 5; // Dd
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EAssignmentStatus status;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "activity_id")
    private Activity activity;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "assignment", fetch = FetchType.LAZY)
    private List<AssignmentFile> assignmentFiles = new ArrayList<>();
    @Column(name = "pass_point")
    private Long passPoint = 5L; // Default 5

    @OneToMany(mappedBy = "assignment", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<AssignmentSubmition> assignmentSubmitions = new ArrayList<>();

    public Assignment(String description, Instant startDate, Instant endDate, Integer editBeForSubmitMin, Integer maxFileSubmit, Integer maxFileSize, EAssignmentStatus status, Activity activity, List<AssignmentFile> assignmentFiles, Long passPoint) {
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.editBeForSubmitMin = editBeForSubmitMin;
        this.maxFileSubmit = maxFileSubmit;
        this.maxFileSize = maxFileSize;
        this.status = status;
        this.activity = activity;
        this.assignmentFiles = assignmentFiles;
        this.passPoint = passPoint;
    }

    public Assignment() {
    }

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

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public List<AssignmentFile> getAssignmentFiles() {
        return assignmentFiles;
    }

    public void setAssignmentFiles(List<AssignmentFile> assignmentFiles) {
        this.assignmentFiles = assignmentFiles;
    }

    public Long getPassPoint() {
        return passPoint;
    }

    public void setPassPoint(Long passPoint) {
        this.passPoint = passPoint;
    }

    public List<AssignmentSubmition> getAssignmentSubmitions() {
        return assignmentSubmitions;
    }

    public void setAssignmentSubmitions(List<AssignmentSubmition> assignmentSubmitions) {
        this.assignmentSubmitions = assignmentSubmitions;
    }
}
