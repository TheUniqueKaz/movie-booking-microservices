package com.moviebooking.notification_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    public void sendSuccessEmail(String toEmail, String ticketId) {
        System.out.println("Gửi email tới: " + toEmail);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("VÉ CỦA BẠN ĐÃ ĐƯỢC ĐẶT THÀNH CÔNG!");
            message.setText("Xin chào,\n\n" +
                    "Chúc mừng bạn đã thanh toán thành công.\n" +
                    "Mã vé của bạn là: " + ticketId + "\n\n" +
                    "Chúc bạn xem phim vui vẻ!\n" +
                    " ");

            javaMailSender.send(message);
            System.out.println("Thành công!");

        } catch (Exception e) {
            System.err.println("Lỗi gửi email: " + e.getMessage());
        }
    }
}