package com.example.spring_boot_base.service;

import com.example.spring_boot_base.constant.ItemSellStatus;
import com.example.spring_boot_base.constant.OrderStatus;
import com.example.spring_boot_base.dto.OrderDto;
import com.example.spring_boot_base.entity.Item;
import com.example.spring_boot_base.entity.Member;
import com.example.spring_boot_base.entity.Order;
import com.example.spring_boot_base.entity.OrderItem;
import com.example.spring_boot_base.repository.ItemRepository;
import com.example.spring_boot_base.repository.MemberRepository;
import com.example.spring_boot_base.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class OrderServiceTest {
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    public Item saveItem() {
        Item item = new Item();
        item.setItemName("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        return itemRepository.save(item);
    }

    public Member saveMember() {
        Member member = new Member();
        member.setEmail("test@test.com");
        return memberRepository.save(member);
    }


}
