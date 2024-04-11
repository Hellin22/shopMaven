package com.shop.controller;

import com.shop.dto.OrderDto;
import com.shop.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;

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
}
