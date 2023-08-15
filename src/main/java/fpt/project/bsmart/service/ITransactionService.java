package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.dto.TransactionDto;
import fpt.project.bsmart.entity.request.*;
import fpt.project.bsmart.entity.response.UserRevenueResponse;
import fpt.project.bsmart.entity.response.SystemRevenueResponse;
import fpt.project.bsmart.entity.response.WithDrawResponse;
import fpt.project.bsmart.payment.PaymentResponse;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface ITransactionService {
    ApiPage<TransactionDto> getSelfTransactions(Pageable pageable);

    ApiPage<TransactionDto> getUserTransactions(Pageable pageable, Long userId);

//    Boolean deposit(DepositRequest request);

    Boolean withdraw(WithdrawRequest request);
    List<WithDrawResponse> managerGetWithDrawRequest();
    Boolean managerProcessWithdrawRequest(List<ProcessWithdrawRequest> requests);

    // Hiện tại sẽ chỉ làm các giao dịch rút tiền và nạp tiền
    PaymentResponse payCourseFromCart(PayCartRequest request) throws UnsupportedEncodingException;

    PaymentResponse payQuickCourse(PayRequest request) throws UnsupportedEncodingException;

    Boolean executeAfterVnPayReturn(HttpServletRequest request);

//    Boolean getBankPaymentResult(Long transaction)
    UserRevenueResponse getUserRevenue(Long userId);
    List<SystemRevenueResponse> getSystemRevenue(Integer year);
}
