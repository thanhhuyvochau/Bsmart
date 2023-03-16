package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.constant.EOrderStatus;
import fpt.project.bsmart.entity.constant.ETransactionType;
import fpt.project.bsmart.entity.dto.TransactionDto;
import fpt.project.bsmart.entity.request.WithdrawRequest;
import fpt.project.bsmart.repository.*;
import fpt.project.bsmart.service.ITransactionService;
import fpt.project.bsmart.util.ConvertUtil;
import fpt.project.bsmart.util.MessageUtil;
import fpt.project.bsmart.util.PageUtil;
import fpt.project.bsmart.util.SecurityUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static fpt.project.bsmart.util.Constants.ErrorMessage.COURSE_NOT_FOUND_BY_ID;

@Service
public class TransactionService implements ITransactionService {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final MessageUtil messageUtil;

    private final BankRepository bankRepository;

    private final CourseRepository courseRepository;

    private final SubCourseRepository subCourseRepository;

    public TransactionService(WalletRepository walletRepository, TransactionRepository transactionRepository, UserRepository userRepository, MessageUtil messageUtil, BankRepository bankRepository, CourseRepository courseRepository, SubCourseRepository subCourseRepository) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.messageUtil = messageUtil;
        this.bankRepository = bankRepository;
        this.courseRepository = courseRepository;
        this.subCourseRepository = subCourseRepository;
    }

    @Override
    public ApiPage<TransactionDto> getSelfTransactions(Pageable pageable) {
        Wallet wallet = SecurityUtil.getCurrentUserWallet();
        List<Transaction> transactions = wallet.getTransactions();
        Page<Transaction> transactionsPages = PageUtil.toPage(transactions, pageable);
        return PageUtil.convert(transactionsPages.map(ConvertUtil::convertTransactionToDto));
    }

    @Override
    public ApiPage<TransactionDto> getUserTransactions(Pageable pageable, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                .withMessage(messageUtil.getLocalMessage("Không tìm thấy người dùng hiện tại với id:") + userId));
        Wallet wallet = user.getWallet();
        List<Transaction> transactions = wallet.getTransactions();
        Page<Transaction> transactionsPages = PageUtil.toPage(transactions, pageable);
        return PageUtil.convert(transactionsPages.map(ConvertUtil::convertTransactionToDto));
    }

    @Override
    public Boolean deposit(BigDecimal amount) {
        Wallet wallet = SecurityUtil.getCurrentUserWallet();
        // Nạp từ 10 nghìn trở lên
        if (amount.compareTo(BigDecimal.valueOf(10000)) <= 0) {
           throw ApiException.create(HttpStatus.CONFLICT).withMessage("Bạn phải nạp từ 10.000 VNĐ trờ lên!");
        }
        Transaction transaction = Transaction.build(amount, null, null, null, wallet, ETransactionType.DEPOSIT);
        wallet.setBalance(wallet.getBalance().add(amount));
        transactionRepository.save(transaction);
        return true;
    }

    @Override
    public Boolean withdraw(WithdrawRequest request) {
        Wallet wallet = SecurityUtil.getCurrentUserWallet();
        BigDecimal amount = request.getAmount();
        if (amount.compareTo(BigDecimal.ZERO) <= 0 || amount.compareTo(wallet.getBalance()) > 0) {
            return false;
        }
        Bank bank = bankRepository.findById(request.getBankId()).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy ngân hàng hỗ trợ xin thử lại!"));
        Transaction transaction = Transaction.build(amount, request.getBankAccount(), request.getBankAccountOwner(), bank, wallet, ETransactionType.WITHDRAW);
        wallet.setBalance(wallet.getBalance().subtract(amount));
        transactionRepository.save(transaction);
        return true;
    }

    @Override
    public Boolean payCourse(Long subCourseId) {
        SubCourse subCourse = subCourseRepository.findById(subCourseId)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(COURSE_NOT_FOUND_BY_ID) + subCourseId));
        BigDecimal price = subCourse.getPrice();
        Wallet wallet = SecurityUtil.getCurrentUserWallet();
        BigDecimal presentBalance = wallet.getBalance();
        if (presentBalance.compareTo(price) < 0) {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage("Số dư của bạn không đủ để thanh toán khóc học này, vui lòng nạp thêm!");
        }
        // TODO: Tạm thời chưa xử lý khuyến mãi, sẽ bổ sung xử lý KM sau
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setSubCourse(subCourse);
        orderDetail.setFinalPrice(price);
        orderDetail.setOriginalPrice(price);

        Order order = new Order();
        order.setStatus(EOrderStatus.SUCCESS);
        order.setTotalPrice(price);
        order.getOrderDetails().add(orderDetail);

        Transaction transaction = new Transaction();
        transaction.setOrder(order);
        transaction.setWallet(wallet);
        transaction.setType(ETransactionType.PAY);
        transaction.setBeforeBalance(presentBalance);
        transaction.setAfterBalance(presentBalance.subtract(price));

        transactionRepository.save(transaction);
        return true;
    }
}
