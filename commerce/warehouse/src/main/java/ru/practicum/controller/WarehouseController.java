package ru.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.client.WarehouseClient;
import ru.practicum.dto.*;
import ru.practicum.service.WarehouseService;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/warehouse")
@RequiredArgsConstructor
public class WarehouseController implements WarehouseClient {
    private final WarehouseService warehouseService;

    @Override
    public void newProductInWarehouse(NewProductInWarehouseRequest request) {
        warehouseService.newProductInWarehouse(request);
    }

    @PostMapping("/check")
    @Override
    public BookedProductsDto checkProductQuantityEnoughForShoppingCart(ShoppingCartDto shoppingCartDto) {
        return warehouseService.checkProductQuantityEnoughForShoppingCart(shoppingCartDto);
    }

    @Override
    @PostMapping("/add")
    public void addProductToWarehouse(AddProductToWareHouseRequest request) {
        warehouseService.addProductToWarehouse(request);
    }

    @Override
    @GetMapping("/address")
    public AddressDto getWarehouseAddress() {
        return warehouseService.getWarehouseAddress();
    }

    @Override
    @PostMapping("/shipped")
    public void shippedToDelivery(@RequestBody @Valid ShippedToDeliveryRequest request) {
        warehouseService.shippedToDelivery(request);
    }

    @Override
    @PostMapping("/return")
    public void acceptReturn(Map<UUID, Integer> products) {
        warehouseService.acceptReturn(products);
    }

    @Override
    @PostMapping("/assembly")
    public BookedProductsDto assemblyProductsForOrder(AssemblyProductsForRequest request) {
        return warehouseService.assemblyProductsForOrder(request);
    }
}