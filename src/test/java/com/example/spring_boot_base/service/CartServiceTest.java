package com.example.spring_boot_base.service;

import com.example.spring_boot_base.constant.ItemSellStatus;
import com.example.spring_boot_base.dto.CartItemDto;
import com.example.spring_boot_base.entity.CartItem;
import com.example.spring_boot_base.entity.Item;
import com.example.spring_boot_base.entity.Member;
import com.example.spring_boot_base.repository.CartItemRepository;
import com.example.spring_boot_base.repository.ItemRepository;
import com.example.spring_boot_base.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityNotFoundException;


import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@TestPropertySource(locations="classpath:application -test.properties")

class CartServiceTest {
    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    CartService cartService;

    @Autowired
    CartItemRepository cartItemRepository;

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

    @Test
    @DisplayName("장바구니 담기 테스트")

    public void addCart() {
        Item item = saveItem();
        Member member = saveMember();
        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setCount(5);
        cartItemDto.setItemId(item.getId());
        Long cartItemId = cartService.addCart(cartItemDto, member.getEmail());

        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        assertEquals(item.getId(), cartItem.getItem().getId());
        assertEquals(cartItemDto.getCount(), cartItem.getCount());
    }
}