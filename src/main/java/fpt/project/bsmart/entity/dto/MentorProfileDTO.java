package fpt.project.bsmart.entity.dto;

import java.util.List;

public class MentorProfileDTO {
    private Long id;
    private String introduce;
    private String workingExperience;
    private UserDto user;

    private List<MentorSkillDto> skillList;

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

    public String getWorkingExperience() {
        return workingExperience;
    }

    public void setWorkingExperience(String workingExperience) {
        this.workingExperience = workingExperience;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public List<MentorSkillDto> getSkillList() {
        return skillList;
    }

    public void setSkillList(List<MentorSkillDto> skillList) {
        this.skillList = skillList;
    }
}
