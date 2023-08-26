package fpt.project.bsmart.entity.constant;

public enum ECourseClassStatus {
    REQUESTING("Đang Yêu cầu", // Lớp / khóa học được yêu cầu mở nhưng chưa sẵn sàng duyệt của giáo viên
            "Requesting"),
    WAITING("Đang Chờ", // Lớp / khóa học đã sẵn được duyệt
            "Waiting"),
    EDITREQUEST("Yêu cầu Sửa", // lớp / khóa học ko hợp lệ, admin yêu cầu giáo viên thay đổi rồi yêu cầu mở lớp / khóa học lại
            "Edit Request"),
    REJECTED("Bị Từ chối", // Từ chối mở lớp / khóa học
            "Rejected"),
    NOTSTART("Chưa Bắt đầu", // Lớp / khóa học được duyệt và đang trong quá trình tuyển sinh
            "Not Started"),
    STARTING("Đang Bắt đầu", // Lớp ngưng tuyển sinh và đang bắt đầu dạy
            "Starting"),
    ENDED("Đã Kết thúc", // Lớp đã kết thúc
            "Ended"),
    CANCEL("Đã Hủy", // Lớp bị hủy
            "Cancelled"),
    UNSATISFY("Không Đủ", // Lớp không đủ số lượng học sinh
            "Unsatisfactory"),
    BLOCK("Bị Khóa", // Khóa học / lớp bị khóa để chỉnh sửa thông tin
            "Blocked"),
    ALL("Tất cả", // Tất cả
            "All");

    private final String vietnameseLabel;
    private final String englishLabel;

    ECourseClassStatus(String vietnameseLabel, String englishLabel) {
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
