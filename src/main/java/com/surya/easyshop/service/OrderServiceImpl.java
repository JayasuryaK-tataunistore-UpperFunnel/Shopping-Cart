package com.surya.easyshop.service;

import com.surya.easyshop.dto.OrderDto;
import com.surya.easyshop.enums.OrderStatus;
import com.surya.easyshop.exception.ResourceNotFoundException;
import com.surya.easyshop.model.*;
import com.surya.easyshop.repository.OrderRepository;
import com.surya.easyshop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    private final CartService cartService;
    private final ModelMapper modelMapper;

    @Transactional
    @Override
    public Order placeOrder(Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        Order order = createOrder(cart);
        List<OrderItem> orderItemList = createOrderItems(order , cart);

        order.setOrderItems(new HashSet<>(orderItemList));
        order.setTotalAmount(calculateTotalAmount(orderItemList));
        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(cart.getId());


        return savedOrder;
    }

    private Order createOrder(Cart cart )
    {
        Order order = new Order();
        //todo set user
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        return order;

    }

    private List<OrderItem> createOrderItems(Order order , Cart cart)
    {
        return cart.getItems().stream()
                .map(cartItem -> {
                    Product product = cartItem.getProduct();
                    product.setInventory(product.getInventory()- cartItem.getQuantity());
                    productRepository.save(product);
                    return new OrderItem(
                            order,
                            product,
                            cartItem.getQuantity(),
                            cartItem.getUnitPrice());
                }).collect(Collectors.toList());
    }




    private BigDecimal calculateTotalAmount(List<OrderItem> orderItemList){

        return orderItemList.stream().map(s -> s.getPrice().multiply(new BigDecimal(s.getQuantity()))).reduce(BigDecimal.ZERO , BigDecimal::add);
    }




    @Override
    public OrderDto getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .map(order -> convertToDto(order))
                .orElseThrow(()-> new ResourceNotFoundException("Order Not Found"));
    }

    @Override
    public List<OrderDto> getUserOrders(Long userId){
        List<Order> orders =  orderRepository.findByUserId(userId);
        return orders.stream()
                .map(order -> convertToDto(order))
                .collect(Collectors.toList());
    }

    private OrderDto convertToDto(Order order)
    {
        return modelMapper.map(order , OrderDto.class);
    }
}
