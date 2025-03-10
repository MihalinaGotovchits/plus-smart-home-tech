package ru.practicum.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartDto {
    private UUID shoppingCartId;

    @NotEmpty
    private Map<UUID, Integer> products;
}
