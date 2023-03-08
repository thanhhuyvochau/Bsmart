package fpt.project.bsmart.entity.request;



import fpt.project.bsmart.entity.constant.ETypeLearn;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;


public class CreateClassRequest implements Serializable {


    private String code;

    private Instant expectedStartDate;

    private Instant startDate;

    private Instant endDate;

    private Integer minStudentNumber = 0;

    private Integer maxStudentNumber = 0;

    private Integer numberOfStudent = 0;

    private BigDecimal price;

    private ETypeLearn typeLearn;

    private Long courseId;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Instant getExpectedStartDate() {
        return expectedStartDate;
    }

    public void setExpectedStartDate(Instant expectedStartDate) {
        this.expectedStartDate = expectedStartDate;
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

    public Integer getMinStudentNumber() {
        return minStudentNumber;
    }

    public void setMinStudentNumber(Integer minStudentNumber) {
        this.minStudentNumber = minStudentNumber;
    }

    public Integer getMaxStudentNumber() {
        return maxStudentNumber;
    }

    public void setMaxStudentNumber(Integer maxStudentNumber) {
        this.maxStudentNumber = maxStudentNumber;
    }

    public Integer getNumberOfStudent() {
        return numberOfStudent;
    }

    public void setNumberOfStudent(Integer numberOfStudent) {
        this.numberOfStudent = numberOfStudent;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public ETypeLearn getTypeLearn() {
        return typeLearn;
    }

    public void setTypeLearn(ETypeLearn typeLearn) {
        this.typeLearn = typeLearn;
    }


    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
}
