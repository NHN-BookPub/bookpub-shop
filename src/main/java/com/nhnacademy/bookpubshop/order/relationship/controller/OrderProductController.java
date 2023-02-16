package com.nhnacademy.bookpubshop.order.relationship.controller;

import com.nhnacademy.bookpubshop.annotation.MemberAndAuth;
import com.nhnacademy.bookpubshop.order.relationship.dto.GetExchangeResponseDto;
import com.nhnacademy.bookpubshop.order.relationship.service.OrderProductService;
import com.nhnacademy.bookpubshop.utils.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 주문상품 컨트롤러.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@RestController
@RequiredArgsConstructor
public class OrderProductController {
    private final OrderProductService orderProductService;

    /**
     * 교환상태인 주문상품의 리스트를 가져오는 메소드.
     *
     * @param pageable 페이저블.
     * @return 교환상태인 주문상품 리스트.
     */
    @MemberAndAuth
    @GetMapping("/token/orders/order-product")
    public ResponseEntity<PageResponse<GetExchangeResponseDto>> exchangeList(Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(orderProductService.getExchangeOrderList(pageable));
    }
}
