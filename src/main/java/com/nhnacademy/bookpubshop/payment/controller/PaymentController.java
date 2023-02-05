package com.nhnacademy.bookpubshop.payment.controller;

import com.nhnacademy.bookpubshop.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 결제 컨트롤러 입니다.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@RestController
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    /**
     * 결제 생성 컨트롤러.
     *
     * @param paymentKey 결재 생성 키.
     * @param orderId 주문 아이디.
     * @param amount 금액.
     * @return 결제 생성.
     */
    @GetMapping("/api/payment")
    public ResponseEntity<Void> createPayment(@RequestParam String paymentKey,
                                              @RequestParam String orderId,
                                              @RequestParam Long amount) {
        return null;
    }
}
