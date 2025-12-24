package com.moviebooking.payment_service.service;

import com.moviebooking.payment_service.dto.PaymentSuccessEvent;
import com.moviebooking.payment_service.dto.TicketCreatedEvent;
import com.moviebooking.payment_service.Entity.Payment;
import com.moviebooking.payment_service.repo.PaymentRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private final PaymentRepo paymentRepository;

    public void processPayment(TicketCreatedEvent event) {
        System.out.println("Đang xử lý thanh toán cho vé: " + event.getTicketId());

        try { Thread.sleep(1000); } catch (InterruptedException e) {}

        Payment payment = new Payment();
        payment.setTicketId(event.getTicketId());
        payment.setAmount(event.getAmount());

        payment.setTransactionId("VNPAY_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        paymentRepository.save(payment);

        System.out.println("✅ THANH TOÁN THÀNH CÔNG! Mã GD: " + payment.getTransactionId());

        PaymentSuccessEvent successEvent = new PaymentSuccessEvent(event.getTicketId(), "SUCCESS");

        String jsonMessage = objectMapper.writeValueAsString(successEvent);

        kafkaTemplate.send("payment-success-topic", jsonMessage);

        System.out.println("Đã báo tin lại cho Booking Service: " + jsonMessage);
    }
}