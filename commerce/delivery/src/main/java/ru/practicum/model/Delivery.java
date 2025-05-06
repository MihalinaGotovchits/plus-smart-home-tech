package ru.practicum.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.dto.DeliveryState;

import java.util.UUID;

@Entity
@Table(name = "delivery")
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "delivery_id")
    private UUID deliveryId;
    @Column(name = "order_id")
    private UUID order;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "from_address_id")
    private Address fromAddress;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "to_address_id")
    private Address toAddress;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "state")
    private DeliveryState deliveryState;
    private Double deliveryWeight;
    private Double deliveryVolume;
    private boolean fragile;
}
