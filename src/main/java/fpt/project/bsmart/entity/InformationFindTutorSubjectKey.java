package fpt.project.bsmart.entity;


import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;


@Embeddable
public class InformationFindTutorSubjectKey implements Serializable {

    @Column(name = "info_find_tutorl_id")
    private Long infoFindTutorId;

    @Column(name = "subject_id")
    private Long subjectId;

    public Long getInfoFindTutorId() {
        return infoFindTutorId;
    }

    public void setInfoFindTutorId(Long infoFindTutorId) {
        this.infoFindTutorId = infoFindTutorId;
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
        InformationFindTutorSubjectKey that = (InformationFindTutorSubjectKey) o;
        return Objects.equals(infoFindTutorId, that.infoFindTutorId) && Objects.equals(subjectId, that.subjectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(infoFindTutorId, subjectId);
    }
}
