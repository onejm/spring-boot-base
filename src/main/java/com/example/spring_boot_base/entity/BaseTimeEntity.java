package com.example.spring_boot_base.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@EntityListeners(value = {AuditingEntityListener.class}) // Auditing 적용을 위한 어노테이션
@MappedSuperclass // 공통 매핑 정보가 필요할 때 사용하는 어노테이션으로 부모 클래스를 상속 받는 자식 클래스에 매핑 정보만 제공
@Getter @Setter
public abstract class BaseTimeEntity {
    @CreatedDate // 엔티티가 생성되어 저장될 때 시간을 자동으로 저장
    @Column(updatable = false) //수정 시 이 컬럼은 변경되지 않도록 설정
    private LocalDateTime regTime;
    @LastModifiedDate // 엔티티 값 변경할 때 시간을 자동으로 저장
    private LocalDateTime updateTime;
}