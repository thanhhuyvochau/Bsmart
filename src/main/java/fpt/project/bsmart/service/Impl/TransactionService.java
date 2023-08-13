package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.config.vnpay.VnpConfig;
import fpt.project.bsmart.director.NotificationDirector;
import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.constant.ECourseStatus;
import fpt.project.bsmart.entity.constant.EOrderStatus;
import fpt.project.bsmart.entity.constant.ETransactionStatus;
import fpt.project.bsmart.entity.constant.ETransactionType;
import fpt.project.bsmart.entity.dto.ResponseMessage;
import fpt.project.bsmart.entity.dto.TransactionDto;
import fpt.project.bsmart.entity.request.*;
import fpt.project.bsmart.entity.response.RevenueResponse;
import fpt.project.bsmart.entity.response.VnPayResponse;
import fpt.project.bsmart.repository.*;
import fpt.project.bsmart.service.ITransactionService;
import fpt.project.bsmart.util.*;
import fpt.project.bsmart.util.specification.TransactionSpecificationBuilder;
import org.jetbrains.annotations.NotNull;
import fpt.project.bsmart.entity.response.WithDrawResponse;
import fpt.project.bsmart.payment.PaymentGateway;
import fpt.project.bsmart.payment.PaymentPicker;
import fpt.project.bsmart.payment.PaymentResponse;
import fpt.project.bsmart.repository.*;
import fpt.project.bsmart.service.ITransactionService;
import fpt.project.bsmart.util.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static fpt.project.bsmart.util.Constants.ErrorMessage.*;
import static fpt.project.bsmart.util.Constants.ErrorMessage.Invalid.*;

@Service
@Transactional
public class TransactionService implements ITransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final MessageUtil messageUtil;

    private final BankRepository bankRepository;

    private final CartItemRepository cartItemRepository;

    private final VnpConfig vnpConfig;
    private final ClassRepository classRepository;
    private final WebSocketUtil webSocketUtil;
    private final NotificationRepository notificationRepository;
    private final PaymentPicker paymentPicker;

    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository, MessageUtil messageUtil, BankRepository bankRepository, CartItemRepository cartItemRepository, VnpConfig vnpConfig, ClassRepository classRepository, WebSocketUtil webSocketUtil, NotificationRepository notificationRepository, PaymentPicker paymentPicker) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.messageUtil = messageUtil;
        this.bankRepository = bankRepository;
        this.cartItemRepository = cartItemRepository;
        this.vnpConfig = vnpConfig;
        this.classRepository = classRepository;
        this.webSocketUtil = webSocketUtil;
        this.notificationRepository = notificationRepository;
        this.paymentPicker = paymentPicker;
    }

    @Override
    public ApiPage<TransactionDto> getSelfTransactions(Pageable pageable) {
        Wallet wallet = SecurityUtil.getCurrentUserWallet();
        Page<Transaction> transactionsPages = transactionRepository.findAllByWallet(wallet, pageable);
        return PageUtil.convert(transactionsPages.map(ConvertUtil::convertTransactionToDto));
    }

    @Override
    public ApiPage<TransactionDto> getUserTransactions(Pageable pageable, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                .withMessage(messageUtil.getLocalMessage(messageUtil.getLocalMessage(USER_NOT_FOUND_BY_ID)) + userId));
        Wallet wallet = user.getWallet();
        Page<Transaction> transactionsPages = transactionRepository.findAllByWallet(wallet, pageable);
        return PageUtil.convert(transactionsPages.map(ConvertUtil::convertTransactionToDto));
    }

//    @Override
//    public Boolean deposit(DepositRequest request) {
//        Wallet wallet = SecurityUtil.getCurrentUserWallet();
//        BigDecimal amount = request.getAmount();
//        // Nạp từ 10 nghìn trở lên
//        if (amount.compareTo(BigDecimal.valueOf(10000)) <= 0) {
//            throw ApiException.create(HttpStatus.CONFLICT).withMessage(messageUtil.getLocalMessage(BELOW_MIN_MONEY_AMOUNT_IN_TRANSACTION));
//        }
//        Transaction transaction = Transaction.build(amount, null, null, null, wallet, ETransactionType.DEPOSIT);
//        wallet.setBalance(wallet.getBalance().add(amount));
//        transactionRepository.save(transaction);
//        return true;
//    }

    @Override
    public Boolean withdraw(WithdrawRequest request) {
        Wallet wallet = SecurityUtil.getCurrentUserWallet();
        BigDecimal amount = request.getAmount();
        if (amount.compareTo(BigDecimal.ZERO) <= 0 || amount.compareTo(wallet.getBalance()) > 0) {
            return false;
        }
        Bank bank = bankRepository.findById(request.getBankId()).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(BANK_NOT_FOUND_BY_ID) + request.getBankId()));
        Transaction transaction = Transaction.build(amount, request.getBankAccount(), request.getBankAccountOwner(), bank, wallet, ETransactionType.WITHDRAW);
        wallet.setBalance(wallet.getBalance().subtract(amount));
        transactionRepository.save(transaction);
        return true;
    }

    public List<WithDrawResponse> managerGetWithDrawRequest(){
        List<Transaction> transactions = transactionRepository.findAllByStatus(ETransactionStatus.WAITING);
        return transactions.stream().map(ConvertUtil::convertWithdrawRequestToWithdrawResponse).collect(Collectors.toList());
    }

    public Boolean managerProcessWithdrawRequest(List<ProcessWithdrawRequest> requests){
        List<Transaction> pendingTransactions = transactionRepository.findAllByStatus(ETransactionStatus.WAITING);
        for (ProcessWithdrawRequest request : requests){
            Transaction transaction = pendingTransactions.stream().filter(x -> x.getId().equals(request.getId()))
                    .findFirst()
                    .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(TRANSACTION_NOT_FOUND_BY_ID) + request.getId()));
            handleUpdatedProcess(transaction, request);
        }
        transactionRepository.saveAll(pendingTransactions);
        return true;
    }

    private void handleUpdatedProcess(Transaction transaction, ProcessWithdrawRequest request){
        switch (request.getStatus()){
            case SUCCESS:
                updateTransactionStatus(transaction, request.getStatus(), request.getNote());
                break;
            case FAIL:
            case CANCEL:
                Wallet wallet = transaction.getWallet();
                wallet.setBalance(wallet.getBalance().add(transaction.getAmount()));
                updateTransactionStatus(transaction, request.getStatus(), request.getNote());
                break;
            case WAITING:
            default:
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(INVALID_TRANSACTION_STATUS) + request.getStatus());
        }
    }

    private void updateTransactionStatus(Transaction transaction, ETransactionStatus status, String note){
        transaction.setStatus(status);
        if(StringUtil.isNotNullOrEmpty(note)){
            transaction.setNote(note);
        }
    }

    @Override
    public PaymentResponse payCourseFromCart(PayCartRequest payCartRequest) throws UnsupportedEncodingException {
        Cart cart = SecurityUtil.getCurrentUserCart();
        List<PayCartItemRequest> payCartItemRequestList = payCartRequest.getPayCartItemRequestList();
        List<Long> cartItemIds = payCartItemRequestList.stream().map(PayCartItemRequest::getCartItemId).collect(Collectors.toList());
        List<CartItem> boughtCartItems = cartItemRepository.findAllById(cartItemIds);
        if (!Objects.equals(boughtCartItems.size(), cartItemIds.size())) {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(INVALID_ITEM_IN_CART));
        }
        User user = SecurityUtil.getUserOrThrowException(SecurityUtil.getCurrentUserOptional());
        Order order = new Order();
        order.setStatus(EOrderStatus.WAIT);
        order.setUser(user);
        for (CartItem cartItem : boughtCartItems) {
            Class clazz = cartItem.getClazz();
            boolean isPaidSubCourse = CourseUtil.isPaidCourse(clazz, user);
            if (isPaidSubCourse) {
                throw ApiException.create(HttpStatus.CONFLICT).withMessage(messageUtil.getLocalMessage(SUB_COURSE_IS_PAID));
            }
            BigDecimal price = clazz.getPrice();
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setClazz(clazz);
            orderDetail.setFinalPrice(price);
            orderDetail.setOriginalPrice(price);
            orderDetail.setOrder(order);
            order.getOrderDetails().add(orderDetail);
            order.setTotalPrice(order.getTotalPrice().add(orderDetail.getFinalPrice()));
            cart.removeCartItem(cartItem);
        }
        PaymentGateway paymentGateway = paymentPicker.pickByType(payCartRequest.getType());
        return paymentGateway.pay(order);
    }

    public PaymentResponse payQuickCourse(PayRequest request) throws UnsupportedEncodingException {
        Class clazz = classRepository.findById(request.getClazzId())
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage(messageUtil.getLocalMessage(SUB_COURSE_NOT_FOUND_BY_ID) + request.getClazzId()));
        if (!clazz.getStatus().equals(ECourseStatus.NOTSTART)) {
            throw ApiException.create(HttpStatus.METHOD_NOT_ALLOWED).withMessage(messageUtil.getLocalMessage(INVALID_COURSE_STATUS_TO_PURCHASE));
        }
        User user = SecurityUtil.getUserOrThrowException(SecurityUtil.getCurrentUserOptional());
        boolean isPaidSubCourse = CourseUtil.isPaidCourse(clazz, user);
        if (isPaidSubCourse) {
            throw ApiException.create(HttpStatus.CONFLICT).withMessage(messageUtil.getLocalMessage(SUB_COURSE_IS_PAID));
        }
        if (CourseUtil.isFullMemberOfSubCourse(clazz)) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(MAX_STUDENT_IN_CLASS));
        }
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setClazz(clazz);
        BigDecimal price = clazz.getPrice();
        orderDetail.setFinalPrice(price);
        orderDetail.setOriginalPrice(price);
        Order order = Order.Builder.builder()
                .setOrderDetails(Arrays.asList(orderDetail))
                .setTotalPrice(price)
                .setUser(user)
                .setStatus(EOrderStatus.WAIT)
                .build();
        orderDetail.setOrder(order);

        PaymentGateway paymentGateway = paymentPicker.pickByType(request.getType());
        return paymentGateway.pay(order);
    }

    @Override
    public Boolean executeAfterVnPayReturn(HttpServletRequest request) {
        String responseCode = request.getParameter("vnp_ResponseCode");
        String transactionStatus = request.getParameter("vnp_TransactionStatus");
        String referenceValues = request.getParameter("vnp_TxnRef");
        String transactionId = referenceValues;
        Transaction transaction = transactionRepository
                .findById(Long.valueOf(transactionId)).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage(messageUtil.getLocalMessage(TRANSACTION_NOT_FOUND_BY_ID) + transactionId));
        if (transactionStatus.equals("00") && responseCode.equals("00")) {
            Order order = transaction.getOrder();
            order.setStatus(EOrderStatus.SUCCESS);
            transaction.setStatus(ETransactionStatus.SUCCESS);

            User user = order.getUser();
            List<Class> orderedClasses = order.getOrderDetails().stream().map(OrderDetail::getClazz).collect(Collectors.toList());
            for (Class orderedClass : orderedClasses) {
                StudentClass studentClass = new StudentClass();
                studentClass.setStudent(user);
                studentClass.setClazz(orderedClass);
                orderedClass.getStudentClasses().add(studentClass);
            }
            List<Notification> notifications = new ArrayList<>();
            Notification paymentSuccessNotification = NotificationDirector.buildPaymentNotification(order, transaction);
            notifications.add(paymentSuccessNotification);
            notifications.addAll(getEnrollClassNotifications(order));
            notificationRepository.saveAll(notifications);

            for (Notification notification : notifications) {
                ResponseMessage responseMessage = ConvertUtil.convertNotificationToResponseMessage(notification, user);
                webSocketUtil.sendPrivateNotification(user.getEmail(), responseMessage);
            }
            classRepository.saveAll(orderedClasses);
            return true;
        }
        return false;
    }

    public List<RevenueResponse> getRevenueForAdminPage(TransactionRequest request){
        TransactionSpecificationBuilder builder = TransactionSpecificationBuilder.transactionSpecificationBuilder()
                .filterFromDate(request.getStartDate())
                .filterToDate(request.getEndDate())
                .filterByBuyer(request.getBuyerId())
                .filterBySeller(request.getSellerId())
                .filterByCourse(request.getCourseId());
        List<Transaction> transactions = transactionRepository.findAll(builder.build());
        return ConvertUtil.convertTransactionsToRevenueResponses(transactions);
    }
    
    private List<Notification> getEnrollClassNotifications(Order order) {
        List<Notification> enrolledClassNotifications = new ArrayList<>();
        if (Objects.equals(order.getStatus(), EOrderStatus.SUCCESS)) {
            User student = order.getUser();
            for (OrderDetail orderDetail : order.getOrderDetails()) {
                Class clazz = orderDetail.getClazz();
                Notification notification = NotificationDirector.buildEnrollClass(clazz, student);
                enrolledClassNotifications.add(notification);
            }
        }
        return enrolledClassNotifications;
    }
}
