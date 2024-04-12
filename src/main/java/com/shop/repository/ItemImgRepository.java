package com.shop.repository;

import com.shop.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// 상품 이미지정보를 저장하는 리파지토리
public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {

    List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);
    // itemId를 통해서 해당 아이템의 아이템 이미지 id를 오름차순으로 출력해줌

    ItemImg findByItemIdAndRepimgYn(Long itemId, String repimgYn);
}
