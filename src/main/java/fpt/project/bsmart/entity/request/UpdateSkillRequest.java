package fpt.project.bsmart.entity.request;

public class UpdateSkillRequest {
    private Long skillId;
    private int yearOfExperiences;

    public Long getSkillId() {
        return skillId;
    }

    public void setSkillId(Long skillId) {
        this.skillId = skillId;
    }

    public int getYearOfExperiences() {
        return yearOfExperiences;
    }

    public void setYearOfExperiences(int yearOfExperiences) {
        this.yearOfExperiences = yearOfExperiences;
    }
}
