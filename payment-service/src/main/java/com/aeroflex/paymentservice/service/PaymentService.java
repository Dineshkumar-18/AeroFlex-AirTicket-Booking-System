package com.aeroflex.paymentservice.service;

import com.aeroflex.paymentservice.dto.PaymentRequest;
import com.aeroflex.paymentservice.dto.PaymentResponse;
import com.aeroflex.paymentservice.dto.RefundRequest;
import com.aeroflex.paymentservice.dto.RefundResponse;

import java.util.List;
import java.util.UUID;

public interface PaymentService {
    PaymentResponse processPayment(PaymentRequest request);
    PaymentResponse getPaymentById(UUID id);
    PaymentResponse getPaymentByBookingId(UUID bookingId);
    List<PaymentResponse> getPaymentsByUserId(UUID userId);
    RefundResponse processRefund(RefundRequest request);
}
