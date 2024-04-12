package com.shop.dto;

import com.shop.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDto {

    public OrderItemDto(OrderItem orderitem, String imgUrl){
        this.itemNm = orderitem.getItem().getItemNm();
        this.count  = orderitem.getCount();
        this.orderPrice = orderitem.getOrderPrice();
        this.imgUrl = imgUrl;
    }

    private String itemNm; // 상품명
    private int count; // 주문 수량
    private int orderPrice; // 주문 금액
    private String imgUrl; // 상품 이미지 경로
}
