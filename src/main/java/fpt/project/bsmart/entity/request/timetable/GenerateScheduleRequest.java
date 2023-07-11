package fpt.project.bsmart.entity.request.timetable;

import fpt.project.bsmart.entity.request.TimeInWeekRequest;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class GenerateScheduleRequest {
    private Instant startDate ;

    private Instant endDate ;

    private Integer  numberOfSlot ;

    private List<TimeInWeekRequest> timeInWeekRequests = new ArrayList<>();

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
}
