package fpt.project.bsmart.entity.dto;


import java.util.List;

public class ArchetypeTeacherDto {

    private Long id ;
    private String code ;
    private String name  ;

    private List<ArchetypeTimeTeacherDto> archetypeTime ;

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

    public List<ArchetypeTimeTeacherDto> getArchetypeTime() {
        return archetypeTime;
    }

    public void setArchetypeTime(List<ArchetypeTimeTeacherDto> archetypeTime) {
        this.archetypeTime = archetypeTime;
    }
}
