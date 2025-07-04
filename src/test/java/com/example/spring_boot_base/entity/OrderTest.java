package com.example.spring_boot_base.entity;

import com.example.spring_boot_base.constant.ItemSellStatus;
import com.example.spring_boot_base.dto.MemberFormDto;
import com.example.spring_boot_base.repository.CartRepository;
import com.example.spring_boot_base.repository.ItemRepository;
import com.example.spring_boot_base.repository.MemberRepository;
import com.example.spring_boot_base.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
@RequiredArgsConstructor
class OrderTest {

    @Autowired
    private final CartRepository cartRepository;

    @Autowired
    private final MemberRepository memberRepository;

    @Autowired
    private final ItemRepository itemRepository;

    @Autowired
    private final OrderRepository orderRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @PersistenceContext
    private EntityManager em;

    public Member createMember() {
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail("test@email.com");
        memberFormDto.setName("홍길동");
        memberFormDto.setAddress("서울시 마포구 합정동");
        memberFormDto.setPassword("1234");
        return Member.createMember(memberFormDto, passwordEncoder);
    }

    public Item createItem() {
        Item item = new Item();
        item.setItemName("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("상세설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        return item;
    }

    public Order createOrder() {
        Order order = new Order();
        for (int i = 0; i < 3; i++) {
            Item item = createItem();
            itemRepository.save(item);

            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrderPrice(1000);
            orderItem.setOrder(order);

            order.getOrderItems().add(orderItem);
        }
        return order;
    }

    @Test
    @DisplayName("장바구니-회원 엔티티 매핑 조회 테스트")
    public void findCartAndMemberTest() {
        Member member = createMember();
        memberRepository.save(member);

        Cart cart = new Cart();
        cart.setMember(member);
        cartRepository.save(cart);

        em.flush();
        em.clear();

        Cart savedCart = cartRepository.findById(cart.getId())
                .orElseThrow(EntityNotFoundException::new);

        assertEquals(member.getId(), savedCart.getMember().getId());
    }

    @Test
    @DisplayName("영속성 전이 테스트")
    public void cascadeTest() {
        Order order = createOrder();
        orderRepository.saveAndFlush(order);
        em.clear();

        Order savedOrder = orderRepository.findById(order.getId())
                .orElseThrow(EntityNotFoundException::new);

        assertEquals(3, savedOrder.getOrderItems().size());
    }

    @Test
    @DisplayName("고아객체 제거 테스트")
    public void orphanRemovalTest() {
        Member member = new Member();
        memberRepository.save(member);

        Order order = createOrder();
        order.setMember(member);
        orderRepository.save(order);

        order.getOrderItems().remove(0);
        em.flush();
    }
}
