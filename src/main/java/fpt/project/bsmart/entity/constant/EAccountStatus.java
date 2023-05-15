package fpt.project.bsmart.entity.constant;

public enum EAccountStatus {
    REQUESTING, // tài khoản được yêu cầu mở nhưng chưa sẵn sàng duyệt
    WAITING, // tài khoản đã sẵn được  duyệt
    EDITREQUEST, //  tài khoản ko hợp lê , admin yêu câu giáo viên thay đổi rồi request mở lớp lại
    REJECTED, // Từ chối mở  tài khoản
    STARTING, //  tài khoản đã đươc sử dung
    CLOSED,// tài khoản đã bị khóa (hoặc bỏ)

    EAccountStatus() {
    }
}