package com.shop.entity;


import com.shop.constant.ItemSellStatus;
import com.shop.dto.ItemFormDto;
import com.shop.exception.OutOfStockException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Entity
@Table(name = "item")
public class Item extends BaseEntity{

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; // 상품 코드

    @Column(nullable = false, length = 50)
    private String itemNm; //상품명

    @Column(name = "price", nullable = false)
    private int price; //가격

    @Column(nullable = false)
    private int stockNumber; // 재고수량

    @Lob
    @Column(nullable = false)
    private String itemDetail; // 상품 상세 설명

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus; // 상품 판매 상태

//    private LocalDateTime regTime; // 상품 등록 시간
//    private LocalDateTime updateTime; // 상품 수정 시간
    // BaseEntity를 통한 통합관리로 인한 삭제

    // item 업데이트하는 로직 -> 엔티티에 비즈니스 로직을 추가해서 재활용 가능
    public void updateItem(ItemFormDto itemFormDto){
        this.itemNm = itemFormDto.getItemNm();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus=itemFormDto.getItemSellStatus();
        this.stockNumber = itemFormDto.getStockNumber();
        this.price = itemFormDto.getPrice();
    }

    // 상품 수량 감소 로직
    public void removeStock(int stockNumber){
        int restStock = this.stockNumber - stockNumber;
        if(restStock < 0){
            throw new OutOfStockException("상품의 재고가 부족합니다. (현재 재고 수량: " + this.stockNumber + ")");
        }
        this.stockNumber = restStock;
    }
}

