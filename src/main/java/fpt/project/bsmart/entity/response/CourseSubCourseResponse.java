package fpt.project.bsmart.entity.response;


import fpt.project.bsmart.entity.constant.ECourseLevel;
import fpt.project.bsmart.entity.constant.ECourseStatus;
import fpt.project.bsmart.entity.constant.ECourseType;
import fpt.project.bsmart.entity.dto.CategoryDto;
import fpt.project.bsmart.entity.dto.SubjectDto;
import fpt.project.bsmart.entity.dto.TimeInWeekDTO;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class CourseSubCourseResponse {

    private Long subCourseId;

    private Long courseId;
    private String imageUrl;

    private String courseName;
    private String courseCode;

    private String subCourseTitle;

    private ECourseStatus status;

    private BigDecimal price;

    private Instant startDateExpected;

    private Instant endDateExpected;

    private int minStudent;

    private int maxStudent;

    private int finalStudent;

    private String courseDescription;


    private List<CategoryDto> categoryDtoList = new ArrayList<>();

    private SubjectDto subject;


    private ECourseLevel level;

    private ECourseType courseType ;

    private String mentorName;



    private Integer numberOfSlot;
    private List<TimeInWeekDTO> timeInWeek;




    public Long getSubCourseId() {
        return subCourseId;
    }

    public void setSubCourseId(Long subCourseId) {
        this.subCourseId = subCourseId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public ECourseType getCourseType() {
        return courseType;
    }

    public void setCourseType(ECourseType courseType) {
        this.courseType = courseType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<CategoryDto> getCategoryDtoList() {
        return categoryDtoList;
    }

    public void setCategoryDtoList(List<CategoryDto> categoryDtoList) {
        this.categoryDtoList = categoryDtoList;
    }

    public SubjectDto getSubject() {
        return subject;
    }

    public void setSubject(SubjectDto subject) {
        this.subject = subject;
    }

    public String getSubCourseTitle() {
        return subCourseTitle;
    }

    public void setSubCourseTitle(String subCourseTitle) {
        this.subCourseTitle = subCourseTitle;
    }

    public ECourseLevel getLevel() {
        return level;
    }

    public void setLevel(ECourseLevel level) {
        this.level = level;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getMentorName() {
        return mentorName;
    }

    public void setMentorName(String mentorName) {
        this.mentorName = mentorName;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }



    public List<TimeInWeekDTO> getTimeInWeek() {
        return timeInWeek;
    }

    public void setTimeInWeek(List<TimeInWeekDTO> timeInWeek) {
        this.timeInWeek = timeInWeek;
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

    public int getFinalStudent() {
        return finalStudent;
    }

    public void setFinalStudent(int finalStudent) {
        this.finalStudent = finalStudent;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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

    public Integer getNumberOfSlot() {
        return numberOfSlot;
    }

    public void setNumberOfSlot(Integer numberOfSlot) {
        this.numberOfSlot = numberOfSlot;
    }
}
