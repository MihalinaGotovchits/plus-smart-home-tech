package ru.practicum.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewProductInWarehouseRequest {
    @NotNull
    private UUID productId;

    private boolean fragile;

    @NotNull
    private DimensionDto dimension;

    @NotNull
    @Min(value = 1)
    private Double weight;
}
