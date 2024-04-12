package com.shop.entity;

import com.shop.constant.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "orders") // 정렬시 사용되는 order이라는 키워드 때문에 orders로 변경
public class Order extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 한명의 회원은 여러번 주문 가능 -> 주문 엔티티 기준 다대일 매핑

    private LocalDateTime orderDate; // 주문일

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; // 주문 상태

    // OrderItem 생성 후 Order에 추가 (양방향 매핑이 된다~~)
    // orphanRemoval = true를 통해 해당 orderItems 안에 있는 orderItem들은 orders가 사라지면 고아객체가 되므로 같이 사라짐.
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL
                ,orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();

    // private LocalDateTime regTime;
    // private LocalDateTime updateTime;
    // BaseEntity를 통한 통합관리로 인한 삭제


    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem); // private List<OrderItem> orderItems = new ArrayList<>();
        orderItem.setOrder(this);
        // Order 엔티티와 OrderItem 엔티티가 양방향 참조이므로 orderItem 객체에도 order 객체를 세팅한다.
    }

    public static Order createOrder(Member member, List<OrderItem> orderItemList){
        Order order = new Order();
        order.setMember(member);
        for(OrderItem orderItem : orderItemList){
            order.addOrderItem(orderItem);
        }
        // 상품 페이지에서는 해당 상품만 구매 가능
        // but 장바구니 페이지에서는 여러개의 상품 한번에 구매 가능.
        // -> addOrderItem을 통해 private List<OrderItem> orderItems = new ArrayList<>(); List에 추가

        order.setOrderStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    // 총 주문금액 구하는 메서드
    public int getTotalPrice(){
        int totalPrice = 0;
        for(OrderItem orderItem : orderItems){
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

    public void cancelOrder(){
        this.orderStatus = OrderStatus.CANCEL;

        for(OrderItem orderItem : orderItems){
            orderItem.cancel();
        }
    }
}
