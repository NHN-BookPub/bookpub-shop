package com.nhnacademy.bookpubshop.paymenttypestatecode.controller;

import com.nhnacademy.bookpubshop.annotation.AdminAuth;
import com.nhnacademy.bookpubshop.paymenttypestatecode.dto.response.GetPaymentTypeResponseDto;
import com.nhnacademy.bookpubshop.paymenttypestatecode.entity.PaymentTypeStateCode;
import com.nhnacademy.bookpubshop.paymenttypestatecode.service.PaymentTypeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 결제유형의 rest controller.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@RestController
@RequiredArgsConstructor
public class PaymentTypeStateController {
    private final PaymentTypeService paymentTypeService;

    /**
     * 모든 결제유형을 조회하는 api.
     *
     * @return 성공시 200과 모든 정책코드의 리스트가 반환됩니다.
     */
    @GetMapping("/token/payment/type")
    @AdminAuth
    public ResponseEntity<List<GetPaymentTypeResponseDto>> getPaymentList() {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(paymentTypeService.getAllPaymentType());
    }

    /**
     * 결제유형 이름으로 조회하는 api.
     *
     * @param typeName 판매유형 이름.
     * @return 성공시 200, 객체를 반환합니다.
     */
    @GetMapping("/api/payment/type/{typeName}")
    public ResponseEntity<PaymentTypeStateCode> productSaleStateCodeDetails(
            @PathVariable String typeName) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(paymentTypeService.getPaymentType(typeName));
    }
}
