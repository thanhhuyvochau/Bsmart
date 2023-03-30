package fpt.project.bsmart.entity.response;

import io.swagger.v3.oas.annotations.media.Schema;

public class MentorProfileResponse {
    private Long id;
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
