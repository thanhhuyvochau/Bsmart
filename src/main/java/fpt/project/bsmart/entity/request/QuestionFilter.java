package fpt.project.bsmart.entity.request;

import javax.validation.constraints.NotNull;

public class QuestionFilter {
    /**
     * Tạm thời filter trong question bank sẽ chỉ có subject => tương lai có thì thêm sau
     */
    @NotNull
    private Long subjectId;
    private int selection = 0; // -1 private, 0 all, 1 shared

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public int getSelection() {
        return selection;
    }

    public void setSelection(int selection) {
        this.selection = selection;
    }
}
