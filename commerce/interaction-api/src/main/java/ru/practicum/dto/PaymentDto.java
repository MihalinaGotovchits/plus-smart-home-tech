package ru.practicum.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {
    @NotNull
    private UUID paymentId;
    private Double totalPayment;
    private Double DeliveryTotal;
    private Double feetTotal;
}
