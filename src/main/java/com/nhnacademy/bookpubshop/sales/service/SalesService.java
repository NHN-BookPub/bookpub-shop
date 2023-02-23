package com.nhnacademy.bookpubshop.sales.service;

import com.nhnacademy.bookpubshop.sales.dto.response.OrderCntResponseDto;
import com.nhnacademy.bookpubshop.sales.dto.response.SaleProductCntDto;
import com.nhnacademy.bookpubshop.sales.dto.response.TotalSaleDto;
import com.nhnacademy.bookpubshop.sales.dto.response.TotalSaleYearDto;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 매출 서비스 클래스입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
public interface SalesService {

    /**
     * 매출정보를 반환합니다.
     *
     * @param start 시작일자
     * @param end   종료일자
     * @return 매출값 반환.
     */
    List<TotalSaleDto> getSales(LocalDateTime start, LocalDateTime end);

    /**
     * 주문개수를 반환합니다.
     *
     * @return 주문개수를 반환
     */
    List<OrderCntResponseDto> getOrderCnt();

    /**
     * 월별 매출정보를 반환합니다.
     *
     * @param start 시작일자
     * @param end   종료일자
     * @return 매출정보 반환
     */
    List<TotalSaleYearDto> getTotalSaleCurrentYear(LocalDateTime start, LocalDateTime end);

    /**
     * 상품 판매량 랭킹을 조회하기 위한 메서드입니다.
     *
     * @param start 시작일자
     * @param end   종료일자
     * @return  상품 판매량 랭킹 정보가 담긴 dto 리스트
     */
    List<SaleProductCntDto> getSaleProductCount(LocalDateTime start, LocalDateTime end);
}
