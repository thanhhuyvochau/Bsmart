package fpt.project.bsmart.entity.dto.mentor;

public class LearningInformationDTO {
    private long numberOfCourse = 0;
    private long numberOfClass = 0;
    private long numberOfFinishedClass = 0;

    public LearningInformationDTO(long numberOfCourse, long numberOfClass, long numberOfFinishedClass) {
        this.numberOfCourse = numberOfCourse;
        this.numberOfClass = numberOfClass;
        this.numberOfFinishedClass = numberOfFinishedClass;
    }

    public long getNumberOfCourse() {
        return numberOfCourse;
    }

    public void setNumberOfCourse(long numberOfCourse) {
        this.numberOfCourse = numberOfCourse;
    }

    public long getNumberOfClass() {
        return numberOfClass;
    }

    public void setNumberOfClass(long numberOfClass) {
        this.numberOfClass = numberOfClass;
    }

    public long getNumberOfFinishedClass() {
        return numberOfFinishedClass;
    }

    public void setNumberOfFinishedClass(long numberOfFinishedClass) {
        this.numberOfFinishedClass = numberOfFinishedClass;
    }
}
