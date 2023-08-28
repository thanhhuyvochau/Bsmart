package fpt.project.bsmart.entity.request.clazz;

import fpt.project.bsmart.entity.request.TimeInWeekRequest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class MentorCreateClass {


    private Long imageId;

    private BigDecimal price;

    private Integer minStudent;

    private Integer maxStudent;

    private Instant startDate;

    private Instant endDate;

    private Integer numberOfSlot;

    private List<TimeInWeekRequest> timeInWeekRequests = new ArrayList<>();

//    private List<MentorCreateScheduleRequest> timeTableRequest;

    private String link;

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getMinStudent() {
        return minStudent;
    }

    public void setMinStudent(Integer minStudent) {
        this.minStudent = minStudent;
    }

    public Integer getMaxStudent() {
        return maxStudent;
    }

    public void setMaxStudent(Integer maxStudent) {
        this.maxStudent = maxStudent;
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

    public Integer getNumberOfSlot() {
        return numberOfSlot;
    }

    public void setNumberOfSlot(Integer numberOfSlot) {
        this.numberOfSlot = numberOfSlot;
    }

    public List<TimeInWeekRequest> getTimeInWeekRequests() {
        return timeInWeekRequests;
    }

    public void setTimeInWeekRequests(List<TimeInWeekRequest> timeInWeekRequests) {
        this.timeInWeekRequests = timeInWeekRequests;
    }

//    public List<MentorCreateScheduleRequest> getTimeTableRequest() {
//        return timeTableRequest;
//    }
//
//    public void setTimeTableRequest(List<MentorCreateScheduleRequest> timeTableRequest) {
//        this.timeTableRequest = timeTableRequest;
//    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
