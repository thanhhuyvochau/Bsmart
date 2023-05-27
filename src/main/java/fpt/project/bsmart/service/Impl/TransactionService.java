package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.constant.EOrderStatus;
import fpt.project.bsmart.entity.constant.ETransactionStatus;
import fpt.project.bsmart.entity.constant.ETransactionType;
import fpt.project.bsmart.entity.dto.TransactionDto;
import fpt.project.bsmart.entity.request.DepositRequest;
import fpt.project.bsmart.entity.request.PayCourseRequest;
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
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static fpt.project.bsmart.util.Constants.ErrorMessage.COURSE_NOT_FOUND_BY_ID;

@Service
public class TransactionService implements ITransactionService {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final MessageUtil messageUtil;

    private final BankRepository bankRepository;

    private final OrderDetailRepository orderDetailRepository;


    private final CourseRepository courseRepository;

    private final OrderRepository orderRepository;

    private final SubCourseRepository subCourseRepository;
    private final CartItemRepository cartItemRepository;

    private final ReferralCodeRepository referralCodeRepository;

    public TransactionService(WalletRepository walletRepository, TransactionRepository transactionRepository, UserRepository userRepository, MessageUtil messageUtil, BankRepository bankRepository, OrderDetailRepository orderDetailRepository, CourseRepository courseRepository, OrderRepository orderRepository, SubCourseRepository subCourseRepository, CartItemRepository cartItemRepository, ReferralCodeRepository referralCodeRepository) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.messageUtil = messageUtil;
        this.bankRepository = bankRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.courseRepository = courseRepository;
        this.orderRepository = orderRepository;
        this.subCourseRepository = subCourseRepository;
        this.cartItemRepository = cartItemRepository;
        this.referralCodeRepository = referralCodeRepository;
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
                .withMessage(messageUtil.getLocalMessage("Không tìm thấy người dùng hiện tại với id:") + userId));
        Wallet wallet = user.getWallet();
        Page<Transaction> transactionsPages = transactionRepository.findAllByWallet(wallet, pageable);
        return PageUtil.convert(transactionsPages.map(ConvertUtil::convertTransactionToDto));
    }

    @Override
    public Boolean deposit(DepositRequest request) {
        Wallet wallet = SecurityUtil.getCurrentUserWallet();
        BigDecimal amount = request.getAmount();
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
    public Boolean payQuickCourse(PayCourseRequest request) {
        SubCourse subCourse = subCourseRepository.findById(request.getSubCourseId())
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(COURSE_NOT_FOUND_BY_ID) + request.getSubCourseId()));
        BigDecimal price = subCourse.getPrice();
        if (price == null) {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage("Hệ thống đang chỉnh sửa về giá của khóa học ! Vui long thử lại sau");
        }

        Wallet wallet = SecurityUtil.getCurrentUserWallet();
        BigDecimal presentBalance = wallet.getBalance();
        if (presentBalance.compareTo(price) < 0) {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage("Số dư của bạn không đủ để thanh toán khóc học này, vui lòng nạp thêm!");
        }

        List<SubCourse> subCourses = new ArrayList<>();
        User owner = wallet.getOwner();
        List<Order> orders = owner.getOrder();
        orders.forEach(order -> {
            List<OrderDetail> orderDetails = order.getOrderDetails();
            orderDetails.forEach(orderDetail -> {
                subCourses.add(orderDetail.getSubCourse());
            });
        });
        List<SubCourse> checkRegistered = subCourses.stream().filter(subCou -> subCou.getId().equals(subCourse.getId())).collect(Collectors.toList());
        if (!checkRegistered.isEmpty()) {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage("Bạn đã thanh toán khóa học này trươc đó !!!");
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
        order.setUser(wallet.getOwner());

        List<ReferralCode> referalCodeList = new ArrayList<>();
        ReferralCode referralCode = null;
        if (request.getReferralCode() != null || request.getReferralCode().isEmpty()) {
            referralCode = referralCodeRepository.findByCodeAndStatusIsTrue(request.getReferralCode())
                    .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage("Mã giới thiệu không tìm thấy!!") + request.getReferralCode()));


            ReferralCodeType referralCodeType = referralCode.getReferralCodeType();
            if (referralCodeType == null) {
                throw ApiException.create(HttpStatus.NOT_FOUND).withMessage("link  giới thiệu không thuộc loại khuyến mãi nào ");
            }
            Double discount = referralCodeType.getDiscount();
            if (discount != null && discount > 0) {
                price = price.subtract(price.multiply(BigDecimal.valueOf(discount)).divide(BigDecimal.valueOf(100), 2, RoundingMode.CEILING));
            }

            if (referralCode.getUsed() == null) {
                throw ApiException.create(HttpStatus.NOT_FOUND).withMessage("Mã giới thiêu đã được sử dụng rồi !! Vui lòng nhập mã khác");
            }
            if (referralCode.getUser() == null) {
                throw ApiException.create(HttpStatus.NOT_FOUND).withMessage("Mã giới thiệu không tìm thấy ");
            }
            if (referralCode.getUsed()) {
                throw ApiException.create(HttpStatus.NOT_FOUND).withMessage("Mã giới thiêu đã được sử dụng rồi !! Vui lòng nhập mã khác");
            }
            referalCodeList.add(referralCode);


            orderDetail.setReferralCode(request.getReferralCode());
            orderDetail.getReferralCodes().addAll(referalCodeList);
            orderDetail.setReferralCodes(referalCodeList);
        }
        orderRepository.save(order);
        orderDetail.setOrder(order);

        orderDetailRepository.save(orderDetail);
        if (referralCode != null) {
            referralCode.setUsed(true);
            referralCode.setStatus(false);
            referralCode.setOrderDetail(orderDetail);

            User user = referralCode.getUser();
            Double point = user.getPoint();
            user.setPoint(point + 10D);
        }
        Transaction transaction = new Transaction();
        transaction.setAmount(price);
        transaction.setStatus(ETransactionStatus.SUCCESS);
        transaction.setOrder(order);
        transaction.setWallet(wallet);
        transaction.setType(ETransactionType.PAY);
        transaction.setBeforeBalance(presentBalance);
        transaction.setAfterBalance(presentBalance.subtract(price));
        wallet.decreaseBalance(transaction.getAmount());
        transactionRepository.save(transaction);
        return true;
    }

    @Override
    public Boolean payCourseFromCart(List<PayCourseRequest> request) {
        Cart cart = SecurityUtil.getCurrentUserCart();
        List<Long> cartItemIds = request.stream().map(PayCourseRequest::getCartItemId).collect(Collectors.toList());
        List<CartItem> boughtCartItems = cartItemRepository.findAllById(cartItemIds);
        if (!Objects.equals(boughtCartItems.size(), cartItemIds.size())) {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage("Có lỗi đã xảy ra, có thể do khóa học trong giỏ hàng không hợp lệ!");
        }
        List<Transaction> transactions = new ArrayList<>();
        for (CartItem cartItem : boughtCartItems) {
            SubCourse subCourse = cartItem.getSubCourse();
            BigDecimal price = subCourse.getPrice();
            Wallet wallet = SecurityUtil.getCurrentUserWallet();
            BigDecimal presentBalance = wallet.getBalance();
            if (presentBalance.compareTo(price) < 0) {
                throw ApiException.create(HttpStatus.NOT_FOUND).withMessage("Số dư của bạn không đủ để thanh toán khóc học này, vui lòng nạp thêm!");
            }
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setSubCourse(subCourse);
            orderDetail.setFinalPrice(price);
            orderDetail.setOriginalPrice(price);

            Order order = new Order();
            order.setStatus(EOrderStatus.SUCCESS);
            order.setTotalPrice(price);
            order.getOrderDetails().add(orderDetail);

            Transaction transaction = new Transaction();
            transaction.setAmount(cartItem.getPrice());
            transaction.setStatus(ETransactionStatus.SUCCESS);
            transaction.setOrder(order);
            transaction.setWallet(wallet);
            transaction.setType(ETransactionType.PAY);
            transaction.setBeforeBalance(presentBalance);
            transaction.setAfterBalance(presentBalance.subtract(price));

            cart.removeCartItem(cartItem);
            wallet.decreaseBalance(transaction.getAmount());
            transactions.add(transaction);
        }
        transactionRepository.saveAll(transactions);
        return true;
    }
}
