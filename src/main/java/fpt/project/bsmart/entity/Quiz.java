package fpt.project.bsmart.entity;

import fpt.project.bsmart.entity.constant.QuizStatus;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "quiz")
public class Quiz extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "code")
    private String code;
    @Column(name = "start_date")
    private Instant startDate;
    @Column(name = "end_date")
    private Instant endDate;
    @Column(name = "time")
    private Integer time;
    @Column(name = "status")
    private QuizStatus status;
    @Column(name = "default_point")
    private Float defaultPoint;
    @Column(name = "is_suffle_question")
    private Boolean isSuffleQuestion = false;
    @Column(name = "is_allow_review")
    private Boolean isAllowReview = true;
    @Column(name = "allow_review_after_min")
    private Integer allowReviewAfterMin = 0;
    @Column(name = "password")
    private String password;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "activity_id")
    private Activity activity;
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuizQuestion> quizQuestions = new ArrayList<>();
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuizSubmittion> quizSubmittions = new ArrayList<>();
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

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public QuizStatus getStatus() {
        return status;
    }

    public void setStatus(QuizStatus status) {
        this.status = status;
    }

    public Float getDefaultPoint() {
        return defaultPoint;
    }

    public void setDefaultPoint(Float defaultPoint) {
        this.defaultPoint = defaultPoint;
    }

    public Boolean getIsSuffleQuestion() {
        return isSuffleQuestion;
    }

    public void setIsSuffleQuestion(Boolean suffleQuestion) {
        isSuffleQuestion = suffleQuestion;
    }

    public Boolean getIsAllowReview() {
        return isAllowReview;
    }

    public void setIsAllowReview(Boolean allowReview) {
        isAllowReview = allowReview;
    }

    public Integer getAllowReviewAfterMin() {
        return allowReviewAfterMin;
    }

    public void setAllowReviewAfterMin(Integer allowReviewAfterMin) {
        this.allowReviewAfterMin = allowReviewAfterMin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public List<QuizQuestion> getQuizQuestions() {
        return quizQuestions;
    }

    public void setQuizQuestions(List<QuizQuestion> quizQuestions) {
        this.quizQuestions = quizQuestions;
    }

    public List<QuizSubmittion> getQuizSubmittions() {
        return quizSubmittions;
    }

    public void setQuizSubmittions(List<QuizSubmittion> quizSubmittions) {
        this.quizSubmittions = quizSubmittions;
    }
}
