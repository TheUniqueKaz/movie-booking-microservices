package com.moviebooking.payment_service.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketCreatedEvent {
    private String ticketId;
    private Long userId;
    private Double amount;
    private String seatNumber;
}