package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.client.OrderClient;
import ru.practicum.client.ShoppingStoreClient;
import ru.practicum.dto.OrderDto;
import ru.practicum.dto.PaymentDto;
import ru.practicum.dto.ProductDto;
import ru.practicum.exception.NoPaymentFoundException;
import ru.practicum.exception.NotEnoughInfoInOrderToCalculateException;
import ru.practicum.mapper.PaymentMapper;
import ru.practicum.model.Payment;
import ru.practicum.model.PaymentState;
import ru.practicum.repository.PaymentRepository;
import ru.practicum.utils.PaymentUtil;

import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    private final ShoppingStoreClient shoppingStoreClient;
    private final OrderClient orderClient;

    @Override
    @Transactional
    public PaymentDto createPayment(OrderDto orderDto) {
        Payment savedPayment = paymentRepository.save(getNewPayment(orderDto));
        return paymentMapper.toPaymentDto(savedPayment);
    }

    @Override
    public Double getTotalCost(OrderDto orderDto) {
        return calcTotalCost(orderDto);
    }

    @Override
    @Transactional
    public void paymentSuccess(UUID orderId) {
        updatePaymentState(orderId, PaymentState.SUCCESS);
        orderClient.successPayOrder(orderId);
    }

    @Override
    public Double getProductCost(OrderDto orderDto) {
        return calcProductsCost(orderDto);
    }

    @Override
    @Transactional
    public void paymentFailed(UUID orderId) {
        updatePaymentState(orderId, PaymentState.FAILED);
        orderClient.failPayOrder(orderId);
    }

    private Payment updatePaymentState(UUID orderId, PaymentState state) {
        Payment payment = getPaymentByOrderId(orderId);
        payment.setState(state);
        return paymentRepository.save(payment);
    }

    private Payment getPaymentByOrderId(UUID orderId) {
        return paymentRepository.findByOrderId(orderId).orElseThrow(
                () -> new NoPaymentFoundException("Указанная оплата не найдена")
        );
    }

    private Payment getNewPayment(OrderDto orderDto) {
        double fee = calcFeeCost(orderDto.getProductPrice());
        return Payment.builder()
                .orderId(orderDto.getOrderId())
                .state(PaymentState.PENDING)
                .totalPayment(orderDto.getTotalPrice())
                .deliveryTotal(orderDto.getDeliveryPrice())
                .feeTotal(fee)
                .build();
    }

    private double calcTotalCost(OrderDto orderDto) {
        double productCost = calcProductsCost(orderDto);
        double deliveryCost = orderDto.getDeliveryPrice();

        return productCost + calcFeeCost(productCost) + deliveryCost;
    }

    private double calcProductsCost(OrderDto orderDto) {
        double cost = 0;

        Map<UUID, ProductDto> products = shoppingStoreClient.getProductByIds(orderDto.getProducts().keySet())
                .stream()
                .collect(Collectors.toMap(ProductDto::getProductId, Function.identity()));
        for (Map.Entry<UUID, Integer> orderProduct : orderDto.getProducts().entrySet()) {
            int quantity = orderProduct.getValue();
            if (!products.containsKey(orderProduct.getKey())) {
                throw new NotEnoughInfoInOrderToCalculateException("Недостаточно информации в заказе для расчета");
            }
            double price = products.get(orderProduct.getKey()).getPrice();
            cost += price * quantity;
        }
        return cost;
    }

    private double calcFeeCost(double cost) {
        return cost * PaymentUtil.BASE_VAT_RATE;
    }
}
