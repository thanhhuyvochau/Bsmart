package fpt.project.bsmart.entity.dto;

import fpt.project.bsmart.entity.common.EFileType;



public class ExerciseDto {

    private Long id;


    private String name;


    private String url;

    private EFileType fileType ;


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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public EFileType getFileType() {
        return fileType;
    }

    public void setFileType(EFileType fileType) {
        this.fileType = fileType;
    }
}
