package ru.practicum.service;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.client.WarehouseClient;
import ru.practicum.dto.BookedProductsDto;
import ru.practicum.dto.ChangeProductQuantityRequest;
import ru.practicum.dto.ShoppingCartDto;
import ru.practicum.exception.NoProductsInShoppingCartException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.ShoppingCartMapper;
import ru.practicum.model.ShoppingCart;
import ru.practicum.repository.ShoppingCartRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper cartMapper;
    private final WarehouseClient warehouseClient;
    @Override
    public ShoppingCartDto getShoppingCart(String userName) {
        return cartMapper.toShoppingCartDto(getShoppingCartByUsername(userName));
    }

    @Override
    @Transactional
    public ShoppingCartDto addProductsToShoppingCart(String userName, Map<UUID, @NotNull Long> products) {
        ShoppingCart cart = shoppingCartRepository.findByUserName(userName).orElse(getNewShoppingCart(userName)
        );
        Map<UUID, Long> cartProducts = new HashMap<>();
        if (cart.getProducts() != null && !cart.getProducts().isEmpty()) {
            cartProducts.putAll(cart.getProducts());
        }
        cartProducts.putAll(products);
        cart.setProducts(cartProducts);
        ShoppingCart savedCart = shoppingCartRepository.save(cart);
        return cartMapper.toShoppingCartDto(savedCart);
    }

    @Override
    @Transactional
    public ShoppingCartDto removeFromShoppingCart(String userName, List<UUID> products) {
        ShoppingCart cart = getShoppingCartByUsername(userName);
        products.forEach(productId -> cart.getProducts().remove(productId));
        ShoppingCart savedCart = shoppingCartRepository.save(cart);
        return cartMapper.toShoppingCartDto(savedCart);
    }

    @Override
    @Transactional
    public ShoppingCartDto changeProductQuantity(String userName, ChangeProductQuantityRequest request) {
        ShoppingCart cart = getShoppingCartByUsername(userName);
        if (!cart.getProducts().containsKey(request.getProductId())) {
            throw new NotFoundException("Данных товаров в корзине не найдено");
        }
        cart.getProducts().put(request.getProductId(), request.getNewQuantity());
        ShoppingCart savedCart = shoppingCartRepository.save(cart);
        return cartMapper.toShoppingCartDto(savedCart);
    }

    @Override
    @Transactional
    public BookedProductsDto bookingProductsFromShoppingCart(String userName) {
        ShoppingCartDto cartDto = cartMapper.toShoppingCartDto(getShoppingCartByUsername(userName));
        return warehouseClient.checkProductQuantityEnoughForShoppingCart(cartDto);
    }

    @Override
    @Transactional
    public void deleteShoppingCart(String userName) {
        shoppingCartRepository.deleteByUserName(userName);

    }

    private ShoppingCart getShoppingCartByUsername(String userName) {
        return shoppingCartRepository.findByUserName(userName).orElseThrow(
                () -> new NoProductsInShoppingCartException("Корзина данного покупателя не найдена")
        ) ;
    }

    private ShoppingCart getNewShoppingCart(String userName) {
        ShoppingCart cart = new ShoppingCart();
        cart.setUserName(userName);
        return cart;
    }
}
