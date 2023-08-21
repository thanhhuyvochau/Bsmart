package fpt.project.bsmart.payment;

import fpt.project.bsmart.config.vnpay.VnpConfig;
import fpt.project.bsmart.entity.Order;
import fpt.project.bsmart.entity.Transaction;
import fpt.project.bsmart.entity.Wallet;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.constant.EOrderStatus;
import fpt.project.bsmart.entity.constant.EPaymentType;
import fpt.project.bsmart.entity.constant.ETransactionStatus;
import fpt.project.bsmart.entity.constant.ETransactionType;
import fpt.project.bsmart.repository.TransactionRepository;
import fpt.project.bsmart.util.ConvertUtil;
import fpt.project.bsmart.util.SecurityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

@Component
@Transactional
public class WalletPaymentGateway implements PaymentGateway<Boolean> {
    private final TransactionRepository transactionRepository;
    private final VnpConfig vnpConfig;

    public WalletPaymentGateway(TransactionRepository transactionRepository, VnpConfig vnpConfig) {
        this.transactionRepository = transactionRepository;
        this.vnpConfig = vnpConfig;
    }

    @Override
    public PaymentResponse<Boolean> pay(Order order) throws UnsupportedEncodingException {
        BigDecimal totalPrice = order.getTotalPrice();

        Transaction transaction = new Transaction();
        transaction.setOrder(order);
        transaction.setAmount(totalPrice);
        transaction.setPaymentType(EPaymentType.WALLET);
        transaction.setType(ETransactionType.PAY);
        transactionRepository.save(transaction);

        Wallet wallet = SecurityUtil.getCurrentUserWallet();
        BigDecimal presentBalance = wallet.getBalance();
        if (presentBalance.compareTo(totalPrice) < 0) {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage("Số dư của bạn không đủ để thanh toán khóc học này!");
        } else {
            order.setStatus(EOrderStatus.SUCCESS);
            transaction.setStatus(ETransactionStatus.SUCCESS);
            PaymentResponse<Boolean> paymentResponse = ConvertUtil.convertPaymentResponse(order, transaction);
            paymentResponse.setMetadata(true);
            wallet.decreaseBalance(transaction.getAmount());
            return paymentResponse;
        }
    }
}
