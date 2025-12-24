package com.moviebooking.bookingservice.features.BookTicket;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface TicketRepo extends JpaRepository<Ticket, UUID> {

    boolean existsBySeatNumberAndShowtimeIdAndStatusNot(
            String seatNumber,
            Long showtimeId,
            TicketStatus status
    );
}