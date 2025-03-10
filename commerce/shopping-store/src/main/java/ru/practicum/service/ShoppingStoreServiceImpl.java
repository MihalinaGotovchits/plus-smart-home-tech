package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.*;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.ProductMapper;
import ru.practicum.model.Product;
import ru.practicum.repository.ProductRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShoppingStoreServiceImpl implements ShoppingStoreService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public List<ProductDto> getProducts(ProductCategory productCategory, Pageable pageable) {
        Sort sort = Sort.by(pageable.getSort().getFirst());
        PageRequest pageRequest = PageRequest.of(pageable.getPage(), pageable.getSize(), sort);
        return productRepository.findAllByProductCategory(productCategory, pageRequest)
                .stream()
                .map(productMapper::toProductDto)
                .toList();
    }

    @Override
    public ProductDto getProductById(UUID productId) {
        return productMapper.toProductDto(getProduct(productId));
    }

    @Override
    @Transactional
    public ProductDto addProduct(ProductDto productDto) {
        Product product = productMapper.toProduct(productDto);
        return productMapper.toProductDto(productRepository.save(product));
    }

    @Override
    @Transactional
    public ProductDto updateProduct(ProductDto productDto) {
        Product product = getProduct(productDto.getProductId());
        productMapper.update(productDto, product);
        return productMapper.toProductDto(productRepository.save(product));
    }

    @Override
    @Transactional
    public boolean setQuantityState(SetProductQuantityStateRequest request) {
        Product product = getProduct(request.getProductId());
        product.setQuantityState(request.getQuantityState());
        productRepository.save(product);
        return true;
    }

    @Override
    @Transactional
    public boolean deleteProduct(UUID productId) {
        Product product = getProduct(productId);
        product.setProductState(ProductState.DEACTIVATE);
        productRepository.save(product);
        return true;
    }

    private Product getProduct(UUID productId) {
        return productRepository.findById(productId).orElseThrow(
                () -> new NotFoundException("Товар с id " + productId + " не найден")
        );
    }
}
