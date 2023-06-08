package fpt.project.bsmart.entity;

import fpt.project.bsmart.entity.constant.EDayOfWeekCode;

import javax.persistence.*;

@Entity
@Table(name = "day_of_week")
public class DayOfWeek extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")

    private String name;
    @Column(name = "code")
    @Enumerated(EnumType.STRING)
    private EDayOfWeekCode code;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EDayOfWeekCode getCode() {
        return code;
    }

    public void setCode(EDayOfWeekCode code) {
        this.code = code;
    }

}
