package ru.practicum.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryDto {
    private UUID deliveryId;
    @NotNull
    private AddressDto fromAddress;
    @NotNull
    private AddressDto toAddress;
    @NotNull
    private UUID orderId;
    @NotNull
    private DeliveryState deliveryState;
}
