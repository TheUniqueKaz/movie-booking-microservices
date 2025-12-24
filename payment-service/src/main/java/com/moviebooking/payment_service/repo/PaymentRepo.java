package com.moviebooking.payment_service.repo;

import com.moviebooking.payment_service.Entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID; // <--- Import UUID

public interface PaymentRepo extends JpaRepository<Payment, UUID> {
}