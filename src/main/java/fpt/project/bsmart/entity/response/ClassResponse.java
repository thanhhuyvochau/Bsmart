package fpt.project.bsmart.entity.response;

import com.fasterxml.jackson.annotation.JsonView;
import fpt.project.bsmart.config.json.View;
import fpt.project.bsmart.entity.constant.ECourseClassStatus;
import fpt.project.bsmart.entity.dto.*;
import fpt.project.bsmart.entity.dto.feedback.FeedbackTemplateDto;
import fpt.project.bsmart.entity.dto.mentor.MentorDto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ClassResponse {
    private Long id;
    private String code;
    private Instant startDate;
    private Instant endDate;
    private ECourseClassStatus status;
    private BigDecimal price;
    @JsonView(View.Teacher.class)
    private Integer minStudent;
    @JsonView(View.Teacher.class)
    private Integer maxStudent;
    private Integer numberOfSlot = 0;
    @JsonView(View.Teacher.class)
    private boolean hasReferralCode;
    @JsonView(View.Teacher.class)
    private Integer numberReferralCode;
    private ImageDto classImage;
    private MentorDto mentor;
    private List<ActivityDto> activities = new ArrayList<>();
    private List<TimeInWeekDTO> timeInWeeks = new ArrayList<>();
    private CourseDto course;
    private int numberOfCurrentStudent;
    private ClassProgressTimeDto progress;
    private List<UserDto> studentClass = new ArrayList<>();

    private ImageDto image;

    private FeedbackTemplateDto feedback;


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

    public ECourseClassStatus getStatus() {
        return status;
    }

    public void setStatus(ECourseClassStatus status) {
        this.status = status;
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

    public Integer getNumberOfSlot() {
        return numberOfSlot;
    }

    public void setNumberOfSlot(Integer numberOfSlot) {
        this.numberOfSlot = numberOfSlot;
    }

    public boolean isHasReferralCode() {
        return hasReferralCode;
    }

    public void setHasReferralCode(boolean hasReferralCode) {
        this.hasReferralCode = hasReferralCode;
    }

    public Integer getNumberReferralCode() {
        return numberReferralCode;
    }

    public void setNumberReferralCode(Integer numberReferralCode) {
        this.numberReferralCode = numberReferralCode;
    }

    public ImageDto getClassImage() {
        return classImage;
    }

    public void setClassImage(ImageDto classImage) {
        this.classImage = classImage;
    }

    public MentorDto getMentor() {
        return mentor;
    }

    public void setMentor(MentorDto mentor) {
        this.mentor = mentor;
    }

    public List<ActivityDto> getActivities() {
        return activities;
    }

    public void setActivities(List<ActivityDto> activities) {
        this.activities = activities;
    }

    public List<TimeInWeekDTO> getTimeInWeeks() {
        return timeInWeeks;
    }

    public void setTimeInWeeks(List<TimeInWeekDTO> timeInWeeks) {
        this.timeInWeeks = timeInWeeks;
    }

    public CourseDto getCourse() {
        return course;
    }

    public void setCourse(CourseDto course) {
        this.course = course;
    }

    public int getNumberOfCurrentStudent() {
        return numberOfCurrentStudent;
    }

    public void setNumberOfCurrentStudent(int numberOfCurrentStudent) {
        this.numberOfCurrentStudent = numberOfCurrentStudent;
    }

    public ClassProgressTimeDto getProgress() {
        return progress;
    }

    public void setProgress(ClassProgressTimeDto progress) {
        this.progress = progress;
    }

    public ImageDto getImage() {
        return image;
    }

    public void setImage(ImageDto image) {
        this.image = image;
    }

    public FeedbackTemplateDto getFeedback() {
        return feedback;
    }

    public void setFeedback(FeedbackTemplateDto feedback) {
        this.feedback = feedback;
    }

    public List<UserDto> getStudentClass() {
        return studentClass;
    }

    public void setStudentClass(List<UserDto> studentClass) {
        this.studentClass = studentClass;
    }
}
