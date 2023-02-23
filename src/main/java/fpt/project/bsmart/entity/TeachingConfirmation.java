package fpt.project.bsmart.entity;

import fpt.project.bsmart.entity.common.EConfirmStatus;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "teaching_confirmation")
public class TeachingConfirmation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "class_candicate_id")
    private ClassTeacherCandicate candidate;

    @Column(name = "expirte_date")
    private Instant expireDate;
    @Column(name = "code")
    private String code;
    @Column(name = "is_accept")
    private Boolean isAccept;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EConfirmStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ClassTeacherCandicate getCandidate() {
        return candidate;
    }

    public void setCandidate(ClassTeacherCandicate candidate) {
        this.candidate = candidate;
    }

    public Instant getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Instant expireDate) {
        this.expireDate = expireDate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getIsAccept() {
        return isAccept;
    }

    public void setIsAccept(Boolean accept) {
        isAccept = accept;
    }

    public EConfirmStatus getStatus() {
        return status;
    }

    public void setStatus(EConfirmStatus status) {
        this.status = status;
    }
}
