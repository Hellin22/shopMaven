package com.shop.entity;

import com.shop.dto.MemberFormDto;
import com.shop.repository.CartRepository;
import com.shop.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations="classpath:application-test.properties")
class CartTest {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PersistenceContext
    EntityManager em;

    public Member createMember(){
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail("test@email.com");
        memberFormDto.setName("홍길동");
        memberFormDto.setAddress("서울시 마포구 합정동");
        memberFormDto.setPassword("1234");
        return Member.createMember(memberFormDto, passwordEncoder);
    }

    @Test
    @DisplayName("장바구니 회원 엔티티 매핑 조회 테스트")
    public void findCartAndMemberTest(){
        Member member = createMember(); // Member 자체가 엔티티이다. (나중에 혼자할때에는 MemberEntity로 작성하는게 이해하기 쉽겠다)
        memberRepository.save(member);

        Cart cart = new Cart();
        cart.setMember(member);
        cartRepository.save(cart);

        em.flush(); // JPA의 특성(영속성 컨텍스트에 데이터 저장 후 트랜잭션이 끝날 때 flush()를 호출해서 db에 반영
                    // , 엔티티를 영속성컨텍스트에 저장하고 entity manager로부터 강제로 flush()를 해서 db에 반영

        em.clear(); // JPA의 특성(영속성 컨텍스트에서 엔티티 조회 후 없을때 db를 조회함
                    // 실제 db에서 장바구니 엔티티를 가져올 때 회원 엔티티도 가져오는지 보기위해 영속성 컨텍스트를 비워줌.

        Cart savedCart = cartRepository.findById(cart.getId())
                .orElseThrow(EntityNotFoundException::new); // 저장된 장바구니 엔티티 확인
        assertEquals(savedCart.getMember().getId(), member.getId()); // 장바구니의 멤버 아이디와 실제로 저장했던 멤버 아이디 비교
    }
}