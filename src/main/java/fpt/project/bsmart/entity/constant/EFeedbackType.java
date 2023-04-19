package fpt.project.bsmart.entity.constant;

public enum EFeedbackType {
    SUB_COURSE_FIRST_HALF("Nửa đầu khóa học"),
    SUB_COURSE_SECOND_HALF("Nửa sau khóa học"),
    REPORT("Báo cáo");
    private final String type;

    EFeedbackType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
