package com.example.spring_boot_base.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "t_cart")
@Getter @Setter
@ToString
public class Cart extends BaseEntity {
    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    // 매핑할 외래키 지정. 지정하지 않으면 JPA가 알아서 ID를 찾지만, 컬럼명이 원하는 대로 생성되지 않을 수 있음.
    @JoinColumn(name="member_id")
    private Member member;

    public static Cart createCart(Member member){
        Cart cart = new Cart();
        cart.setMember(member);
        return cart;
    }
}