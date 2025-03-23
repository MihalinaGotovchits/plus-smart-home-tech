package ru.practicum.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "warehouse_product")
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseProduct {
    @Id
    @Column(name = "product_id")
    private UUID productId;
    private boolean fragile;
    private Double width;
    private Double height;
    private Double depth;
    private Double weight;
    private long quantity = 0L;
}