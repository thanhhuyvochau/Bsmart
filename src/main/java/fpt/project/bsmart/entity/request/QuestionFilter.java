package fpt.project.bsmart.entity.request;

import javax.validation.constraints.NotNull;

public class QuestionFilter {
    /**
     * Tạm thời filter trong question bank sẽ chỉ có subject => tương lai có thì thêm sau
     */
    @NotNull
    private Long subjectId;

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }
}
