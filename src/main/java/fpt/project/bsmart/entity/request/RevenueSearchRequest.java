package fpt.project.bsmart.entity.request;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

public class RevenueSearchRequest implements Serializable {

    private List<Long> classIds ;

    private List<Long> teacherIds ;

    private Instant dateFrom;
    private Instant dateTo;

    public List<Long> getClassIds() {
        return classIds;
    }

    public void setClassIds(List<Long> classIds) {
        this.classIds = classIds;
    }

    public List<Long> getTeacherIds() {
        return teacherIds;
    }

    public void setTeacherIds(List<Long> teacherIds) {
        this.teacherIds = teacherIds;
    }

    public Instant getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Instant dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Instant getDateTo() {
        return dateTo;
    }

    public void setDateTo(Instant dateTo) {
        this.dateTo = dateTo;
    }
}
