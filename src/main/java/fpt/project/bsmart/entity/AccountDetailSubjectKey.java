package fpt.project.bsmart.entity;


import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;


@Embeddable
public class AccountDetailSubjectKey implements Serializable {

    @Column(name = "account_detail_id")
    private Long accountDetailId;

    @Column(name = "subject_id")
    private Long subjectId;

    public Long getAccountDetailId() {
        return accountDetailId;
    }

    public void setAccountDetailId(Long accountDetailId) {
        this.accountDetailId = accountDetailId;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountDetailSubjectKey that = (AccountDetailSubjectKey) o;
        return Objects.equals(accountDetailId, that.accountDetailId) && Objects.equals(subjectId, that.subjectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountDetailId, subjectId);
    }
}
