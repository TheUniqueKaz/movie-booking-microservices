package com.moviebooking.bookingservice.features.BookTicket;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepo ticketRepository;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final ObjectMapper objectMapper;

    @Transactional
    @SneakyThrows
    public String bookTicket(String seatNumber, Long showtimeId, Long userId) {

        //  Check if the seat is booked ?
        boolean isTaken = ticketRepository.existsBySeatNumberAndShowtimeIdAndStatusNot(
                seatNumber, showtimeId, TicketStatus.CANCELLED
        );

        if (isTaken) {
            throw new RuntimeException("Ghế " + seatNumber + " đã có người đặt.");
        }

        // Create new Ticket
        Ticket ticket = new Ticket();
        ticket.setSeatNumber(seatNumber);
        ticket.setShowtimeId(showtimeId);
        ticket.setUserId(userId);
        ticket.setPrice(100000.0);
        ticket.setStatus(TicketStatus.PENDING); // Set Pending status

        // Save DATABASE
        ticketRepository.save(ticket);

        // KAFKA
        TicketCreatedEvent event = new TicketCreatedEvent(
                ticket.getId().toString(),
                userId,
                ticket.getPrice(),
                seatNumber
        );
        String eventAsJson = objectMapper.writeValueAsString(event);

        kafkaTemplate.send("ticket-created-topic", eventAsJson);

        System.out.println("✅ Đã bắn sự kiện sang Kafka: " + eventAsJson);

        return ticket.getId().toString();
    }

    public void confirmTicket(PaymentSuccessEvent event) {
        System.out.println("🔄 Đang cập nhật trạng thái vé: " + event.getTicketId());


        UUID id = UUID.fromString(event.getTicketId());

        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy vé!"));

        if ("SUCCESS".equals(event.getStatus())) {
            ticket.setStatus(TicketStatus.CONFIRMED);
            ticketRepository.save(ticket);
            System.out.println("✅ Vé đã được xác nhận! ID: " + ticket.getId());
        }
    }
}