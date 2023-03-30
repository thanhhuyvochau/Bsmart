package fpt.project.bsmart.entity.response;


import fpt.project.bsmart.entity.constant.ECourseStatus;
import fpt.project.bsmart.entity.constant.ETypeLearn;
import fpt.project.bsmart.entity.dto.ImageDto;
import fpt.project.bsmart.entity.dto.TimeInWeekDTO;

import java.math.BigDecimal;
import java.time.Instant;
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

    private Instant startDateExpected ;

    private Instant endDateExpected ;

    private int minStudent ;

    private  int maxStudent ;

    private int finalStudent ;

    private String courseDescription;

    private String categoryName;

    private String subjectName;


    private String mentorName;

    private ETypeLearn typeLearn;


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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubCourseTitle() {
        return subCourseTitle;
    }

    public void setSubCourseTitle(String subCourseTitle) {
        this.subCourseTitle = subCourseTitle;
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

    public ETypeLearn getTypeLearn() {
        return typeLearn;
    }

    public void setTypeLearn(ETypeLearn typeLearn) {
        this.typeLearn = typeLearn;
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
}
