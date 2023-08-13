package fpt.project.bsmart.entity.request;

public class FeedbackSubmissionSearchRequest {
    private Long id;
    private Boolean isCourse = true;
    private Integer rate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getIsCourse() {
        return isCourse;
    }

    public void setIsCourse(Boolean course) {
        isCourse = course;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }
}
