package ru.practicum.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.client.DeliveryClient;
import ru.practicum.client.PaymentClient;
import ru.practicum.client.ShoppingCartClient;
import ru.practicum.client.WarehouseClient;
import ru.practicum.dto.*;
import ru.practicum.mapper.OrderMapper;
import ru.practicum.model.Order;
import ru.practicum.service.OrderService;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderFacadeImpl implements OrderFacade {
    private final OrderService orderService;
    private final OrderMapper orderMapper;

    private final ShoppingCartClient shoppingCartClient;
    private final PaymentClient paymentClient;
    private final DeliveryClient deliveryClient;
    private final WarehouseClient warehouseClient;

    @Override
    public List<OrderDto> getClientOrders(String userName) {
        log.info("get orders for user {}", userName);
        return orderService.getClientOrders(userName)
                .stream()
                .map(orderMapper::toOrderDto)
                .toList();
    }

    @Override
    public OrderDto createNewOrder(CreateNewOrderRequest request) {
        Order order = orderService.createNewOrder(getNewOrderFromRequest(request));
        log.info("new order from request: {}", request);

        UUID deliveryId = getNewDeliveryId(order.getOrderId(), request.getDeliveryAddress());
        return orderMapper.toOrderDto(orderService.setDelivery(order.getOrderId(), deliveryId));
    }

    @Override
    public OrderDto returnProducts(ProductReturnRequest request) {
        warehouseClient.acceptReturn(request.getProducts());
        return orderMapper.toOrderDto(orderService.returnProducts(request.getOrderId()));
    }

    @Override
    public OrderDto payOrder(UUID orderId) {
        Order order = orderService.getOrderById(orderId);
        double productCost = paymentClient.getProductCost(orderMapper.toOrderDto(order));
        double deliveryCost = deliveryClient.deliveryCost(orderMapper.toOrderDto(order));
        order.setDeliveryPrice(deliveryCost);
        order.setProductPrice(productCost);
        log.info("order after setting productPrice: {}", order);
        double totalCost = paymentClient.getTotalCost(orderMapper.toOrderDto(order));
        order.setTotalPrice(totalCost);
        PaymentDto paymentDto = paymentClient.createPayment(orderMapper.toOrderDto(order));
        order.setPaymentId(paymentDto.getPaymentId());

        Order savedOrder = orderService.savePaymentInfo(order);
        log.info("payOrder: order after creating payment {}", savedOrder);
        return orderMapper.toOrderDto(savedOrder);
    }

    @Override
    public OrderDto successPayOrder(UUID orderId) {
        return orderMapper.toOrderDto(orderService.successPayOrder(orderId));
    }

    @Override
    public OrderDto failPayOrder(UUID orderId) {
        return orderMapper.toOrderDto(orderService.failPayOrder(orderId));
    }

    @Override
    public OrderDto deliverOrder(UUID orderId) {
        return orderMapper.toOrderDto(orderService.deliverOrder(orderId));
    }

    @Override
    public OrderDto failDeliverOrder(UUID orderId) {
        return orderMapper.toOrderDto(orderService.failDeliverOrder(orderId));
    }

    @Override
    public OrderDto completeOrder(UUID orderId) {
        return orderMapper.toOrderDto(orderService.completeOrder(orderId));
    }

    @Override
    public OrderDto calculateTotalPrice(UUID orderId) {
        Order order = orderService.getOrderById(orderId);
        double totalCost = paymentClient.getTotalCost(orderMapper.toOrderDto(order));
        return orderMapper.toOrderDto(orderService.setTotalPrice(orderId, totalCost));
    }

    @Override
    public OrderDto calculateDeliveryPrice(UUID orderId) {
        Order order = orderService.getOrderById(orderId);
        double deliveryCost = deliveryClient.deliveryCost(orderMapper.toOrderDto(order));
        return orderMapper.toOrderDto(orderService.setDeliveryPrice(orderId, deliveryCost));
    }

    @Override
    public OrderDto assemblyOrder(UUID orderId) {
        warehouseClient.assemblyProductsForOrder(getNewAssemblyProductsForOrderRequest(orderId));
        return orderMapper.toOrderDto(orderService.assemblyOrder(orderId));
    }

    @Override
    public OrderDto failAssemblyOrder(UUID orderId) {
        return orderMapper.toOrderDto(orderService.failAssemblyOrder(orderId));
    }

    @Override
    public OrderDto getOrderById(UUID orderId) {
        return orderMapper.toOrderDto(orderService.getOrderById(orderId));
    }

    private AssemblyProductsForRequest getNewAssemblyProductsForOrderRequest(UUID orderId) {
        Order order = orderService.getOrderById(orderId);
        AssemblyProductsForRequest request = new AssemblyProductsForRequest();
        request.setOrderId(orderId);
        request.setProducts(order.getProducts());
        return request;
    }

    private Order getNewOrderFromRequest(CreateNewOrderRequest request) {
        BookedProductsDto bookedProductsDto = shoppingCartClient.bookingProductsFromShoppingCart(request.getUserName());

        return Order.builder()
                .userName(request.getUserName())
                .cartId(request.getShoppingCart().getShoppingCartId())
                .products(request.getShoppingCart().getProducts())
                .deliveryWeight(bookedProductsDto.getDeliveryWeight())
                .deliveryVolume(bookedProductsDto.getDeliveryVolume())
                .fragile(bookedProductsDto.getFragile())
                .state(OrderState.NEW)
                .build();
    }

    private UUID getNewDeliveryId(UUID orderId, AddressDto deliveryAddress) {
        DeliveryDto deliveryDto = new DeliveryDto();
        deliveryDto.setFromAddress(warehouseClient.getWarehouseAddress());
        deliveryDto.setToAddress(deliveryAddress);
        deliveryDto.setOrderId(orderId);
        deliveryDto.setDeliveryState(DeliveryState.CREATED);
        log.info("new DeliveryDto: {}", deliveryDto);

        return deliveryClient.planDelivery(deliveryDto).getDeliveryId();
    }
}
