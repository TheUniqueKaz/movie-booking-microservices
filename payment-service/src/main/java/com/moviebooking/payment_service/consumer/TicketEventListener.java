package com.moviebooking.payment_service.consumer;

import tools.jackson.databind.ObjectMapper;
import com.moviebooking.payment_service.dto.TicketCreatedEvent;
import com.moviebooking.payment_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketEventListener {

    private final PaymentService paymentService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "ticket-created-topic", groupId = "payment-group")
    public void handleTicketCreated(String message) {
        System.out.println("📩 [NHẬN TIN NHẮN] Raw message: " + message);

        try {
            // 1. Dịch tin nhắn (String) -> Object (TicketCreatedEvent)
            TicketCreatedEvent event = objectMapper.readValue(message, TicketCreatedEvent.class);

            // 2. Gọi Service để xử lý thanh toán
            paymentService.processPayment(event);

        } catch (Exception e) {
            System.err.println("❌ Lỗi dịch dữ liệu JSON: " + e.getMessage());
        }
    }
}