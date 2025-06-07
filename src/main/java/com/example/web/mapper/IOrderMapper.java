package com.example.web.mapper;

import com.example.web.dto.request.OrderRequest;

import com.example.web.dto.response.OrderResponse;

import com.example.web.entity.Order;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IOrderMapper {
    @Mapping(source = "user.username",target = "username")
    @Mapping(source = "paymentMethod.name",target = "paymentMethod")
    @Mapping(source = "note", target = "note")
    @Mapping(source = "orderDate", target = "orderDate")
    @Mapping(source = "totalPrice", target = "totalPrice")
    @Mapping(source = "totalQuantity", target = "totalQuantity")
    @Mapping(source = "shippingFee", target = "shippingFee")
    @Mapping(source = "status", target = "status")
    OrderResponse toOrderResponse(Order order);

    Order toOrder(OrderRequest request);
}


