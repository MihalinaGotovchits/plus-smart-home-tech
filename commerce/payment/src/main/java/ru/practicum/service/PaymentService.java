package ru.practicum.service;

import ru.practicum.dto.OrderDto;
import ru.practicum.dto.PaymentDto;

import java.util.UUID;

public interface PaymentService {

    PaymentDto createPayment(OrderDto orderDto);

    Double getTotalCost(OrderDto orderDto);

    void paymentSuccess(UUID orderId);

    Double getProductCost(OrderDto orderDto);

    void paymentFailed(UUID orderId);
}
