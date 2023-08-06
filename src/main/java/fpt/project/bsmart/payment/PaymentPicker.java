package fpt.project.bsmart.payment;

import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.constant.EPaymentType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class PaymentPicker {
    private final VnPayPaymentGateWay vnPayPaymentGateWay;
    private final WalletPaymentGateway walletPaymentGateway;

    public PaymentPicker(VnPayPaymentGateWay vnPayPaymentGateWay, WalletPaymentGateway walletPaymentGateway) {
        this.vnPayPaymentGateWay = vnPayPaymentGateWay;
        this.walletPaymentGateway = walletPaymentGateway;
    }

    public PaymentGateway pickByType(EPaymentType type) {
        switch (type) {
            case BANKING:
                return vnPayPaymentGateWay;
            case WALLET:
                return walletPaymentGateway;
            default:
                throw ApiException.create(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy phương thức giao dịch, vui lòng kiểm tra lại");
        }
    }
}
