package fpt.project.bsmart.entity;

import javax.persistence.*;

@Entity
@Table(name = "info_find_tutor_account")
public class InfoFindTutorAccount extends BaseEntity {

    @EmbeddedId
    private InfoFindTutorAccountKey id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("infoFindTutorId")
    @JoinColumn(name = "info_find_tutorl_id")
    private InfoFindTutor infoFindTutor;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("teacherId")
    @JoinColumn(name = "teacher_id")
    private Account teacher;

    public InfoFindTutorAccountKey getId() {
        return id;
    }

    public void setId(InfoFindTutorAccountKey id) {
        this.id = id;
    }




    public Account getTeacher() {
        return teacher;
    }

    public void setTeacher(Account teacher) {
        this.teacher = teacher;
    }

    public InfoFindTutor getInfoFindTutor() {
        return infoFindTutor;
    }

    public void setInfoFindTutor(InfoFindTutor infoFindTutor) {
        this.infoFindTutor = infoFindTutor;
    }
}
