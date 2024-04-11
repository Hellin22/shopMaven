package com.shop.service;

import com.shop.dto.OrderDto;
import com.shop.entity.Item;
import com.shop.entity.Member;
import com.shop.entity.Order;
import com.shop.entity.OrderItem;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepository;
import com.shop.repository.OrderRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;

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
}
