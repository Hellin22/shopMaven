package com.shop.repository;

import com.shop.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;

// 상품 이미지정보를 저장하는 리파지토리
public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {
}
