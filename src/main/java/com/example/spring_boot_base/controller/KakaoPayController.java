package com.example.spring_boot_base.controller;

import com.example.spring_boot_base.dto.KakaoPayApproveResponseDto;
import com.example.spring_boot_base.dto.KakaoPayReadyResponseDto;
import com.example.spring_boot_base.dto.OrderRequestDto;
import com.example.spring_boot_base.entity.Order;
import com.example.spring_boot_base.service.KakaoPayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityNotFoundException;
import java.nio.file.AccessDeniedException;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class KakaoPayController {

    private final KakaoPayService kakaoPayService;

    @PostMapping("/kakaoPayReady")
    @ResponseBody
    public ResponseEntity<?> kakaoPayReady(@RequestBody OrderRequestDto requestDto,
                                           Principal principal) {
        String userEmail = principal.getName();
        try {
            KakaoPayReadyResponseDto responseDto = kakaoPayService.readyPayment(requestDto.getOrderId(), userEmail);
            return ResponseEntity.ok(responseDto);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
        }
    }

    @GetMapping("/kakaoPaySuccess")
    public String kakaoPaySuccess(@RequestParam Long orderId,
                                  @RequestParam String pg_token,
                                  RedirectAttributes redirectAttributes,
                                  Principal principal) {
        try {
            KakaoPayApproveResponseDto response = kakaoPayService.approvePayment(orderId, principal.getName(), pg_token);
            redirectAttributes.addFlashAttribute("approveResponse", response);
            return "redirect:/kakaoPaySuccessResult";
        } catch (Exception e) {
            return "redirect:/kakaoPayFail";
        }
    }

    @GetMapping("/kakaoPaySuccessResult")
    public String kakaoPaySuccessResult() {
        return "payment/paymentSuccess"; // 재승인 방지
    }

    @GetMapping("/kakaoPayFailResult")
    public String kakaoPayFailResult() {
        return "payment/paymentFail"; // 재승인 방지
    }


    @PostMapping("/kakaoPayCancel/{orderId}")
    @ResponseBody
    public ResponseEntity<?> kakaoPayCancel(@PathVariable Long orderId, Principal principal) {
        try {
            Order order = kakaoPayService.getOrderByIdAndUser(orderId, principal.getName());
            kakaoPayService.cancelPayment(order);
            return ResponseEntity.ok("결제가 성공적으로 취소되었습니다.");
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("접근 권한이 없습니다.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("주문이 존재하지 않습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("결제 취소 중 오류 발생");
        }
    }

    @GetMapping("/kakaoPayCancelResult")
    public String kakaoPayCancelResult(Model model) {
        return "payment/paymentCancel"; // 여기서 해당 html을 참조
    }

    @GetMapping("/kakaoPayFail")
    public String kakaoPayFail() {
        return "redirect:/";
    }
}