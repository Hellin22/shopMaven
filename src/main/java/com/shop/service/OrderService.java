package com.shop.service;

import com.shop.dto.OrderDto;
import com.shop.dto.OrderHistDto;
import com.shop.dto.OrderItemDto;
import com.shop.entity.*;
import com.shop.repository.ItemImgRepository;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepository;
import com.shop.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ItemImgRepository itemImgRepository;

    public Long order(OrderDto orderDto, String email) {
        Item item = itemRepository.findById(orderDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);
        Member member = memberRepository.findByEmail(email);
        // item과 member를 각각 찾아온다.

        OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
        // item을 orderItem으로 바꾼다.

        List<OrderItem> orderItemList = new ArrayList<>();

        orderItemList.add(orderItem);

        Order order = Order.createOrder(member, orderItemList);
        // 현재 list에는 1개의 orderItem만 존재 -> 장바구니 기능 만들고 여러개를 넣을 예정이다.

        orderRepository.save(order);

        return order.getId();
    }

    @Transactional(readOnly = true)
    public Page<OrderHistDto> getOrderList(String email, Pageable pageable) {
        List<Order> orders = orderRepository.findOrders(email, pageable);
        // 사용자 email과 페이징 조건을 활용해서 member가 주문한 order들을 찾아온다.
        Long totalCount = orderRepository.countOrder(email);
        // 사용자가 주문한 order의 총 개수를 찾아온다.

        List<OrderHistDto> orderHistDtoList = new ArrayList<>();
        // 주문 이력은 OrderHistDto 객체로 넘겨줄 것이기 때문에 새로운 OHD를 만든다.

        for(Order order : orders) {
            // member 가 주문한 모든 order를 OrderHistDto로 바꿔줘야 한다.
            // 1. orderHistDto에는 orderItemDtoList와 itemImg(대표 이미지)가 필요하다.
            // 2. order에는 여러개의 item이 존재할 수 있다. -> for문을 한번 더 돌린다.

            OrderHistDto orderHistDto = new OrderHistDto(order);

            List<OrderItem> orderItems = order.getOrderItems();
            for(OrderItem orderItem : orderItems) {
                ItemImg itemImg = itemImgRepository.findByItemIdAndRepimgYn(orderItem.getId(), "Y");

                OrderItemDto orderItemDto = new OrderItemDto(orderItem, itemImg.getImgUrl());

                orderHistDto.addOrderItem(orderItemDto);
            }
            orderHistDtoList.add(orderHistDto);
        }
        return new PageImpl<>(orderHistDtoList, pageable, totalCount);
        // 페이지 구현 객체를 생성해서 반환한다.
    }

    @Transactional(readOnly = true)
    public boolean validateOrder(Long orderId, String email){
        // 현재 로그인한 사용자와 주문 데이터를 만든 사용자가 같은지 검사를 한다.
        Member curMember = memberRepository.findByEmail(email);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);

        Member savedMember = order.getMember();

        if(!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())) {
            return false;
        }
        return true;
    }

    public void cancelOrder(Long orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);
        order.cancelOrder();
        // 주문 취소상태로 변경하면 트랜잭션이 끝날때 update 쿼리가 실행된다.
    }

}
