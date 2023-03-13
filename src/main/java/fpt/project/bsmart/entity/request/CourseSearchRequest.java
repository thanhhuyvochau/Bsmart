package fpt.project.bsmart.entity.request;

import java.io.Serializable;

public class CourseSearchRequest implements Serializable {


    private String q;

    private Long categoryId ;

    private Long subjectId ;

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }
}
