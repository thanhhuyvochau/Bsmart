package fpt.project.bsmart.entity.request;

import java.util.ArrayList;
import java.util.List;

public class UpdateMentorProfileRequest {
    private String introduce;
    private String yearOfExperiences;
    private List<Long> subjectIds = new ArrayList<>();

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getYearOfExperiences() {
        return yearOfExperiences;
    }

    public void setYearOfExperiences(String yearOfExperiences) {
        this.yearOfExperiences = yearOfExperiences;
    }

    public List<Long> getSubjectIds() {
        return subjectIds;
    }

    public void setSubjectIds(List<Long> subjectIds) {
        this.subjectIds = subjectIds;
    }
}
