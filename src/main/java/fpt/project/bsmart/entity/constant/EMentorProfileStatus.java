package fpt.project.bsmart.entity.constant;

public enum EMentorProfileStatus {
    REQUESTING("Yêu cầu Mở", "Requesting"),
    WAITING("Đang Chờ", "Waiting"),
    EDITREQUEST("Yêu cầu Sửa", "Edit Request"),
    REJECTED("Bị Từ chối", "Rejected"),
    STARTING("Đang Sử dụng", "Starting"),
    CLOSED("Đã Đóng", "Closed");

    private final String vietnameseLabel;
    private final String englishLabel;

    EMentorProfileStatus(String vietnameseLabel, String englishLabel) {
        this.vietnameseLabel = vietnameseLabel;
        this.englishLabel = englishLabel;
    }

    public String getVietnameseLabel() {
        return vietnameseLabel;
    }

    public String getEnglishLabel() {
        return englishLabel;
    }
}
