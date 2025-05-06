package ru.practicum.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "payment")
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "payment_id")
    private UUID paymentId;
    @Column(name = "order_id")
    private UUID orderId;
    @Enumerated(value = EnumType.STRING)
    private PaymentState state;
    @Column(name = "total_cost")
    private Double totalPayment;
    @Column(name = "delivery_cost")
    private Double deliveryTotal;
    @Column(name = "fee_cost")
    private Double feeTotal;
}
