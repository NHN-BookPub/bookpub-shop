package com.nhnacademy.bookpubshop.paymentstatecode.controller;

import com.nhnacademy.bookpubshop.annotation.AdminAuth;
import com.nhnacademy.bookpubshop.paymentstatecode.dto.response.GetPaymentStateResponseDto;
import com.nhnacademy.bookpubshop.paymentstatecode.entity.PaymentStateCode;
import com.nhnacademy.bookpubshop.paymentstatecode.service.PaymentStateService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 결제상태 컨트롤러.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@RestController
@RequiredArgsConstructor
public class PaymentStateController {
    private final PaymentStateService paymentStateService;

    /**
     * 모든 결제상태를 조회하는 api.
     *
     * @return 성공시 200과 모든 결제상태의 리스트가 반환됩니다.
     */
    @GetMapping("/token/payment/state")
    @AdminAuth
    public ResponseEntity<List<GetPaymentStateResponseDto>> getPaymentList() {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(paymentStateService.getAllPaymentState());
    }

    /**
     * 결제상태 이름으로 조회하는 api.
     *
     * @param stateName 결제상태 이름.
     * @return 성공시 200, 객체를 반환합니다.
     */
    @GetMapping("/api/payment/state/{stateName}")
    public ResponseEntity<PaymentStateCode> productSaleStateCodeDetails(
            @PathVariable String stateName) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(paymentStateService.getPaymentState(stateName));
    }
}
