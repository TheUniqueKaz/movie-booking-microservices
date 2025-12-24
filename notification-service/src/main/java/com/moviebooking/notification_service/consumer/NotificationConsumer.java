package com.moviebooking.notification_service.consumer;



import com.moviebooking.notification_service.dto.PaymentSuccessEvent;
import com.moviebooking.notification_service.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private final EmailService emailService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "payment-success-topic", groupId = "notification-group")
    public void handlePaymentSuccess(String message) {
        System.out.println("📩 [NOTI-SERVICE] Nhận tin nhắn: " + message);

        try {
            PaymentSuccessEvent event = objectMapper.readValue(message, PaymentSuccessEvent.class);


            String myEmail = "nguyenvandc.31@gmail.com";

            emailService.sendSuccessEmail(myEmail, event.getTicketId());

        } catch (Exception e) {
            System.err.println("❌ Lỗi xử lý thông báo: " + e.getMessage());
        }
    }
}