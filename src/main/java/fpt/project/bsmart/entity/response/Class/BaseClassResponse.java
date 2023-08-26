package fpt.project.bsmart.entity.response.Class;

import fpt.project.bsmart.entity.constant.ECourseClassStatus;

import java.time.Instant;

public class BaseClassResponse {
    private Long id;

    private String code;

    private Instant startDate;

    private Instant endDate;

    private Integer numberOfStudent ;

    private Integer numberOfSlot ;


    private ECourseClassStatus status;

    private int minStudent;

    private int maxStudent;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public Integer getNumberOfStudent() {
        return numberOfStudent;
    }

    public void setNumberOfStudent(Integer numberOfStudent) {
        this.numberOfStudent = numberOfStudent;
    }

    public Integer getNumberOfSlot() {
        return numberOfSlot;
    }

    public void setNumberOfSlot(Integer numberOfSlot) {
        this.numberOfSlot = numberOfSlot;
    }

    public ECourseClassStatus getStatus() {
        return status;
    }

    public void setStatus(ECourseClassStatus status) {
        this.status = status;
    }

    public int getMinStudent() {
        return minStudent;
    }

    public void setMinStudent(int minStudent) {
        this.minStudent = minStudent;
    }

    public int getMaxStudent() {
        return maxStudent;
    }

    public void setMaxStudent(int maxStudent) {
        this.maxStudent = maxStudent;
    }


}
