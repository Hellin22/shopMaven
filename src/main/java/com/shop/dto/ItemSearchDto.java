package com.shop.dto;

import com.shop.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemSearchDto {

    private String searchDateType;
    // 현재 시간과 등록일을 비교해서 상품 데이터 조회(1d, 1w, ..., 6m, all)

    private ItemSellStatus searchSellStatus;
    // 상품이 SELL 인지, SOLD_OUT 인지 확인 후 데이터 조회

    private String searchBy;
    // 상품 조회시 2가지 유형으로 조회 가능(itemNm: 상품명, createdBy: 상품 등록자 아이디)

    private String searchQuery="";
    // 사용자가 입력한 검색어를 저장하는 변수 -> searchBy에 따라 뭘 기준으로 검색할지 바뀜
    // itemNm이면 상품명을 기준으로 검색하고 createdBy면 등록자 아이디 기준으로 검색
}
