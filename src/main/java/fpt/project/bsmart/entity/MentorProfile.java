package fpt.project.bsmart.entity;

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
    private Boolean status = false;

    @OneToMany(mappedBy = "mentorProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MentorSkill> skills = new ArrayList<>();

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

    public void setWorkingExperience(String yearsOfExperience) {
        this.workingExperience = yearsOfExperience;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<MentorSkill> getSkills() {
        return skills;
    }

    public void setSkills(List<MentorSkill> skills) {
        this.skills.clear();
        if(skills != null){
            this.skills.addAll(skills);
        }
    }
}
