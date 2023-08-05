package fpt.project.bsmart.entity;

import fpt.project.bsmart.entity.constant.EMentorProfileStatus;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "mentor_profile")
public class MentorProfile extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "introduce")
    private String introduce;
    @Column(name = "working_experience")
    private String workingExperience;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EMentorProfileStatus status;

    @Column(name = "is_interviewed")
    private boolean isInterviewed = false;


    @OneToMany(mappedBy = "mentorProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MentorSkill> skills = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isInterviewed() {
        return isInterviewed;
    }

    public void setInterviewed(boolean interviewed) {
        isInterviewed = interviewed;
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

    public void setWorkingExperience(String yearsOfExperience) {
        this.workingExperience = yearsOfExperience;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public EMentorProfileStatus getStatus() {
        return status;
    }

    public void setStatus(EMentorProfileStatus status) {
        this.status = status;
    }

    public List<MentorSkill> getSkills() {
        return skills;
    }

    public void setSkills(List<MentorSkill> skills) {
        this.skills.clear();
        if (skills != null) {
            this.skills.addAll(skills);
        }
    }
}
