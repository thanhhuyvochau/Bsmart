package fpt.project.bsmart.entity.dto.mentor;

import fpt.project.bsmart.entity.dto.ImageDto;
import fpt.project.bsmart.entity.dto.MentorSkillDto;

import java.util.List;

public class MentorDto {

    private Long id;

    private String name ;

    private String introduce;

    private List<MentorSkillDto> mentorSkills;
    private ImageDto avatar ;

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

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }


    public List<MentorSkillDto> getMentorSkills() {
        return mentorSkills;
    }

    public void setMentorSkills(List<MentorSkillDto> mentorSkills) {
        this.mentorSkills = mentorSkills;
    }

    public ImageDto getAvatar() {
        return avatar;
    }

    public void setAvatar(ImageDto avatar) {
        this.avatar = avatar;
    }
}
