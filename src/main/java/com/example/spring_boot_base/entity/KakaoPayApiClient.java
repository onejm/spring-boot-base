package com.example.spring_boot_base.entity;

import com.example.spring_boot_base.dto.KakaoPayCancelResponseDto;
import com.example.spring_boot_base.dto.KakaoPayReadyRequest;
import com.example.spring_boot_base.dto.KakaoPayReadyResponseDto;
import com.example.spring_boot_base.dto.KakaoPayApproveResponseDto;
import com.example.spring_boot_base.dto.KakaoPayCancelRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class KakaoPayApiClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${kakaopay.admin-key}")
    private String adminKey;

    @Value("${kakaopay.cid}")
    private String cid;

    @Value("${kakaopay.host}")
    private String host;

    @Value("${kakaopay.ready-url}")
    private String readyUrl;

    @Value("${kakaopay.approve-url}")
    private String approveUrl;

    public KakaoPayReadyResponseDto ready(Order order) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "SECRET_KEY " + adminKey);

        KakaoPayReadyRequest requestBody = getKakaoPayReadyRequest(order);

        HttpEntity<KakaoPayReadyRequest> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<KakaoPayReadyResponseDto> response = restTemplate.postForEntity(
                readyUrl,
                request,
                KakaoPayReadyResponseDto.class
        );

        return response.getBody();
    }

    private KakaoPayReadyRequest getKakaoPayReadyRequest(Order order) {
        KakaoPayReadyRequest requestBody = new KakaoPayReadyRequest();
        requestBody.setCid(cid);
        requestBody.setPartner_order_id(String.valueOf(order.getId()));
        requestBody.setPartner_user_id(order.getMember().getEmail());
        requestBody.setItem_name(order.getOrderItems().get(0).getItem().getItemName());
        requestBody.setQuantity(order.getOrderItems().get(0).getCount());
        requestBody.setTotal_amount(order.getTotalPrice());
        requestBody.setVat_amount(0);
        requestBody.setTax_free_amount(0);

        requestBody.setApproval_url(host + "/kakaoPaySuccess?orderId=" + order.getId());
        requestBody.setCancel_url(host + "/kakaoPayCancel");
        requestBody.setFail_url(host + "/kakaoPayFail");
        return requestBody;
    }

    public KakaoPayApproveResponseDto approve(Order order, String pgToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "SECRET_KEY " + adminKey);

        Map<String,Object> body = new HashMap<>();
        body.put("cid", cid);
        body.put("tid", order.getKakaoTid());
        body.put("partner_order_id", String.valueOf(order.getId()));
        body.put("partner_user_id", order.getMember().getEmail());
        body.put("pg_token", pgToken);

        HttpEntity<Map<String,Object>> req = new HttpEntity<>(body, headers);
        ResponseEntity<KakaoPayApproveResponseDto> resp = restTemplate.postForEntity(approveUrl, req, KakaoPayApproveResponseDto.class);
        return resp.getBody();
    }

    public KakaoPayCancelResponseDto requestCancelPayment(KakaoPayCancelRequestDto dto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "SECRET_KEY " + adminKey);

        Map<String, Object> body = new HashMap<>();
        body.put("cid", cid);
        body.put("tid", dto.getTid());
        body.put("cancel_amount", dto.getCancelAmount());
        body.put("cancel_tax_free_amount", 0);
        body.put("cancel_vat_amount", 0);
        body.put("cancel_available_amount", dto.getCancelAmount());

        HttpEntity<Map<String, Object>> req = new HttpEntity<>(body, headers);

        ResponseEntity<KakaoPayCancelResponseDto> response = restTemplate.postForEntity(
                "https://open-api.kakaopay.com/online/v1/payment/cancel",
                req,
                KakaoPayCancelResponseDto.class
        );

        return response.getBody();
    }

}