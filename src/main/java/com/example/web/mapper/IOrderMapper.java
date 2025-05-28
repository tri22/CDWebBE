package com.example.web.mapper;

import com.example.web.dto.request.OrderRequest;

import com.example.web.dto.response.OrderResponse;

import com.example.web.entity.Order;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IOrderMapper {
    Order toOrder(OrderRequest req);

    OrderResponse toOrderResponse(Order order);
}
