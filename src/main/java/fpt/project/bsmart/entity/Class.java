package fpt.project.bsmart.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import fpt.project.bsmart.entity.common.EClassStatus;
import fpt.project.bsmart.entity.common.EClassType;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "class")
public class Class extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EClassStatus status;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @Column(name = "closing_date")
    private Instant closingDate;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teacher_id")
    private Account account;


    @OneToMany(mappedBy = "aClass", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StudentClass> studentClasses = new ArrayList<>();

    @Column(name = "number_student")
    private Long numberStudent = 0L;

    @Column(name = "max_number_student")
    private Long maxNumberStudent = 0L;
    @Column(name = "min_number_student")
    private Long minNumberStudent = 0L;

    @Column(name = "is_avtive")
    private boolean isActive;

    @ManyToOne
    @JoinColumn(name = "class_level_id")
    private ClassLevel classLevel;

    @Column(name = "class_type")
    @Enumerated(EnumType.STRING)
    private EClassType classType;

    @Column(name = "each_student_pay_price")
    private BigDecimal unitPrice;


    @OneToMany(mappedBy = "clazz", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<TimeTable> timeTables = new ArrayList<>();

    @Column(name = "course_mooodle_id")
    private Long moodleClassId;

    @OneToOne(mappedBy = "clazz", cascade = CascadeType.ALL)
    private Forum forum;

    @OneToMany(mappedBy = "clazz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> sections;
    @OneToMany(mappedBy = "clazz", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ClassTeacherCandicate> candicates = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "paymentClass")
    private List<Transaction> transactions = new ArrayList<>();

    @OneToMany(mappedBy = "aClass", cascade = CascadeType.ALL)
    private List<FeedbackClassLog> feedbackClassLogs = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public EClassStatus getStatus() {
        return status;
    }

    public void setStatus(EClassStatus status) {
        this.status = status;
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

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public List<StudentClass> getStudentClasses() {
        return studentClasses;
    }

//    public void setStudentClasses(List<StudentClass> studentClasses) {
//        this.studentClasses = studentClasses;
//    }

//    public List<FeedBack> getFeedBacks() {
//        return feedBacks;
//    }
//
//    public void setFeedBacks(List<FeedBack> feedBacks) {
//        this.feedBacks = feedBacks;
//    }

    public Long getNumberStudent() {
        return numberStudent;
    }

    public void setNumberStudent(Long numberStudent) {
        this.numberStudent = numberStudent;
    }

    public Long getMaxNumberStudent() {
        return maxNumberStudent;
    }

    public void setMaxNumberStudent(Long maxNumberStudent) {
        this.maxNumberStudent = maxNumberStudent;
    }

    public Long getMinNumberStudent() {
        return minNumberStudent;
    }

    public void setMinNumberStudent(Long minNumberStudent) {
        this.minNumberStudent = minNumberStudent;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public ClassLevel getClassLevel() {
        return classLevel;
    }

    public EClassType getClassType() {
        return classType;
    }

    public void setClassType(EClassType classType) {
        this.classType = classType;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal eachStudentPayPrice) {
        this.unitPrice = eachStudentPayPrice;
    }

    public List<TimeTable> getTimeTables() {
        return timeTables;
    }

    public void setTimeTables(List<TimeTable> timeTables) {
        this.timeTables = timeTables;
    }

    public Long getMoodleClassId() {
        return moodleClassId;
    }

    public void setMoodleClassId(Long resourceMoodleId) {
        this.moodleClassId = resourceMoodleId;
    }

    public Forum getForum() {
        return forum;
    }

    public void setForum(Forum forums) {
        this.forum = forums;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

    public List<ClassTeacherCandicate> getCandicates() {
        return candicates;
    }

    public void setCandicates(List<ClassTeacherCandicate> candicates) {
        this.candicates = candicates;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void setClassLevel(ClassLevel classLevel) {
        this.classLevel = classLevel;
    }

    public Instant getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(Instant closingDate) {
        this.closingDate = closingDate;
    }

    public void setStudentClasses(List<StudentClass> studentClasses) {
        this.studentClasses = studentClasses;
    }

    public List<FeedbackClassLog> getFeedbackClassLogs() {
        return feedbackClassLogs;
    }

    public void setFeedbackClassLogs(List<FeedbackClassLog> feedbackClassLogs) {
        this.feedbackClassLogs = feedbackClassLogs;
    }
}
