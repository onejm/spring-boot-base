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
public class Order extends BaseEntity {
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

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem); // orderItem에는 주문 상품 정보들을 담아주고, orderItem 객체를 order객체의 orderItems에 추가
        orderItem.setOrder(this); // Order 엔티티와 OrderItem 엔티티가 양방향 참조 관계 이므로, ordreItem 객체에도 order 객체를 세팅
    }
    public static Order createOrder(Member member, List<OrderItem> orderItemList) {
        Order order = new Order();
        order.setMember(member);
        for(OrderItem orderItem : orderItemList) {
            order.addOrderItem(orderItem);
        }
        order.setOrderStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }
    public int getTotalPrice() {
        int totalPrice = 0;
        for(OrderItem orderItem : orderItems){
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

    public void cancelOrder() {
        this.orderStatus = OrderStatus.CANCEL;
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }
}