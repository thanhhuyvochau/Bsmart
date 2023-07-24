package fpt.project.bsmart.entity.response.Category;

import fpt.project.bsmart.entity.dto.SubjectDto;
import fpt.project.bsmart.entity.response.Subject.SkillMentorResponse;

import java.util.List;

public class CategoryByMentorSkill {
    private Long id;
    private String code;
    private String name;

    private List<SkillMentorResponse> skills;

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

    public List<SkillMentorResponse> getSkills() {
        return skills;
    }

    public void setSkills(List<SkillMentorResponse> skills) {
        this.skills = skills;
    }
}
