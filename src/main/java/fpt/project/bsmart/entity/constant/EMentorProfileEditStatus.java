package fpt.project.bsmart.entity.constant;

public enum EMentorProfileEditStatus {
    CREATING("Đang tạo"),
    PENDING("Đang đợi"),
    APPROVED("Đã được chấp nhận"),
    REJECTED("Đã bị từ chối");
    private final String label;

    EMentorProfileEditStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
