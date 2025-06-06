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
public class AddProductToWareHouseRequest {
    @NotNull
    private UUID productId;

    @NotNull
    @Min(value = 1)
    private Long quantity;
}
