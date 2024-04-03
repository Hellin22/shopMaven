package com.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="/thymeleaf")
public class ThymeleafExController {

    @GetMapping("/ex01")
    public String thymeleafExample01(Model model){
        model.addAttribute("data", "모델로 전달한 값이에요. 타임리프가 제대로 됐으면 나와용");
        return "thymeleafEx/thymeleafEx01";
    }
}
