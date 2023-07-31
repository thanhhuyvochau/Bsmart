package fpt.project.bsmart.entity.dto;

public class ResourceDto {

    private Long id;

    private FileDto metadata;

    public ResourceDto(Long id, String url) {
        this.id = id;
        this.metadata = new FileDto(url);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FileDto getMetadata() {
        return metadata;
    }

    public void setMetadata(FileDto metadata) {
        this.metadata = metadata;
    }
}
