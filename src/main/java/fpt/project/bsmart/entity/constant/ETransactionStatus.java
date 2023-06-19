package fpt.project.bsmart.entity.constant;

public enum ETransactionStatus {
    FAIL("Thất bại"), CANCEL("Hủy"), SUCCESS("Thành công"), WAITING("Đang chờ xử lý");
    private final String label;

    ETransactionStatus(String name) {
        this.label = name;
    }

    public String getLabel() {
        return label;
    }
}
