package com.example.spring_boot_base.repository;

import com.example.spring_boot_base.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}