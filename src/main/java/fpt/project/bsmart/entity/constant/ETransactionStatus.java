package fpt.project.bsmart.entity.constant;

public enum ETransactionStatus {
    FAIL("Thất bại"), CANCEL("Hủy"), SUCCESS("Thành công");
    private final String name;

    ETransactionStatus(String name) {
        this.name = name;
    }
}
