package com.moviebooking.bookingservice.features.BookTicket;


import com.moviebooking.bookingservice.features.BookTicket.PaymentSuccessEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class PaymentStatusListener {

    private final TicketService ticketService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "payment-success-topic", groupId = "booking-group")
    public void handlePaymentSuccess(String message) {
        try {
            // 1. Đọc tin nhắn JSON
            PaymentSuccessEvent event = objectMapper.readValue(message, PaymentSuccessEvent.class);

            // 2. Gọi Service cập nhật DB
            ticketService.confirmTicket(event);

        } catch (Exception e) {
            System.err.println("❌ Lỗi cập nhật vé: " + e.getMessage());
        }
    }
}