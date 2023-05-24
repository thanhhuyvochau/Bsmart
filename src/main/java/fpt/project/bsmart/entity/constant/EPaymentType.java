package fpt.project.bsmart.entity.constant;

public enum EPaymentType {
    CASH("Tiền mặt"), BANKING ("Chuyển khoản"), WALLET("Ví Tiền");
    private final String name;

    EPaymentType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
