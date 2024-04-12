package com.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class OrderItem extends BaseEntity{

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item; // 하나의 상품은 여러 주문 상품으로 들어갈 수 있음. 따라서 주문 상품 기준 many to one

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order; // 하나의 주문에 여러개의 상품 가능. 따라서 주문 상품 기준 주문을 many to one
    // manytoone이랑 onetomany는 앞에 있는게 쓰여진곳 기준이라 생각하면 된다.
    // 즉, manytoone이면 현재 엔티티가 many라는 뜻(현재 엔티티가 여러개 있을 수 있다는 뜻)

    private int orderPrice; // 주문 가격
    private int count; // 수량

//    private LocalDateTime regTime;
//    private LocalDateTime updateTime;
    // BaseEntity를 통한 통합관리로 인한 삭제

    // 주문 상품 만드는 메서드 -> (살 아이템, 수량)이 파라미터로 온다.
    public static OrderItem createOrderItem(Item item, int count){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setCount(count);

        orderItem.setOrderPrice(item.getPrice());

        item.removeStock(count);
        return orderItem;
    }

    // 주문 금액 계산 메서드
    public int getTotalPrice(){
        return orderPrice*count;
    }

    // 주문 취소시에 주문했던 수량만큼 재고를 늘리는 메서드
    public void cancel(){
        this.getItem().addStock(count);
    }
}
