package com.surya.easyshop.controller;


import com.surya.easyshop.dto.OrderDto;
import com.surya.easyshop.exception.ResourceNotFoundException;
import com.surya.easyshop.model.Cart;
import com.surya.easyshop.model.Order;
import com.surya.easyshop.model.User;
import com.surya.easyshop.repository.CartRepository;
import com.surya.easyshop.response.ApiResponse;
import com.surya.easyshop.service.CartService;
import com.surya.easyshop.service.OrderService;
import com.surya.easyshop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/order")
        public ResponseEntity<ApiResponse> createOrder(@RequestParam Long userId)
    {
        try {
            Order order =orderService.placeOrder(userId);
            OrderDto orderDto = orderService.convertToDto(order);

            return ResponseEntity.ok(new ApiResponse("Item OrderSuccess!" , orderDto));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage() , e.getCause()));
        }
    }

    @GetMapping("/{orderId}/order")
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long orderId){
        try {
            OrderDto orderDto = orderService.getOrder(orderId);
            return ResponseEntity.ok(new ApiResponse(" Order Found!" , orderDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Error: ",e.getMessage() ));
        }

    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<ApiResponse> getUserOrders(@PathVariable Long userId){
        try {
            List<OrderDto> orderDtos = orderService.getUserOrders(userId);
            return ResponseEntity.ok(new ApiResponse(" Order Found!" , orderDtos));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Error: ",e.getMessage() ));
        }

    }

}
