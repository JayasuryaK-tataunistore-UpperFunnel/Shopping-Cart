package com.surya.easyshop.repository;

import com.surya.easyshop.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart , Long> {


    Cart findByUserId(Long userId);
}
