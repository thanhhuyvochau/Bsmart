package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.dto.TransactionDto;
import fpt.project.bsmart.entity.request.DepositRequest;
import fpt.project.bsmart.entity.request.PayCourseRequest;
import fpt.project.bsmart.entity.request.VpnPayRequest;
import fpt.project.bsmart.entity.request.WithdrawRequest;
import fpt.project.bsmart.entity.response.VnPayResponse;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface ITransactionService {
    ApiPage<TransactionDto> getSelfTransactions(Pageable pageable);

    ApiPage<TransactionDto> getUserTransactions(Pageable pageable, Long userId);

    Boolean deposit(DepositRequest request);

    Boolean withdraw(WithdrawRequest request);

    // Hiện tại sẽ chỉ làm các giao dịch rút tiền và nạp tiền
    VnPayResponse payCourseFromCart(HttpServletRequest req, List<PayCourseRequest> request) throws UnsupportedEncodingException;

    VnPayResponse payQuickCourse(HttpServletRequest req, VpnPayRequest request) throws UnsupportedEncodingException;

    void executeAfterPayment(HttpServletRequest request);

//    Boolean getBankPaymentResult(Long transaction)

}
