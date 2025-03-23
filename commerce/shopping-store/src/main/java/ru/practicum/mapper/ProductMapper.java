package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.dto.ProductDto;
import ru.practicum.model.Product;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapper {

    Product toProduct(ProductDto dto);

    ProductDto toProductDto(Product product);

    @Mapping(target = "productId", ignore = true)
    void update(ProductDto productDto, @MappingTarget Product product);
}
