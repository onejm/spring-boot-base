package com.example.spring_boot_base.repository;

import com.example.spring_boot_base.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart,Long> {
    Cart findByMemberId(Long memberId);
}
