package fpt.project.bsmart.entity;

import javax.persistence.*;


@Entity
@Table(name = "feed_back")
public class FeedBack  extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long  id;



    private StudentClassKey studentClassKeyId = new StudentClassKey();

    @Column(name = "teacher_id")
    private Long teacherId;

    @Column(name = "content")
    private String content ;

    @Column(name = "point")
    private int  pointEvaluation ;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StudentClassKey getStudentClassKeyId() {
        return studentClassKeyId;
    }

    public void setStudentClassKeyId(StudentClassKey studentClassKeyId) {
        this.studentClassKeyId = studentClassKeyId;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPointEvaluation() {
        return pointEvaluation;
    }

    public void setPointEvaluation(int pointEvaluation) {
        this.pointEvaluation = pointEvaluation;
    }
}
