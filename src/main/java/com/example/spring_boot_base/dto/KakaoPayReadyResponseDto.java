package com.example.spring_boot_base.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class KakaoPayReadyResponseDto {
    private String tid;
    @JsonProperty("next_redirect_pc_url")
    private String nextRedirectPcUrl;
    private LocalDateTime createdAt;
}