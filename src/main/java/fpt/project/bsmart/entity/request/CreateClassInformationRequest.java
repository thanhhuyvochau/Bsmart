package fpt.project.bsmart.entity.request;

import fpt.project.bsmart.entity.constant.ECourseLevel;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class CreateClassInformationRequest {

    private ECourseLevel level;

    private Long imageId;

    private BigDecimal price;

    private Integer minStudent ;

    private Integer maxStudent ;

    private Instant startDateExpected  ;

    private Instant endDateExpected  ;

    private Integer  numberOfSlot ;

    private List<TimeInWeekRequest> timeInWeekRequests = new ArrayList<>();

    public ECourseLevel getLevel() {
        return level;
    }

    public void setLevel(ECourseLevel level) {
        this.level = level;
    }

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

    public Instant getStartDateExpected() {
        return startDateExpected;
    }

    public void setStartDateExpected(Instant startDateExpected) {
        this.startDateExpected = startDateExpected;
    }

    public Instant getEndDateExpected() {
        return endDateExpected;
    }

    public void setEndDateExpected(Instant endDateExpected) {
        this.endDateExpected = endDateExpected;
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
}
