package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.dto.ShoppingCartDto;
import ru.practicum.model.ShoppingCart;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ShoppingCartMapper {

    @Mapping(target = "userName", source = "userName")
    ShoppingCart toShoppingCart(ShoppingCartDto shoppingCartDto,  String userName);

    @Mapping(target = "shoppingCartId", source = "cartId")
    ShoppingCartDto toShoppingCartDto(ShoppingCart shoppingCart);
}
