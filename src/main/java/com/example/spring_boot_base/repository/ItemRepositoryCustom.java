package com.example.spring_boot_base.repository;

import com.example.spring_boot_base.dto.ItemSearchDto;
import com.example.spring_boot_base.dto.MainItemDto;
import com.example.spring_boot_base.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {
    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
    Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
}