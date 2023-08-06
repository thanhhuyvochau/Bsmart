package fpt.project.bsmart.payment;

import fpt.project.bsmart.config.vnpay.VnpConfig;
import fpt.project.bsmart.entity.Order;
import fpt.project.bsmart.entity.Transaction;
import fpt.project.bsmart.entity.constant.EPaymentType;
import fpt.project.bsmart.entity.constant.ETransactionStatus;
import fpt.project.bsmart.entity.constant.ETransactionType;
import fpt.project.bsmart.entity.response.VnPayResponse;
import fpt.project.bsmart.repository.TransactionRepository;
import fpt.project.bsmart.util.ConvertUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
@Transactional
public class VnPayPaymentGateWay implements PaymentGateway<VnPayResponse> {
    private final TransactionRepository transactionRepository;
    private final VnpConfig vnpConfig;

    public VnPayPaymentGateWay(TransactionRepository transactionRepository, VnpConfig vnpConfig) {
        this.transactionRepository = transactionRepository;
        this.vnpConfig = vnpConfig;
    }

    @Override
    public PaymentResponse<VnPayResponse> pay(Order order) throws UnsupportedEncodingException {
        Transaction transaction = new Transaction();
        transaction.setOrder(order);
        transaction.setAmount(order.getTotalPrice());
        transaction.setPaymentType(EPaymentType.BANKING);
        transaction.setType(ETransactionType.PAY);
        transaction.setStatus(ETransactionStatus.WAITING);
        transactionRepository.save(transaction);

        HttpServletRequest request =
                ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                        .getRequest();
        String paymentUrl = buildPaymentUrl(request, transaction);
        PaymentResponse<VnPayResponse> paymentResponse = ConvertUtil.convertPaymentResponse(order, transaction);
        paymentResponse.setMetadata(new VnPayResponse(paymentUrl));
        return paymentResponse;
    }

    @NotNull
    private String buildPaymentUrl(HttpServletRequest req, Transaction transaction) throws UnsupportedEncodingException {
        Map<String, String> vnp_Params = prepareParameters(req, transaction.getAmount(), transaction.getId().toString());
        transaction.setVpnCommand(vnp_Params.get("vnp_Command"));
        transaction.setOrderInfo(vnp_Params.get("vnp_OrderInfo"));
        //Billing
        //Build data to hash and querystring
        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = vnpConfig.hmacSHA512(vnpConfig.getVnp_HashSecret(), hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = vnpConfig.getVnp_PayUrl() + "?" + queryUrl;
        return paymentUrl;
    }

    @NotNull
    private Map<String, String> prepareParameters(HttpServletRequest req, BigDecimal price, String uniquePayCode) {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_OrderInfo = "Thanh Toan Khoa Hoc";
        String orderType = "pay";
        String vnp_IpAddr = vnpConfig.getIpAddress(req);
        String vnp_TmnCode = vnpConfig.getVnp_TmnCode();
        int amount = price.intValue() * 100;
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");

        String vnp_TxnRef = uniquePayCode;
//        vnp_Params.put("vnp_TxnRef", vnp_TxnRef + "&" + request.getSessionId());
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
        String locate = "VN";
        vnp_Params.put("vnp_Locale", locate);
        vnp_Params.put("vnp_ReturnUrl", vnpConfig.getVnp_Returnurl());
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT-7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        cld.add(Calendar.YEAR, 1);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        //Add Params of 2.0.1 Version
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
        SimpleDateFormat formatterCheck = new SimpleDateFormat("dd-MM-yyyy");
        vnp_Params.put("vnp_OrderType", orderType);
        System.out.println("EXPIRED:" + formatterCheck.format(cld.getTime()));
        return vnp_Params;
    }
}
