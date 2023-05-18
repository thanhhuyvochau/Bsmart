package fpt.project.bsmart.entity.common;

public enum EVerifyStatus {
    SUCCESS("Xác thực thành công"),
    FAIL("Xác thực thất bại"),
    USED("Mã xác thực đã được sử dụng"),
    EXPIRED("Mã xác thực đã hết hạn, vui lòng yêu cầu mã mới");
    private final String message;

    EVerifyStatus(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
