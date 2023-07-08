package fpt.project.bsmart.entity.response.Class;


import fpt.project.bsmart.entity.constant.ECourseLevel;
import fpt.project.bsmart.entity.constant.ECourseStatus;
import fpt.project.bsmart.entity.dto.ImageDto;
import fpt.project.bsmart.entity.dto.TimeInWeekDTO;
import fpt.project.bsmart.entity.dto.activity.SectionDto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;


public class MentorGetClassDetailResponse {

    private Long id;

    private String code;

    private Instant startDate;

    private Instant endDate;

    private Integer numberOfStudent = 0;

    private ECourseLevel level;

    private ECourseStatus status;

    private BigDecimal price;

    private int minStudent;

    private int maxStudent;

    private ImageDto image;
    private List<TimeInWeekDTO> timeInWeeks;



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

    public ECourseLevel getLevel() {
        return level;
    }

    public void setLevel(ECourseLevel level) {
        this.level = level;
    }

    public ECourseStatus getStatus() {
        return status;
    }

    public void setStatus(ECourseStatus status) {
        this.status = status;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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

    public ImageDto getImage() {
        return image;
    }

    public void setImage(ImageDto image) {
        this.image = image;
    }

    public List<TimeInWeekDTO> getTimeInWeeks() {
        return timeInWeeks;
    }

    public void setTimeInWeeks(List<TimeInWeekDTO> timeInWeeks) {
        this.timeInWeeks = timeInWeeks;
    }


}
