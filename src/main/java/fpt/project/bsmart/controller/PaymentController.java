package fpt.project.bsmart.controller;


import fpt.project.bsmart.entity.Transaction;
import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.request.VpnPayRequest;
import fpt.project.bsmart.entity.response.PaymentResponse;
import fpt.project.bsmart.service.ITransactionService;
import fpt.project.bsmart.util.WebSocketUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    private final ITransactionService transactionService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final WebSocketUtil webSocketUtil;
    @Value("${payment-redirect}")
    private String paymentRedirect;
    private final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    public PaymentController(ITransactionService transactionService, SimpMessagingTemplate simpMessagingTemplate, WebSocketUtil webSocketUtil) {
        this.transactionService = transactionService;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.webSocketUtil = webSocketUtil;
    }

    @PostMapping

    @PreAuthorize("hasAuthority('STUDENT')")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPayment(HttpServletRequest req, @RequestBody VpnPayRequest request) throws IOException {
        return ResponseEntity.ok(ApiResponse.success(transactionService.startPayment(req, request)));
    }


    @GetMapping("/payment-result")
    public void executeAfterPayment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Transaction transaction = transactionService.executeAfterPayment(request);
        response.sendRedirect(paymentRedirect);
    }

//    @PostMapping("/send-private-message/{id}")
//    public void sendPrivateMessage(@RequestBody ResponseMessage message, @PathVariable String id) {
//        logger.debug("SESSION ID:" + id);
//        webSocketUtil.sendPrivateMessage(message.getContent(), id.trim());
//    }


}
