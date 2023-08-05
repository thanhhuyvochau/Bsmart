package fpt.project.bsmart.entity.response.member;

public class StudyInformationDTO {
    private int numberOfCourse = 0;
    private int numberOfClass = 0;

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
}
