package ru.practicum.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.dto.OrderState;

import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_id")
    private UUID orderId;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "cart_id")
    private UUID cartId;
    @ElementCollection
    @CollectionTable(name = "order_products", joinColumns = @JoinColumn(name = "order_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    private Map<UUID, Integer> products;
    @Column(name = "payment_id")
    private UUID paymentId;
    @Column(name = "delivery_id")
    private UUID deliveryId;
    @Enumerated(value = EnumType.STRING)
    private OrderState state;
    @Column(name = "delivery_weight")
    private Double deliveryWeight;
    @Column(name = "delivery_volume")
    private Double deliveryVolume;
    private boolean fragile;
    @Column(name = "total_price")
    private Double totalPrice;
    @Column(name = "delivery_price")
    private Double deliveryPrice;
    @Column(name = "product_price")
    private Double productPrice;
}
