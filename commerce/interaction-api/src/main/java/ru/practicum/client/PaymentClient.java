package ru.practicum.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.dto.OrderDto;
import ru.practicum.dto.PaymentDto;

import java.util.UUID;

@FeignClient(name = "payment", path = "/api/v1/payment")
public interface PaymentClient {

    @PostMapping
    PaymentDto createPayment(@RequestBody OrderDto orderDto);

    @PostMapping("/totalCost")
    Double getTotalCost(@RequestBody OrderDto orderDto);

    @PostMapping("/refund")
    void paymentSuccess(@RequestBody UUID orderId);

    @PostMapping("/productCost")
    Double getProductCost(@RequestBody OrderDto orderDto);

    @PostMapping("/failed")
    void paymentFailed(@RequestBody UUID orderId);
}
