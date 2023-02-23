package fpt.project.bsmart.entity.dto;


import java.util.List;

public class ArchetypeDto {

    private Long id;
    private String code;
    private String name;
    private Long createdByTeacherId;
    private List<ArchetypeTimeDto> archetypeTimes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCreatedByTeacherId() {
        return createdByTeacherId;
    }

    public void setCreatedByTeacherId(Long createdByTeacherId) {
        this.createdByTeacherId = createdByTeacherId;
    }

    public List<ArchetypeTimeDto> getArchetypeTimes() {
        return archetypeTimes;
    }

    public void setArchetypeTimes(List<ArchetypeTimeDto> archetypeTimes) {
        this.archetypeTimes = archetypeTimes;
    }
}
