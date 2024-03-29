package fpt.project.bsmart.entity.dto;

import fpt.project.bsmart.entity.constant.FileType;

import java.time.Instant;


public class AssignmentFileDto {
    private Long id;
    private String url;
    private Instant uploadTime;
    private FileType fileType;
    private Float point;
    private String note;
    private String name;
    private FileDto metadata;

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
        this.metadata = new FileDto(this.url);
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

    public Float getPoint() {
        return point;
    }

    public void setPoint(Float point) {
        this.point = point;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FileDto getMetadata() {
        return metadata;
    }

    public void setMetadata(FileDto metadata) {
        this.metadata = metadata;
    }
}
