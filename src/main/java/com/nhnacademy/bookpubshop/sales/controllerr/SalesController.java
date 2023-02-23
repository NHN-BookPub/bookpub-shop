package com.nhnacademy.bookpubshop.sales.controllerr;

import com.nhnacademy.bookpubshop.annotation.AdminAuth;
import com.nhnacademy.bookpubshop.sales.dto.response.OrderCntResponseDto;
import com.nhnacademy.bookpubshop.sales.dto.response.SaleProductCntDto;
import com.nhnacademy.bookpubshop.sales.dto.response.TotalSaleDto;
import com.nhnacademy.bookpubshop.sales.dto.response.TotalSaleYearDto;
import com.nhnacademy.bookpubshop.sales.service.SalesService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 매출관련 rest controller 입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
@RestController
@RequiredArgsConstructor
public class SalesController {
    private final SalesService service;

    @AdminAuth
    @GetMapping("/token/sales")
    public ResponseEntity<List<TotalSaleDto>> salesList(
            @RequestParam(value = "start", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime start,
            @RequestParam(value = "end", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime end) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.getSales(start, end));
    }

    /**
     * 주문의 수를 시간대별로 확인합니다.
     *
     * @return the response entity
     */
    @AdminAuth
    @GetMapping("/token/order-count")
    public ResponseEntity<List<OrderCntResponseDto>> orderCnt() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.getOrderCnt());
    }

    /**
     * 월별 주문을 조회합니다.
     * 성공시 200이 반환됩니다.
     *
     * @param start 시작일자
     * @param end   종료일자
     * @return the response entity
     */
    @GetMapping("/token/sales-month")
    public ResponseEntity<List<TotalSaleYearDto>> salesMonthList(
            @RequestParam(value = "start", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime start,
            @RequestParam(value = "end", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime end) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.getTotalSaleCurrentYear(start, end));
    }

    /**
     * 특정 기간의 상품 판매량 랭킹을 조회하기 위한 메서드입니다.
     * 조건이 없을 시, 올해의 상품 판매량 랭킹 정보가 반환됩니다.
     * 성공 시 200이 반환.
     *
     * @param start 시작일자
     * @param end   종료일자
     * @return the response entity
     */
    @AdminAuth
    @GetMapping("/token/sale-product-rank")
    public ResponseEntity<List<SaleProductCntDto>> saleProductCount(
            @RequestParam(value = "start", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime start,
            @RequestParam(value = "end", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime end
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.getSaleProductCount(start, end));
    }
}
