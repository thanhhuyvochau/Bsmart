package fpt.project.bsmart.entity;


import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;


@Embeddable
public class InfoFindTutorAccountKey implements Serializable {

    @Column(name = "info_find_tutorl_id")
    private Long infoFindTutorId;

    @Column(name = "teacher_id")
    private Long teacherId;

    public Long getInfoFindTutorId() {
        return infoFindTutorId;
    }

    public void setInfoFindTutorId(Long infoFindTutorId) {
        this.infoFindTutorId = infoFindTutorId;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InfoFindTutorAccountKey that = (InfoFindTutorAccountKey) o;
        return Objects.equals(infoFindTutorId, that.infoFindTutorId) && Objects.equals(teacherId, that.teacherId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(infoFindTutorId, teacherId);
    }
}
