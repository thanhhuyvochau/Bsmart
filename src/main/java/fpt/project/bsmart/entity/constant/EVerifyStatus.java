package fpt.project.bsmart.entity.constant;

import org.springframework.http.HttpStatus;

public enum EVerifyStatus {
    SUCCESS("Xác thực thành công", HttpStatus.OK),
    FAIL("Xác thực thất bại", HttpStatus.FORBIDDEN),
    USED("Mã xác thực đã được sử dụng", HttpStatus.FORBIDDEN),
    EXPIRED("Mã xác thực đã hết hạn, vui lòng yêu cầu mã mới", HttpStatus.FORBIDDEN);
    private final String message;
    private final HttpStatus status;

    EVerifyStatus(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
