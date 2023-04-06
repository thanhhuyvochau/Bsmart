package fpt.project.bsmart.entity.request;

import java.io.Serializable;

public class MentorSearchRequest implements Serializable {
    private String q;

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }
}
