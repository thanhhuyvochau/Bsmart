package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.dto.TransactionDto;
import fpt.project.bsmart.entity.request.WithdrawRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface ITransactionService {
    ApiPage<TransactionDto> getSelfTransactions(Pageable pageable);

    ApiPage<TransactionDto> getUserTransactions(Pageable pageable, Long userId);

    Boolean deposit(BigDecimal amount);

    Boolean withdraw(WithdrawRequest request);

    // Hiện tại sẽ chỉ làm các giao dịch rút tiền và nạp tiền
    Boolean payCourse(Long courseId);
}
