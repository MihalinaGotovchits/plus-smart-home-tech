package ru.practicum.service;

import ru.practicum.dto.Pageable;
import ru.practicum.dto.ProductCategory;
import ru.practicum.dto.ProductDto;
import ru.practicum.dto.SetProductQuantityStateRequest;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface ShoppingStoreService {
    List<ProductDto> getProducts(ProductCategory productCategory, Pageable pageable);

    ProductDto getProductById(UUID productId);

    ProductDto addProduct(ProductDto productDto);

    ProductDto updateProduct(ProductDto productDto);

    boolean setQuantityState(SetProductQuantityStateRequest request);

    boolean deleteProduct(UUID productId);

    List<ProductDto> getProductByIds(Collection<UUID> ids);
}
