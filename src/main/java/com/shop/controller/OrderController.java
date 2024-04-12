package com.shop.controller;

import com.shop.dto.OrderDto;
import com.shop.dto.OrderHistDto;
import com.shop.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping(value = "/order")
    public @ResponseBody ResponseEntity order (@RequestBody @Valid OrderDto orderDto,
                                               BindingResult bindingResult, Principal principal) {
        // 스프링에서 비동기 처리를 할때 @RequestBody와 @ResponseBody를 사용한다.
        // @RequestBody -> http 요청의 body 내용을 자바 객체로 전달
        // @ResponseBody -> 자바 객체를 http 요청의 body로 전달
        // 즉, responseEntity

        if(bindingResult.hasErrors()) { // 주문 정보가 있는 orderDto에 에러가 있는지 검사한다.
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for(FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
            // 에러가 있다면 에러정보를 ResponseEntity 객체에 담아서 리턴한다.
        }

        String email = principal.getName();
        // 현재 로그인 유저의 정보를 얻기 위해 @Controller 어노테이션이 있는 클래스에서
        // 메서드 인자로 Principal 객체를 넘겨주면 해당 객체(로그인 유저)에 접근할 수 있다.
        // 그 후 principal 객체에서 사용자의 이메일 정보를 받는다.

        Long orderId;
        try{
            orderId = orderService.order(orderDto, email); // 주문 로직 호출
        }catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
        // 결과값으로 생성된 주문 번호와 ok 상태코드를 리턴한다.
    }

    @GetMapping(value = {"/orders", "/orders/{page}"})
    public String orderHist(@PathVariable("page")Optional<Integer> page, Principal principal, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 4);
        // 한번에 가지고 올 주문 개수는 4개이다.

        Page<OrderHistDto> ordersHistDtoList = orderService.getOrderList(principal.getName(), pageable);
        // 현재 로그인한 회원은 이메일과 페이징 객체를 파라미터로 전달해서 OrderHistDto 데이터를 리턴값으로 받는다.

        model.addAttribute("orders", ordersHistDtoList);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage", 5);

        return "order/orderHist";
    }

    @PostMapping("/order/{orderId}/cancel")
    public @ResponseBody ResponseEntity cancelOrder
            (@PathVariable("orderId") Long orderId, Principal principal) {

        if(!orderService.validateOrder(orderId, principal.getName())) {
            // js에서 취소할 주문 번호 조작이 가능하므로 다른사람이 취소 못하게 권한 검사를 한다.
            return new ResponseEntity<String>("주문 취소 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        orderService.cancelOrder(orderId);
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }
}
