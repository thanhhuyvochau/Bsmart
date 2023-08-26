package fpt.project.bsmart.entity.constant;

public enum ECourseClassStatus {
    REQUESTING, // Lớp / khóa học được yêu cầu mở nhưng chưa sẵn sàng duyệt của giáo viên

    WAITING, // Lớp / khóa học đã sẵn được  duyệt

    EDITREQUEST, // lớp / khóa học ko hợp lê , admin yêu câu giáo viên thay đổi rồi request mở lớp / khóa học lại

    REJECTED, // Từ chối mở lớp / khóa học

    NOTSTART, // Lớp / khóa học được duyệt và đang trong quá trình tuyển sinh

    STARTING, // Lớp ngưng tuyển sinh và đang bắt đầu dạy

    ENDED,// Lớp đã kết thúc

    CANCEL, // Lớp bị hủy
    UNSATISFY, // Lớp không đủ số lượng học sinh

    BLOCK, // Khóa học / lớp bị khóa để chỉnh sửa thông tin

    ALL;// Tất cả

    ECourseClassStatus() {
    }
}
