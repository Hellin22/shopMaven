package com.shop.dto;

import com.shop.constant.ItemSellStatus;
import com.shop.entity.Item;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemFormDto { // 상품 데이터정보 전달 dto

    private Long id;

    @NotBlank(message="상품명은 필수")
    private String itemNm;

    @NotNull(message = "상품 가격은 필수")
    private Integer price;

    @NotBlank(message = "상품 이름은 필수")
    private String itemDetail;

    @NotNull(message = "상품 재고는 필수")
    private Integer stockNumber;

    private ItemSellStatus itemSellStatus;

    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();
    // 상품 저장하고 수정할 때 상품 이미지 정보를 저장하는 리스트

    private List<Long> itemImgIds = new ArrayList<>();
    // 상품이미지 아이디를 저장하는 리스트
    // 등록할때는 상품 이미지를 저장 안했기 때문에 값이 없고 수정할때 이미지 아이디를 담아놓음.

    private static ModelMapper modelMapper = new ModelMapper();

    public Item createItem(){
        return modelMapper.map(this, Item.class);
    }

    public static ItemFormDto of(Item item){
        return modelMapper.map(item, ItemFormDto.class);
    }
    // 위 2개의 함수는 모델매퍼로 엔티티와 dto 간에 데이터복사해서 복사한 객체를 반환하는 메서드
}
