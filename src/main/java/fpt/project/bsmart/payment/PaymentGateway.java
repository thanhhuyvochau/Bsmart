package fpt.project.bsmart.payment;

import fpt.project.bsmart.entity.Order;
import fpt.project.bsmart.entity.Transaction;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

public interface PaymentGateway<T> {
    PaymentResponse<T> pay(Order order) throws UnsupportedEncodingException;
}
