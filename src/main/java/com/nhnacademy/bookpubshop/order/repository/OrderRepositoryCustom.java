package com.nhnacademy.bookpubshop.order.repository;

import com.nhnacademy.bookpubshop.order.dto.GetOrderDetailResponseDto;
import com.nhnacademy.bookpubshop.order.dto.GetOrderListResponseDto;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 주문레포지토리에서 querydsl을 사용하기 위한 custom 클래스입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@NoRepositoryBean
public interface OrderRepositoryCustom {
    /**
     * 주문의 상세 정보를 반환합니다.
     *
     * @param orderNo 주문번호.
     * @return 주문상세 정보를 반환.
     */
    Optional<GetOrderDetailResponseDto> getOrderDetailById(Long orderNo);

    /**
     * 모든 주문을 반환합니다.
     *
     * @param pageable 페이징을 위해 받습니다.
     * @return 모든 주문을 반환.
     */
    Page<GetOrderListResponseDto> getOrdersList(Pageable pageable);

    /**
     * 멤버의 모든 주문을 반환합니다.
     *
     * @param pageable 페이징.
     * @param memberNo 멤버 번호.
     * @return 멤버의 모든 주문 반환.
     */
    Page<GetOrderListResponseDto> getOrdersListByUser(Pageable pageable, Long memberNo);
}
