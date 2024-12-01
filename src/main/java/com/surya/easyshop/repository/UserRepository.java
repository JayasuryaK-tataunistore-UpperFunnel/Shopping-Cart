package com.surya.easyshop.repository;

import com.surya.easyshop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User , Long> {
    boolean existsByEmail(String email);

    User findByEmail(String email);
}
