package com.surya.easyshop.service;

import com.surya.easyshop.model.CartItem;

public interface CartItemService {
    void addCartItem(Long cartId , Long productId , int quantity );

    void removeItemFromCart(Long cartId , Long productId);

    void updateItemQuantity(Long cartId , Long productId , int quantity);

    CartItem getCartItem(Long cartId, Long productId);
}
