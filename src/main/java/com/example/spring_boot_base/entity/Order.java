package com.example.spring_boot_base.entity;

import com.example.spring_boot_base.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "t_order")
@Getter
@Setter
public class Order {
    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 한 명의 회원은 여러 번 주문을 할 수 있음. 주문 엔티티 기준 다대일 단방향 매핑
    private LocalDateTime orderDate; //주문일
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; //주문상태
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();
    private LocalDateTime regTime;
    private LocalDateTime updateTime;
}