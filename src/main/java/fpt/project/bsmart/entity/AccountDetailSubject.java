package fpt.project.bsmart.entity;

import javax.persistence.*;

@Entity
@Table(name = "account_detail_subject")
public class AccountDetailSubject extends BaseEntity {

    @EmbeddedId
    private AccountDetailSubjectKey id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("accountDetailId")
    @JoinColumn(name = "account_detail_id")
    private AccountDetail accountDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("subjectId")
    @JoinColumn(name = "subject_id")
    private Subject subject;

    public AccountDetailSubjectKey getId() {
        return id;
    }

    public void setId(AccountDetailSubjectKey id) {
        this.id = id;
    }

    public AccountDetail getAccountDetail() {
        return accountDetail;
    }

    public void setAccountDetail(AccountDetail accountDetail) {
        this.accountDetail = accountDetail;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }
}
