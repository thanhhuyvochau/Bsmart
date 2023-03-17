package fpt.project.bsmart.entity.request;

import java.util.ArrayList;
import java.util.List;

public class UpdateMentorProfileRequest {
    private String introduce;
    private String yearOfExperiences;
    private List<Long> subjectIdList = new ArrayList<>();

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

    public List<Long> getSubjectIdList() {
        return subjectIdList;
    }

    public void setSubjectIdList(List<Long> subjectIdList) {
        this.subjectIdList = subjectIdList;
    }
}
