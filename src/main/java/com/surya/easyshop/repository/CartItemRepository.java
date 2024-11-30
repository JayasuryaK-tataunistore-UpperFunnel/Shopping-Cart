package com.surya.easyshop.repository;

import com.surya.easyshop.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem ,Long> {
    void deleteAllByCartId(Long id);
}
