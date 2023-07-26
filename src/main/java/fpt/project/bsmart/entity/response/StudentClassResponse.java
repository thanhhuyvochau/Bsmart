package fpt.project.bsmart.entity.response;


import fpt.project.bsmart.entity.dto.ImageDto;

public class StudentClassResponse {
    private Long id;

    private ImageDto images ;

    private String email;

    private String name;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ImageDto getImages() {
        return images;
    }

    public void setImages(ImageDto images) {
        this.images = images;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
