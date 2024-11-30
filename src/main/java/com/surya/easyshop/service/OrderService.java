package com.surya.easyshop.service;

import com.surya.easyshop.dto.OrderDto;
import com.surya.easyshop.model.Order;

import java.util.List;

public interface OrderService {

    Order placeOrder(Long userId);

    OrderDto getOrder(Long orderId);

    List<OrderDto> getUserOrders(Long userId);
}
