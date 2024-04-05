package com.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="cart")
@Getter
@Setter
@ToString
public class Cart {

    @Id
    @Column(name="cart_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne // -> @OneToOne 어노테이션을 통해 회원 엔티티와 일대일로 매핑한다.
    @JoinColumn(name="member_id") // @JoinColumn 어노테이션을 통해 매핑할 외래키 선택(name에는 매핑할 외래키 이름 /
                                // name을 사용 안하면 JPA가 자동으로 ID를 찾지만 컬럼명이 원하는대로 안될수도 있음.)
    private Member member;
}
