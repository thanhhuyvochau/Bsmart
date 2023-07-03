package fpt.project.bsmart.entity;

import fpt.project.bsmart.entity.constant.ECourseLevel;
import fpt.project.bsmart.entity.constant.ECourseStatus;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "class", indexes = @Index(name = "idx_class_code", columnList = "code", unique = true))
public class Class extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code")
    private String code;
    @Column(name = "start_date")
    private Instant startDate;
    @Column(name = "end_date")
    private Instant endDate;
    @Column(name = "student_number")
    private Integer numberOfStudent = 0;
    @Column(name = "level")
    @Enumerated(EnumType.STRING)
    private ECourseLevel level;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ECourseStatus status;


    @Column(name = "price")
    private BigDecimal price;


    @Column(name = "min_student")
    private Integer minStudent;

    @Column(name = "max_student")
    private Integer maxStudent;

    @Column(name = "number_of_slot")
    private Integer numberOfSlot = 0;


    @Column(name = "has_referral_code")
    private boolean hasReferralCode;

    @Column(name = "number_referral_code")
    private Integer numberReferralCode;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @OneToOne
    @JoinColumn(name = "feedback_template_id")
    private FeedbackTemplate feedbackTemplate;
    @OneToOne(mappedBy = "aClass")
    private ClassImage classImage;

    @OneToMany(mappedBy = "clazz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TimeInWeek> timeInWeeks = new ArrayList<>();

    @OneToMany(mappedBy = "clazz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "mentor_id")
    private User mentor;

    @OneToMany(mappedBy = "clazz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TimeTable> timeTables = new ArrayList<>();
    @OneToMany(mappedBy = "clazz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentClass> studentClasses = new ArrayList<>();

    @OneToMany(mappedBy = "announcementClass", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClassAnnouncement> classAnnouncements = new ArrayList<>();
    @OneToMany(mappedBy = "authorizeClass", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ActivityAuthorize> activityAuthorizes = new ArrayList<>();

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

    public List<TimeTable> getTimeTables() {
        return timeTables;
    }

    public void setTimeTables(List<TimeTable> timeTables) {
        this.timeTables = timeTables;
    }

    public List<StudentClass> getStudentClasses() {
        return studentClasses;
    }

    public void setStudentClasses(List<StudentClass> studentClasses) {
        this.studentClasses = studentClasses;
    }

    public List<ClassAnnouncement> getClassAnnouncements() {
        return classAnnouncements;
    }

    public void setClassAnnouncements(List<ClassAnnouncement> classAnnouncements) {
        this.classAnnouncements = classAnnouncements;
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

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public FeedbackTemplate getFeedbackTemplate() {
        return feedbackTemplate;
    }

    public void setFeedbackTemplate(FeedbackTemplate feedbackTemplate) {
        this.feedbackTemplate = feedbackTemplate;
    }

    public ClassImage getClassImage() {
        return classImage;
    }

    public void setClassImage(ClassImage classImage) {
        this.classImage = classImage;
    }

    public List<TimeInWeek> getTimeInWeeks() {
        return timeInWeeks;
    }

    public void setTimeInWeeks(List<TimeInWeek> timeInWeeks) {
        this.timeInWeeks = timeInWeeks;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public User getMentor() {
        return mentor;
    }

    public void setMentor(User mentor) {
        this.mentor = mentor;
    }

    public List<ActivityAuthorize> getActivityAuthorizes() {
        return activityAuthorizes;
    }

    public void setActivityAuthorizes(List<ActivityAuthorize> activityAuthorizes) {
        this.activityAuthorizes = activityAuthorizes;
    }
}