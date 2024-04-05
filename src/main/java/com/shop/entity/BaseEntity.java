package com.shop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(value = {AuditingEntityListener.class}) // auditing을 적용하기 위함.
@MappedSuperclass // 공통 매핑 정보가 필요할 때 사용. 부모클래스를 상속받는 자식클래스에 매핑정보만 제공
@Getter
public class BaseEntity extends BaseTimeEntity{

    @CreatedBy // 엔티티가 생성되고 저장될 때 시간 자동저장
    @Column(updatable = false)
    private String createdBy;

    @LastModifiedBy // 엔티티의 값 변경시 시간 자동저장
    private String modifiedBy;
}
