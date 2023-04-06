package fpt.project.bsmart.entity.request;

import java.io.Serializable;
import java.util.List;

public class MentorSearchRequest implements Serializable {
    private String q;

    private List<Long> skills;

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public List<Long> getSkills() {
        return skills;
    }

    public void setSkills(List<Long> skills) {
        this.skills = skills;
    }
}
