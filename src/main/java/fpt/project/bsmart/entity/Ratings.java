package fpt.project.bsmart.entity;

import javax.persistence.*;


@Entity
@Table(name = "rating")
public class Ratings extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "userId")
    private Long  userId ;


    @Column(name = "subject_name")
    private Long subjectName ;

    @Column(name = "class_level")
    private Long classLevel ;

    @Column(name = "learn_ability")
    private String learningAbility ;


    @Column(name = "free_time_day")
    private String freeTimeInDay ;

    @Column(name = "number_free_time_week")
    private int freeTimeInWeek  ;

    @Column(name = "rating")
    private Long  rating;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(Long subjectName) {
        this.subjectName = subjectName;
    }

    public Long getClassLevel() {
        return classLevel;
    }

    public void setClassLevel(Long classLevel) {
        this.classLevel = classLevel;
    }

    public String getLearningAbility() {
        return learningAbility;
    }

    public void setLearningAbility(String learningAbility) {
        this.learningAbility = learningAbility;
    }

    public String getFreeTimeInDay() {
        return freeTimeInDay;
    }

    public void setFreeTimeInDay(String freeTimeInDay) {
        this.freeTimeInDay = freeTimeInDay;
    }

    public int getFreeTimeInWeek() {
        return freeTimeInWeek;
    }

    public void setFreeTimeInWeek(int freeTimeInWeek) {
        this.freeTimeInWeek = freeTimeInWeek;
    }

    public Long getRating() {
        return rating;
    }

    public void setRating(Long rating) {
        this.rating = rating;
    }
}
