package fpt.project.bsmart.entity.request;

import fpt.project.bsmart.entity.BaseEntity;


public class GradeAssignmentSubmitionRequest extends BaseEntity {
    private Long id;
    private Float point;
    private String note;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getPoint() {
        return point;
    }

    public void setPoint(Float point) {
        this.point = point;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
