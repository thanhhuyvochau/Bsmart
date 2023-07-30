package fpt.project.bsmart.entity.dto.mentor;

public class TeachInformationDTO {
    private int numberOfCourse = 0;
    private int numberOfClass = 0;
    private int numberOfMember = 0;

    private Double scoreFeedback = 0.0 ;

    private int numberOfFeedBack = 0;

    public int getNumberOfCourse() {
        return numberOfCourse;
    }

    public void setNumberOfCourse(int numberOfCourse) {
        this.numberOfCourse = numberOfCourse;
    }

    public int getNumberOfClass() {
        return numberOfClass;
    }

    public void setNumberOfClass(int numberOfClass) {
        this.numberOfClass = numberOfClass;
    }

    public int getNumberOfMember() {
        return numberOfMember;
    }

    public void setNumberOfMember(int numberOfMember) {
        this.numberOfMember = numberOfMember;
    }

    public Double getScoreFeedback() {
        return scoreFeedback;
    }

    public void setScoreFeedback(Double scoreFeedback) {
        this.scoreFeedback = scoreFeedback;
    }

    public int getNumberOfFeedBack() {
        return numberOfFeedBack;
    }

    public void setNumberOfFeedBack(int numberOfFeedBack) {
        this.numberOfFeedBack = numberOfFeedBack;
    }
}
