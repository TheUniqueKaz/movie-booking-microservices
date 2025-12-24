package com.moviebooking.bookingservice.features.BookTicket;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

record BookTicketRequest(String seatNumber, Long showtimeId, Long userId) {}

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    public String createBooking(@RequestBody BookTicketRequest request) {
        return ticketService.bookTicket(
                request.seatNumber(),
                request.showtimeId(),
                request.userId()
        );
    }
}