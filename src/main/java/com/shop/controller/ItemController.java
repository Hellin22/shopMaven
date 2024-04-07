package com.shop.controller;

import com.shop.dto.ItemFormDto;
import com.shop.service.ItemService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping(value = "/admin/new/{itemId}")
    public String itemDtl(@PathVariable("itemId") Long itemId, Model model){
        try{
            ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
            model.addAttribute("itemFormDto", itemFormDto);
            // 이 부분은 처음에 상품 등록 코드와 비슷하다.
            // 상품 등록 코드는 빈 itemFormDto를 보냈고 현재는 itemId를 받아와서 특정 itemFormDto를 보낸다.
        }catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "존재하지 않는 상품입니다.");
            model.addAttribute("itemFormDto", new ItemFormDto());
            return "item/itemForm";
        }
        return "item/itemForm";
    }
}
