package fpt.project.bsmart.entity;

import fpt.project.bsmart.entity.constant.FileType;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "activity_file")
public class AssignmentFile extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "url")
    private String url;
    @Column(name = "upload_time")
    private Instant uploadTime;
    @Column(name = "file_type")
    private FileType fileType;
    @ManyToOne
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;
    @Column(name = "name")
    private String name;
    @ManyToOne
    @JoinColumn(name = "assignment_submition")
    private AssignmentSubmition assignmentSubmition;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Instant getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Instant uploadTime) {
        this.uploadTime = uploadTime;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public AssignmentSubmition getAssignmentSubmition() {
        return assignmentSubmition;
    }

    public void setAssignmentSubmition(AssignmentSubmition assignmentSubmition) {
        this.assignmentSubmition = assignmentSubmition;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
