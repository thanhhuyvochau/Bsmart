package fpt.project.bsmart.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import fpt.project.bsmart.entity.Transaction;
import fpt.project.bsmart.entity.request.VpnPayRequest;
import fpt.project.bsmart.entity.response.PaymentResponse;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

public interface ITransactionService {
    PaymentResponse startPayment(HttpServletRequest req, VpnPayRequest request) throws UnsupportedEncodingException;

    Transaction executeAfterPayment(HttpServletRequest request) throws JsonProcessingException;
}
