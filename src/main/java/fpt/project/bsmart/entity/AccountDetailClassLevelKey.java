package fpt.project.bsmart.entity;


import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;


@Embeddable
public class AccountDetailClassLevelKey implements Serializable {

    @Column(name = "account_detail_id")
    private Long accountDetailId;

    @Column(name = "class_level_id")
    private Long classLevelId;

    public Long getAccountDetailId() {
        return accountDetailId;
    }

    public void setAccountDetailId(Long accountDetailId) {
        this.accountDetailId = accountDetailId;
    }

    public Long getClassLevelId() {
        return classLevelId;
    }

    public void setClassLevelId(Long classLevelId) {
        this.classLevelId = classLevelId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountDetailClassLevelKey that = (AccountDetailClassLevelKey) o;
        return Objects.equals(accountDetailId, that.accountDetailId) && Objects.equals(classLevelId, that.classLevelId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountDetailId, classLevelId);
    }
}
