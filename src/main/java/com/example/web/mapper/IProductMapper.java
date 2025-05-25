package com.example.web.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.example.web.dto.request.ProductRequest;
import com.example.web.dto.response.ProductResponse;
import com.example.web.entity.Product;

@Mapper(componentModel = "spring")
public interface IProductMapper {

    // Convert entity -> response DTO
    ProductResponse toProductResponse(Product product);

    List<ProductResponse> toProductResponseList(List<Product> products);

    // Convert request DTO -> entity
    Product toEntity(ProductRequest requestDTO);

    // Optional: partial update mapping
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProduct(@MappingTarget Product entity, ProductRequest dto);
}
