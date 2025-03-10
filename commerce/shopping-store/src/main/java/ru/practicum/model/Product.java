package ru.practicum.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.dto.ProductCategory;
import ru.practicum.dto.ProductState;
import ru.practicum.dto.QuantityState;

import java.util.UUID;

@Entity
@Table(name = "products")
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "name")
    private String productName;

    private String description;

    private String imageSrc;

    @Enumerated(value = EnumType.STRING)
    private QuantityState quantityState;

    @Enumerated(value = EnumType.STRING)
    private ProductState productState;

    private Double rating;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "category")
    private ProductCategory productCategory;

    private Double price;
}
