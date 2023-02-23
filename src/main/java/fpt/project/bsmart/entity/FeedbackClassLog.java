package fpt.project.bsmart.entity;

import fpt.project.bsmart.entity.common.EFeedbackClassLogStatus;

import javax.persistence.*;


@Entity
@Table(name = "feedback_class_log")
public class FeedbackClassLog extends BaseEntity  {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "feedback_account_log_id_generator")
    @SequenceGenerator(name = "feedback_account_log_id_generator", sequenceName = "feedback_account_log_id_generator")
    private Long id;


    @Column(name = "content")
    private String content;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EFeedbackClassLogStatus status;

    @ManyToOne(fetch = FetchType.LAZY , cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    private Account account;


    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "class_id")
    private Class aClass;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public EFeedbackClassLogStatus getStatus() {
        return status;
    }

    public void setStatus(EFeedbackClassLogStatus status) {
        this.status = status;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Class getaClass() {
        return aClass;
    }

    public void setaClass(Class aClass) {
        this.aClass = aClass;
    }
}
