package fpt.project.bsmart.entity.constant;

public enum ETransactionType {
    DEPOSIT("Nạp tiền"), WITHDRAW("Rút tiền"), PAY("Thanh toán"), TRANSFER("Chuyển tiền"), GIFT("Quà tặng"), REFUND("Hoàn tiền");

    private final String label;

    ETransactionType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
