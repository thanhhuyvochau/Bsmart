package fpt.project.bsmart.entity.request;

import fpt.project.bsmart.entity.constant.ECourseClassStatus;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ClassFilterRequest {
    private String q;
    private Instant startDate;
    private Instant endDate;
    @NotNull
    private ECourseClassStatus status;
    // Student = 1 | Teacher = 2
    private int asRole = 0;
    private List<Long> categoryId = new ArrayList<>();
    private List<Long> subjectId = new ArrayList<>();

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public int getAsRole() {
        return asRole;
    }

    public void setAsRole(int asRole) {
        this.asRole = asRole;
    }

    public ECourseClassStatus getStatus() {
        return status;
    }

    public void setStatus(ECourseClassStatus status) {
        this.status = status;
    }

    public List<Long> getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(List<Long> categoryId) {
        this.categoryId = categoryId;
    }

    public List<Long> getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(List<Long> subjectId) {
        this.subjectId = subjectId;
    }
}
