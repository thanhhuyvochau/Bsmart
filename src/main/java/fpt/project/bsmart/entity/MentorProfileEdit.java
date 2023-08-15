
package fpt.project.bsmart.entity;


import fpt.project.bsmart.entity.constant.EMentorProfileEditStatus;

import javax.persistence.*;

@Entity
@Table(name = "mentor_profile_edit")
public class MentorProfileEdit extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "profile_data")
    private String profileData;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentor_profile_id")
    private MentorProfile mentorProfile;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EMentorProfileEditStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProfileData() {
        return profileData;
    }

    public void setProfileData(String profileData) {
        this.profileData = profileData;
    }

    public MentorProfile getMentorProfile() {
        return mentorProfile;
    }

    public void setMentorProfile(MentorProfile mentorProfile) {
        this.mentorProfile = mentorProfile;
    }

    public EMentorProfileEditStatus getStatus() {
        return status;
    }

    public void setStatus(EMentorProfileEditStatus status) {
        this.status = status;
    }


}
