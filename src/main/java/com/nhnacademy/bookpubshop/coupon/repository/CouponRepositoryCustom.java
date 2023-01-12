package com.nhnacademy.bookpubshop.coupon.repository;

import com.nhnacademy.bookpubshop.coupon.dto.response.GetCouponResponseDto;
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
    Optional<GetCouponResponseDto> getCoupon(Long couponNo);

    /**
     * 쿠폰 리스트 페이지를 조회하는 메서드.
     *
     * @return 쿠폰 조회 Dto 페이지
     */
    Page<GetCouponResponseDto> getCoupons(Pageable pageable);
}
