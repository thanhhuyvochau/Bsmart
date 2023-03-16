package fpt.project.bsmart.entity.request;

import fpt.project.bsmart.entity.constant.ETypeLearn;

import java.io.Serializable;
import java.util.List;

public class CourseSearchRequest implements Serializable {


    private String q;

    private List<Long>  categoryId ;
    private ETypeLearn type ;


    private List<Long> subjectId ;

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public List<Long> getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(List<Long> categoryId) {
        this.categoryId = categoryId;
    }

    public List<Long> getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(List<Long> subjectId) {
        this.subjectId = subjectId;
    }

    public ETypeLearn getType() {
        return type;
    }

    public void setType(ETypeLearn type) {
        this.type = type;
    }
}
