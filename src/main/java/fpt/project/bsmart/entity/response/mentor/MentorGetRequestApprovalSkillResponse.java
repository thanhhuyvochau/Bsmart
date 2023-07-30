package fpt.project.bsmart.entity.response.mentor;

import fpt.project.bsmart.entity.dto.ImageDto;
import fpt.project.bsmart.entity.dto.MentorSkillDto;
import fpt.project.bsmart.entity.dto.UserDto;

import java.time.Instant;
import java.util.List;

public class MentorGetRequestApprovalSkillResponse {


   private List<MentorSkillDto> mentorSkillRequest ;

   private List<ImageDto> degreeRequest ;

   private Instant created ;

   private Integer totalSkillRequest = 0;

    private Integer totalDegreeRequest = 0;


    public List<MentorSkillDto> getMentorSkillRequest() {
        return mentorSkillRequest;
    }

    public void setMentorSkillRequest(List<MentorSkillDto> mentorSkillRequest) {
        this.mentorSkillRequest = mentorSkillRequest;
    }

    public List<ImageDto> getDegreeRequest() {
        return degreeRequest;
    }

    public void setDegreeRequest(List<ImageDto> degreeRequest) {
        this.degreeRequest = degreeRequest;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Integer getTotalSkillRequest() {
        return totalSkillRequest;
    }

    public void setTotalSkillRequest(Integer totalSkillRequest) {
        this.totalSkillRequest = totalSkillRequest;
    }

    public Integer getTotalDegreeRequest() {
        return totalDegreeRequest;
    }

    public void setTotalDegreeRequest(Integer totalDegreeRequest) {
        this.totalDegreeRequest = totalDegreeRequest;
    }
}
