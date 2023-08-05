package fpt.project.bsmart.entity.dto.mentor;

import fpt.project.bsmart.entity.dto.ImageDto;
import fpt.project.bsmart.entity.dto.MentorSkillDto;

import java.time.Instant;
import java.util.List;

public class MentorDto {

    private Long id;

    private String name ;

    private String email ;

    private String introduce;

    private String phone;


    private Instant timeParticipation  ;

    private List<MentorSkillDto> mentorSkills;
    private ImageDto avatar ;

    private TeachInformationDTO  teachInformation ;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Instant getTimeParticipation() {
        return timeParticipation;
    }

    public void setTimeParticipation(Instant timeParticipation) {
        this.timeParticipation = timeParticipation;
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

    public TeachInformationDTO getTeachInformation() {
        return teachInformation;
    }

    public void setTeachInformation(TeachInformationDTO teachInformation) {
        this.teachInformation = teachInformation;
    }
}
