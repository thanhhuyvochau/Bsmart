package fpt.project.bsmart.entity;

import javax.persistence.*;

@Entity
@Table(name = "account_detail_class_level")
public class AccountDetailClassLevel extends BaseEntity {

    @EmbeddedId
    private AccountDetailClassLevelKey id = new AccountDetailClassLevelKey();


    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("accountDetailId")
    @JoinColumn(name = "account_detail_id")
    private AccountDetail accountDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("classLevelId")
    @JoinColumn(name = "class_level_id")
    private ClassLevel classLevel;

    public AccountDetailClassLevelKey getId() {
        return id;
    }

    public void setId(AccountDetailClassLevelKey id) {
        this.id = id;
    }

    public AccountDetail getAccountDetail() {
        return accountDetail;
    }

    public void setAccountDetail(AccountDetail accountDetail) {
        this.accountDetail = accountDetail;
    }

    public ClassLevel getClassLevel() {
        return classLevel;
    }

    public void setClassLevel(ClassLevel classLevel) {
        this.classLevel = classLevel;
    }
}
