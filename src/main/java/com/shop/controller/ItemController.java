package com.shop.controller;

import com.shop.dto.ItemFormDto;
import com.shop.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping(value="/admin/item/new")
    public String itemForm(Model model){
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "/item/itemForm";
    }

    @PostMapping(value="/admin/item/new")
    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, Model model,
                          @RequestParam("itemImgFile") List<MultipartFile>itemImgFileList) {

        if(bindingResult.hasErrors()){ // 상품 등록시에 필수값이 없다면
            return "item/itemForm";
        }

        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null){
            // 상품 대표 이미지 0번을 메인에서 보여줄건데 그거 없으면 상품 등록 페이지로 전환
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수에요");
            return "item/itemForm";
        }

        try{
            itemService.saveItem(itemFormDto, itemImgFileList); // 상품정보 + 상품 이미지정보
        }catch (Exception e){
            model.addAttribute("errorMessage", "상품 등록 중 오류 발생");
            return "item/itemForm";
        }

        return "redirect:/";







    }
}
