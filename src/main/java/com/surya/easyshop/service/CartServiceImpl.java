package com.surya.easyshop.service;

import com.surya.easyshop.exception.ResourceNotFoundException;
import com.surya.easyshop.model.Cart;
import com.surya.easyshop.model.User;
import com.surya.easyshop.repository.CartItemRepository;
import com.surya.easyshop.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;


@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService{

    private final CartRepository cartRepository;

    private final CartItemRepository cartItemRepository;

    private final AtomicLong cartIdGenerator = new AtomicLong(0);

    @Override
    public Cart getCart(Long id) {
        Cart cart =  cartRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Cart Not Found"));
        BigDecimal totalAmount = cart.getTotalAmount();

        cart.setTotalAmount(totalAmount);

        return cartRepository.save(cart);
    }

    @Transactional
    @Override
    public void clearCart(Long id) {
        Cart cart = getCart(id);
        cartItemRepository.deleteAllByCartId(id);
        cart.clearCart();
        cartRepository.deleteById(id);


    }


    @Override
    public BigDecimal getTotalPrice(Long id) {
        Cart cart = getCart(id);
        return cart.getItems().stream()
                .map(c -> c.getTotalPrice())
                    .reduce(BigDecimal.ZERO, (a,b)-> a.add(b));
    }

    @Override
    public Cart initializeNewCart(User user)
    {
        return Optional.ofNullable(getCartByUserId(user.getId()))
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUser(user);
                    return cartRepository.save(cart);
                });
    }

    @Override
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }
}
