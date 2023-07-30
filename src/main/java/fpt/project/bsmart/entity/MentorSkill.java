package fpt.project.bsmart.entity;

import javax.persistence.*;

@Entity
@Table(name = "mentor_skill")
public class MentorSkill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @ManyToOne
    @JoinColumn(name = "skill_id")
    private Subject skill;
    @Column(name = "year_of_experiences")
    private int yearOfExperiences;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentor_profile_id")
    private MentorProfile mentorProfile;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "verified")
    private Boolean verified ;
    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Subject getSkill() {
        return skill;
    }

    public void setSkill(Subject skillId) {
        this.skill = skillId;
    }

    public int getYearOfExperiences() {
        return yearOfExperiences;
    }

    public void setYearOfExperiences(int yearOfExperiences) {
        this.yearOfExperiences = yearOfExperiences;
    }

    public MentorProfile getMentorProfile() {
        return mentorProfile;
    }

    public void setMentorProfile(MentorProfile mentorProfile) {
        this.mentorProfile = mentorProfile;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }
}
