package fpt.project.bsmart.entity.request;

import fpt.project.bsmart.entity.constant.ECourseLevel;
import fpt.project.bsmart.entity.constant.ETypeLearn;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class UpdateSubCourseRequest {

    private String courseCode;
    private String courseName;
    private Long categoryId;
    private Long subjectId;
    private String courseDescription;

    private ECourseLevel level;

    private Long imageId;

    private ETypeLearn type;

    private BigDecimal price;

    private Integer minStudent ;
    private Integer maxStudent ;
    private Instant startDateExpected  ;
    private Instant endDateExpected  ;
    private String subCourseTile ;
    private Integer  numberOfSlot ;

    private List<TimeInWeekRequest> timeInWeekRequests = new ArrayList<>();

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

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

    public ETypeLearn getType() {
        return type;
    }

    public void setType(ETypeLearn type) {
        this.type = type;
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

    public String getSubCourseTile() {
        return subCourseTile;
    }

    public void setSubCourseTile(String subCourseTile) {
        this.subCourseTile = subCourseTile;
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
