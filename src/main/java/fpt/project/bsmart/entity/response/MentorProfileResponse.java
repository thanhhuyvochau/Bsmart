package fpt.project.bsmart.entity.response;

import io.swagger.v3.oas.annotations.media.Schema;

public class MentorProfileResponse {
    @Schema(name = "Mentor profile id")
    private Long id;
    @Schema(name = "Mentor name")
    private String name;

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
}
