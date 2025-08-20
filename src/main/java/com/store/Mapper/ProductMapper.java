package com.store.Mapper;

import com.store.DTO.ProductDTO;
import com.store.Entity.Product;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "default", uses = {CategoryMapper.class})
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(source = "category", target = "category")
    ProductDTO toDTO(Product entity);

    Product toEntity(ProductDTO dto);

    List<ProductDTO> toDTO(List<Product> list);
}
