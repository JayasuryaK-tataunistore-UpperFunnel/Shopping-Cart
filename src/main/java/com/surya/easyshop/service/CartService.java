package com.surya.easyshop.service;

import com.surya.easyshop.model.Cart;
import com.surya.easyshop.model.User;

import java.math.BigDecimal;

public interface CartService {
    Cart getCart(Long id);
    void clearCart(Long id);
    BigDecimal getTotalPrice(Long id);

    Cart initializeNewCart(User user);


    Cart getCartByUserId(Long userId);
}
