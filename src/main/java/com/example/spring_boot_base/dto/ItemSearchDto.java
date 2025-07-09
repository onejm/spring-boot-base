package com.example.spring_boot_base.dto;

import com.example.spring_boot_base.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ItemSearchDto {
    private String searchDateType; //현재 시간과 상품 등록일을 비교해서 상품 데이터를 조회
    private ItemSellStatus searchSellStatus; //판매상태 기준으로 조회
    private String searchBy; // 상품을 어떤 유형으로 조회할지 선택(상품명, 등록자 아이디)
    private String searchQuery = ""; // 조회할 검색어 저장할 변수
}