package com.nhnacademy.bookpubshop.coupon.repository;

import com.nhnacademy.bookpubshop.coupon.dto.response.GetCouponResponseDto;
import com.nhnacademy.bookpubshop.coupon.dto.response.GetOrderCouponResponseDto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * CouponRepository custom 을 위한 레포.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@NoRepositoryBean
public interface CouponRepositoryCustom {
    /**
     * 쿠폰 번호를 통해 쿠폰을 조회하는 메서드.
     *
     * @param couponNo 쿠폰 조회를 위한 쿠폰 번호
     * @return 쿠폰 조회 Dto
     */
    Optional<GetCouponResponseDto> findByCouponNo(Long couponNo);

    /**
     * 쿠폰 리스트 페이지를 조회하는 메서드.
     *
     * @return 쿠폰 조회 Dto 페이지
     */
    Page<GetCouponResponseDto> findAllBy(Pageable pageable, String searchKey, String search);

    /**
     * 주문에 사용될 쿠폰들을 조회하는 메서드입니다.
     *
     * @param memberNo      멤버 번호
     * @param productNoList 상품 번호 리스트
     * @return 주문에 사용될 쿠폰들의 정보를 담은 Dto 리스트
     */
    List<GetOrderCouponResponseDto> findByProductNo(Long memberNo, List<Long> productNoList);
}
