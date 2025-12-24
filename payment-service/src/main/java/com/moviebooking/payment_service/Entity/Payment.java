package com.moviebooking.payment_service.Entity;

import com.moviebooking.payment_service.common.BaseEntity; // <--- Import cái mới
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment extends BaseEntity {

    private String ticketId;
    private Double amount;
    private String transactionId;

}