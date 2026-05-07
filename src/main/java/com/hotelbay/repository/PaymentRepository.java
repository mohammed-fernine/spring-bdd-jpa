package com.hotelbay.repository;

import com.hotelbay.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByReservationId(Long reservationId);
    List<Payment> findByStatus(Payment.PaymentStatus status);
    List<Payment> findByReservationIdAndStatus(Long reservationId, Payment.PaymentStatus status);
}
