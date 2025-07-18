package com.example.spring_boot_base.repository;

import com.example.spring_boot_base.entity.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("select o from Order o " +
            "where o.member.email = :email " +
            "order by o.orderDate desc"
    )
    List<Order> findOrders(@Param("email") String email, Pageable pageable);
    @Query("select count(o) from Order o " +
            "where o.member.email = :email"
    )
    Long countOrder(@Param("email") String email);

    @Query("SELECT o FROM Order o " +
            "JOIN FETCH o.orderItems oi " +
            "JOIN FETCH oi.item " +
            "WHERE o.id = :orderId")
    Optional<Order> findWithItemsById(@Param("orderId") Long orderId);
}