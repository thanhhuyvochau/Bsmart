package fpt.project.bsmart.entity.request;

import java.io.Serializable;


public class StudentFeedbackRequest implements Serializable {

    private Long teacherId ;
    private String content ;
    private int point ;

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

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }
}
