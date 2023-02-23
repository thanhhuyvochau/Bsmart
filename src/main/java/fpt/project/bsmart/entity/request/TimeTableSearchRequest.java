package fpt.project.bsmart.entity.request;



import java.io.Serializable;
import java.time.Instant;

public class TimeTableSearchRequest implements Serializable {

    private String q;

    private Long classId ;

//    private Long accountId ;

    private Instant dateTo;

    private Instant dateFrom;


    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }


//    public Long getAccountId() {
//        return accountId;
//    }
//
//    public void setAccountId(Long accountId) {
//        this.accountId = accountId;
//    }

    public Instant getDateTo() {
        return dateTo;
    }

    public void setDateTo(Instant dateTo) {
        this.dateTo = dateTo;
    }

    public Instant getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Instant dateFrom) {
        this.dateFrom = dateFrom;
    }
}
