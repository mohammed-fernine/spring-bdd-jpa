package com.hotelbay.service;

import com.hotelbay.entity.Payment;
import com.hotelbay.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }

    public Optional<Payment> findById(Long id) {
        return paymentRepository.findById(id);
    }

    public List<Payment> findByReservationId(Long reservationId) {
        return paymentRepository.findByReservationId(reservationId);
    }

    public List<Payment> findByStatus(Payment.PaymentStatus status) {
        return paymentRepository.findByStatus(status);
    }

    public List<Payment> findByReservationAndStatus(Long reservationId, Payment.PaymentStatus status) {
        return paymentRepository.findByReservationIdAndStatus(reservationId, status);
    }

    public Payment save(Payment payment) {
        if (payment.getCreatedAt() == null) {
            payment.setCreatedAt(LocalDateTime.now());
        }
        payment.setUpdatedAt(LocalDateTime.now());
        return paymentRepository.save(payment);
    }

    public void deleteById(Long id) {
        paymentRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return paymentRepository.existsById(id);
    }

    public void deleteAll() {
        paymentRepository.deleteAll();
    }
}
