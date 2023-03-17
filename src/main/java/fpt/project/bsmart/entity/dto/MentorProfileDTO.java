package fpt.project.bsmart.entity.dto;

import java.util.List;

public class MentorProfileDTO {
    private Long id;
    private String introduce;
    private String yearsOfExperience;
    private UserDto user;

    private List<SubjectDto> skillList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(String yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public List<SubjectDto> getSkillList() {
        return skillList;
    }

    public void setSkillList(List<SubjectDto> skillList) {
        this.skillList = skillList;
    }
}
