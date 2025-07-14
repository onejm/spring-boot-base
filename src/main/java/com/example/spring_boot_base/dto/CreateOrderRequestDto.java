package com.example.spring_boot_base.dto;

import lombok.Data;

@Data
public class CreateOrderRequestDto {
    private Long itemId;
    private int count;
}