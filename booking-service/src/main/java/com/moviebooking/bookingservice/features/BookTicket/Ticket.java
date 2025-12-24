package com.moviebooking.bookingservice.features.BookTicket;

import com.moviebooking.bookingservice.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tickets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ticket extends BaseEntity {

    private Long userId;
    private Long showtimeId;
    private String seatNumber;
    private Double price;

    @Enumerated(EnumType.STRING)
    private TicketStatus status;
}