package com.example.spring_boot_base.service;

import com.example.spring_boot_base.entity.KakaoPayApiClient;
import com.example.spring_boot_base.entity.Order;
import com.example.spring_boot_base.constant.PaymentStatus;
import com.example.spring_boot_base.dto.*;
import com.example.spring_boot_base.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class KakaoPayService {

    private final OrderRepository orderRepository;
    private final KakaoPayApiClient kakaoPayApiClient;
    private final SlackNotifierService slackNotifierService;

    @Transactional
    public KakaoPayReadyResponseDto readyPayment(Long orderId, String userEmail) throws AccessDeniedException {
        Order order = orderRepository.findWithItemsById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("주문이 존재하지 않음"));

        if (!order.getMember().getEmail().equals(userEmail)) {
            throw new AccessDeniedException("해당 주문에 대한 접근 권한이 없습니다.");
        }

        order.setPaymentStatus(PaymentStatus.READY);

        KakaoPayReadyResponseDto response = kakaoPayApiClient.ready(order);
        order.setKakaoTid(response.getTid());

        return response;
    }

    @Transactional
    public KakaoPayApproveResponseDto approvePayment(Long orderId, String userEmail, String pgToken) throws AccessDeniedException {
        log.info("approvePayment called with orderId={}, userEmail={}, pgToken={}", orderId, userEmail, pgToken);

        Order order = orderRepository.findWithItemsById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("주문이 존재하지 않음"));

        if (!order.getMember().getEmail().equals(userEmail)) {
            throw new AccessDeniedException("해당 주문에 대한 접근 권한이 없습니다.");
        }

        try {
            KakaoPayApproveResponseDto response = kakaoPayApiClient.approve(order, pgToken);

            if (response != null && response.getTid() != null) {
                order.setPaymentStatus(PaymentStatus.APPROVED);
                order.setPaymentDate(LocalDateTime.now());
                order.setKakaoTid(response.getTid());
                orderRepository.save(order);

                sendPaymentApprovedMessage(order);

                log.info("Order payment status updated to APPROVED for order id: {}", order.getId());
            } else {
                order.setPaymentStatus(PaymentStatus.FAILED);
                orderRepository.save(order);
                log.warn("결제 승인 실패: 응답에 TID 없음. orderId={}", order.getId());
                throw new RuntimeException("결제 승인 실패: 유효하지 않은 응답");
            }

            return response;

        } catch (Exception e) {
            order.setPaymentStatus(PaymentStatus.FAILED);
            orderRepository.save(order);
            log.error("결제 승인 중 예외 발생 - orderId={}, error={}", order.getId(), e.getMessage(), e);
            slackNotifierService.sendMessage(
                    String.format(" 결제 승인 실패\n주문ID: %d\n에러: %s", order.getId(), e.getMessage())
            );
            throw new RuntimeException("결제 승인 중 오류가 발생했습니다.");
        }
    }

    public void sendPaymentApprovedMessage(Order order) {
        String productList = order.getOrderItems().stream()
                .map(oi -> oi.getItem().getItemName() + " x" + oi.getCount())
                .collect(Collectors.joining(", "));

        String message = String.format(
                "결제 승인 완료\n\n" +
                        "주문상품: %s\n"+
                        "주문번호: %d\n" +
                        "결제금액: %,d원\n" +
                        "결제일시: %s\n" +
                        "결제수단: 카카오페이\n" +
                        "결제번호(TID): %s\n" +
                        "구매자: %s",
                productList,
                order.getId(),
                order.getTotalPrice(),
                order.getPaymentDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                order.getKakaoTid(),
                order.getMember().getEmail()
        );

        slackNotifierService.sendMessage(message);
    }

    @Transactional
    public KakaoPayCancelResponseDto cancelPayment(Order order) {
        try {
            KakaoPayCancelResponseDto response = kakaoPayApiClient.requestCancelPayment(
                    new KakaoPayCancelRequestDto(order.getKakaoTid(), order.getTotalPrice())
            );

            String productList = order.getOrderItems().stream()
                    .map(oi -> oi.getItem().getItemName() + " x" + oi.getCount())
                    .collect(Collectors.joining(", "));

            String message = String.format(
                    "결제 취소 완료\n\n" +
                            "취소상품       : %s\n"+
                            "결제수단       : 카카오페이\n" +
                            "결제금액       : %,d원\n" +
                            "취소일시       : %s\n" +
                            "주문번호       : %d\n" +
                            "구매자         : %s",
                    productList,
                    order.getTotalPrice(),
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    order.getId(),
                    order.getMember().getEmail()
            );



            slackNotifierService.sendMessage(message);

            return response;

        } catch (Exception e) {
            String errorMessage = String.format(
                    "결제 취소 실패\n" +
                            "주문번호: %d\n" +
                            "에러: %s",
                    order.getId(),
                    e.getMessage()
            );

            slackNotifierService.sendMessage(errorMessage);

            throw new RuntimeException("결제 취소 중 오류 발생", e);
        }
    }

    public Order getOrderByIdAndUser(Long orderId, String userEmail) throws AccessDeniedException {
        Order order = orderRepository.findWithItemsById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("주문이 존재하지 않음"));

        if (!order.getMember().getEmail().equals(userEmail)) {
            throw new AccessDeniedException("해당 주문에 대한 접근 권한이 없습니다.");
        }

        return order;
    }

}