package com.example.spring_boot_base.entity;

import com.example.spring_boot_base.dto.MemberFormDto;
import com.example.spring_boot_base.repository.CartRepository;
import com.example.spring_boot_base.repository.MemberRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
@RequiredArgsConstructor
class CartTest {

    @Autowired
    private final CartRepository cartRepository;

    @Autowired
    private final MemberRepository memberRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @PersistenceContext
    private EntityManager em;

    /**
     * 테스트용 회원 엔티티 생성 메서드
     */
    public Member createMember() {
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail("test@email.com");
        memberFormDto.setName("홍길동");
        memberFormDto.setAddress("서울시 마포구 합정동");
        memberFormDto.setPassword("1234");
        return Member.createMember(memberFormDto, passwordEncoder);
    }

    @Test
    @DisplayName("장바구니-회원 엔티티 매핑 조회 테스트")
    public void findCartAndMemberTest() {
        // given
        Member member = createMember();
        memberRepository.save(member);

        Cart cart = new Cart();
        cart.setMember(member);
        cartRepository.save(cart);

        // when
        em.flush();
        em.clear();

        Cart savedCart = cartRepository.findById(cart.getId())
                .orElseThrow(EntityNotFoundException::new);

        // then
        assertEquals(member.getId(), savedCart.getMember().getId());
    }
}
