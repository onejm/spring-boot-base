package com.example.spring_boot_base.controller;

import com.example.spring_boot_base.dto.ItemSearchDto;
import com.example.spring_boot_base.dto.MainItemDto;
import com.example.spring_boot_base.entity.Item;
import com.example.spring_boot_base.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.jboss.jandex.Main;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final ItemService itemService;

    @GetMapping(value="/")
    public String main(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
        Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);
        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);

        return "main";
    }
}