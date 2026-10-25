package com.aeroflex.paymentservice.service;

import com.aeroflex.common.enums.PaymentStatus;
import com.aeroflex.common.exception.BusinessException;
import com.aeroflex.common.exception.ResourceNotFoundException;
import com.aeroflex.paymentservice.dto.PaymentRequest;
import com.aeroflex.paymentservice.dto.PaymentResponse;
import com.aeroflex.paymentservice.dto.RefundRequest;
import com.aeroflex.paymentservice.dto.RefundResponse;
import com.aeroflex.paymentservice.entity.Payment;
import com.aeroflex.paymentservice.entity.Refund;
import com.aeroflex.paymentservice.repository.PaymentRepository;
import com.aeroflex.paymentservice.repository.RefundRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final RefundRepository refundRepository;

    @Override
    @Transactional
    public PaymentResponse processPayment(PaymentRequest request) {
        // Idempotency check: if an identical request was already processed, return existing payment
        Optional<Payment> existingPayment = paymentRepository.findByIdempotencyKey(request.getIdempotencyKey());
        if (existingPayment.isPresent()) {
            log.info("Idempotent request received. Returning existing payment: {}", existingPayment.get().getId());
            return mapToResponse(existingPayment.get());
        }

        // Check if booking already paid
        Optional<Payment> bookingPayment = paymentRepository.findByBookingId(request.getBookingId());
        if (bookingPayment.isPresent() && bookingPayment.get().getStatus() == PaymentStatus.SUCCESS) {
            log.info("Booking {} already paid with payment {}", request.getBookingId(), bookingPayment.get().getId());
            return mapToResponse(bookingPayment.get());
        }

        String txnRef = "TXN-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();

        Payment payment = Payment.builder()
                .bookingId(request.getBookingId())
                .userId(request.getUserId())
                .amount(request.getAmount())
                .currency(request.getCurrency() != null ? request.getCurrency().toUpperCase() : "USD")
                .paymentMethod(request.getPaymentMethod())
                .status(PaymentStatus.SUCCESS)
                .transactionReference(txnRef)
                .idempotencyKey(request.getIdempotencyKey())
                .provider("AeroPay")
                .paymentDetails(maskPaymentDetails(request.getPaymentDetails()))
                .paidAt(LocalDateTime.now())
                .build();

        Payment saved = paymentRepository.save(payment);
        log.info("Processed payment {} for booking {} with txnRef {}", saved.getId(), saved.getBookingId(), saved.getTransactionReference());

        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentById(UUID id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));
        return mapToResponse(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentByBookingId(UUID bookingId) {
        Payment payment = paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for booking id: " + bookingId));
        return mapToResponse(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponse> getPaymentsByUserId(UUID userId) {
        return paymentRepository.findByUserId(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RefundResponse processRefund(RefundRequest request) {
        Payment payment = paymentRepository.findById(request.getPaymentId())
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + request.getPaymentId()));

        if (payment.getStatus() == PaymentStatus.REFUNDED) {
            throw new BusinessException("Payment has already been refunded");
        }

        if (payment.getStatus() != PaymentStatus.SUCCESS) {
            throw new BusinessException("Only successful payments can be refunded");
        }

        String refRef = "REF-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();

        Refund refund = Refund.builder()
                .paymentId(payment.getId())
                .bookingId(request.getBookingId())
                .amount(request.getAmount())
                .currency(payment.getCurrency())
                .reason(request.getReason())
                .status(PaymentStatus.SUCCESS)
                .refundReference(refRef)
                .refundedAt(LocalDateTime.now())
                .build();

        Refund savedRefund = refundRepository.save(refund);

        payment.setStatus(PaymentStatus.REFUNDED);
        paymentRepository.save(payment);

        log.info("Processed refund {} of {} {} for payment {}", savedRefund.getId(), savedRefund.getAmount(), savedRefund.getCurrency(), payment.getId());

        return RefundResponse.builder()
                .id(savedRefund.getId())
                .paymentId(savedRefund.getPaymentId())
                .bookingId(savedRefund.getBookingId())
                .amount(savedRefund.getAmount())
                .currency(savedRefund.getCurrency())
                .reason(savedRefund.getReason())
                .status(savedRefund.getStatus())
                .refundReference(savedRefund.getRefundReference())
                .refundedAt(savedRefund.getRefundedAt())
                .createdAt(savedRefund.getCreatedAt())
                .build();
    }

    private String maskPaymentDetails(String details) {
        if (details == null || details.length() < 4) {
            return "N/A";
        }
        return "****-****-****-" + details.substring(details.length() - 4);
    }

    private PaymentResponse mapToResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .bookingId(payment.getBookingId())
                .userId(payment.getUserId())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .paymentMethod(payment.getPaymentMethod())
                .status(payment.getStatus())
                .transactionReference(payment.getTransactionReference())
                .idempotencyKey(payment.getIdempotencyKey())
                .provider(payment.getProvider())
                .paymentDetails(payment.getPaymentDetails())
                .failureReason(payment.getFailureReason())
                .paidAt(payment.getPaidAt())
                .createdAt(payment.getCreatedAt())
                .build();
    }
}
