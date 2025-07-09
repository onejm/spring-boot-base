package com.example.spring_boot_base.repository;

import com.example.spring_boot_base.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}