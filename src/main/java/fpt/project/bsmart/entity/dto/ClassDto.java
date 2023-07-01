package fpt.project.bsmart.entity.dto;

import java.math.BigDecimal;
import java.time.Instant;


public class ClassDto {
    private Long id;
    private CourseDto course;
    private Instant expectedStartDate;
    private Instant startDate;
    private Instant endDate;
    private String code;
    private Integer minStudentNumber = 0;
    private Integer maxStudentNumber = 0;
    private Integer numberOfStudent = 0;
    private BigDecimal price;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
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
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
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



    public CourseDto getCourse() {
        return course;
    }

    public void setCourse(CourseDto course) {
        this.course = course;
    }
}