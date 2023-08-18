package fpt.project.bsmart.entity.response;

public class MentorRevenueResponse extends UserRevenueResponse{
    private Integer numOfClass = 0;
    private Integer numOfStudent = 0;

    public Integer getNumOfClass() {
        return numOfClass;
    }

    public void setNumOfClass(Integer numOfClass) {
        this.numOfClass = numOfClass;
    }

    public Integer getNumOfStudent() {
        return numOfStudent;
    }

    public void setNumOfStudent(Integer numOfStudent) {
        this.numOfStudent = numOfStudent;
    }
}
