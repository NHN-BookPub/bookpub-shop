package com.nhnacademy.bookpubshop.order.relationship.repository;

import com.nhnacademy.bookpubshop.order.relationship.dto.GetExchangeResponseDto;
import com.nhnacademy.bookpubshop.order.relationship.entity.OrderProduct;
import com.nhnacademy.bookpubshop.sales.dto.response.SaleProductCntDto;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 주문상품레포지토리 커스텀.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@NoRepositoryBean
public interface OrderProductRepositoryCustom {
    /**
     * 주문번호로 하위에 있는 모든 주문상품들을 가져오는 메소드.
     *
     * @param orderNo 주문번호.
     * @return 주문상품리스트.
     */
    List<OrderProduct> getOrderProductList(Long orderNo);

    /**
     * 주문상품 번호로 주문상품을 가져오는 메소드.
     *
     * @param orderProductNo 주문상품번호.
     * @return 주문상품.
     */
    Optional<OrderProduct> getOrderProduct(Long orderProductNo);

    /**
     * 교환대기 주문상품 목록 불러오는 메소드.
     *
     * @param pageable 페이지.
     * @return 목록반환
     */
    Page<GetExchangeResponseDto> getExchangeOrderProductList(Pageable pageable);

    /**
     * 기간에 따른 상품 판매 순위를 조회하기 위한 메소드입니다.
     *
     * @param start 시작 기간
     * @param end   끝 기간
     * @return 상품판매량 정보가 담긴 dto 리스트
     */
    List<SaleProductCntDto> getSaleProductCount(LocalDateTime start, LocalDateTime end);
}
