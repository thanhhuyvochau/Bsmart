package fpt.project.bsmart.entity;


import fpt.project.bsmart.entity.constant.ECourseLevel;
import fpt.project.bsmart.entity.constant.ECourseStatus;
import fpt.project.bsmart.entity.constant.ETypeLearn;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sub_course")
public class SubCourse  extends  BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title")
    private String title ;
    @Column(name = "level")
    @Enumerated(EnumType.STRING)
    private ECourseLevel level;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ECourseStatus status;

    @Column(name = "start_date_expected")
    private Instant startDateExpected;

    @Column(name = "end_date_expected")
    private Instant endDateExpected;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "learn_type")
    @Enumerated(EnumType.STRING)
    private ETypeLearn typeLearn;

    @Column(name = "min_student")
    private Integer minStudent;

    @Column(name = "max_student")
    private Integer maxStudent;

    @Column(name = "reference_discount")
    private Double referenceDiscount = 0.0;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Image image;
    @OneToMany(mappedBy = "subCourse", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Class> classes = new ArrayList<>();

    @OneToMany(mappedBy = "subCourse", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TimeInWeek> timeInWeeks = new ArrayList<>();

    @OneToMany(mappedBy = "subCourse", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "mentor_id")
    private User mentor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ECourseStatus getStatus() {
        return status;
    }

    public void setStatus(ECourseStatus status) {
        this.status = status;
    }

    public ECourseLevel getLevel() {
        return level;
    }

    public void setLevel(ECourseLevel level) {
        this.level = level;
    }

    public Instant getStartDateExpected() {
        return startDateExpected;
    }

    public void setStartDateExpected(Instant startDateExpected) {
        this.startDateExpected = startDateExpected;
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

    public ETypeLearn getTypeLearn() {
        return typeLearn;
    }

    public void setTypeLearn(ETypeLearn typeLearn) {
        this.typeLearn = typeLearn;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Instant getEndDateExpected() {
        return endDateExpected;
    }

    public void setEndDateExpected(Instant endDateExpected) {
        this.endDateExpected = endDateExpected;
    }

    public Double getReferenceDiscount() {
        return referenceDiscount;
    }

    public void setReferenceDiscount(Double referenceDiscount) {
        this.referenceDiscount = referenceDiscount;
    }

    public List<Class> getClasses() {
        return classes;
    }

    public void setClasses(List<Class> classes) {
        this.classes = classes;
    }

    public List<TimeInWeek> getTimeInWeeks() {
        return timeInWeeks;
    }

    public void setTimeInWeeks(List<TimeInWeek> timeInWeeks) {
        this.timeInWeeks = timeInWeeks;
    }

    public void addTimeInWeek(TimeInWeek timeInWeek) {
        this.timeInWeeks.add(timeInWeek);
        timeInWeek.setSubCourse(this);
    }

    public void removeTimeInWeek(TimeInWeek timeInWeek) {
        this.timeInWeeks.remove(timeInWeek);
        timeInWeek.setSubCourse(null);
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public User getMentor() {
        return mentor;
    }

    public void setMentor(User mentor) {
        this.mentor = mentor;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }
}
