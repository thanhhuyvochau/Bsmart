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
    @Column(name = "year_of_experience")
    private String yearsOfExperience;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "status")
    private Boolean status = false;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "mentor_skill",
            joinColumns = @JoinColumn(name = "mentor_profile_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id"))
    private List<Subject> skills = new ArrayList<>();

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

    public List<Subject> getSkills() {
        return skills;
    }

    public void setSkills(List<Subject> skills) {
        this.skills = skills;
    }
}
