package fpt.project.bsmart.entity.constant;

public enum ECourseStatus {
    REQUESTING , // Lớp được yêu cầu mở nhưng chưa sẵn sàng duyệt của giáo viên

    WAITING, // Lớp đã sẵn được  duyệt

    EDITREQUEST , // lớp ko hợp lê , admin yêu câu giáo viên thay đổi rồi request mở lớp lại

    REJECTED, // Từ chối mở lớp

    NOTSTART, // Lớp được duyệt và đang trong quá trình tuyển sinh

    STARTING, // Lớp ngưng tuyển sinh và đang bắt đầu dạy

    ENDED,// Lớp đã kết thúc

    CANCEL; // Lớp bị hủy


    }
