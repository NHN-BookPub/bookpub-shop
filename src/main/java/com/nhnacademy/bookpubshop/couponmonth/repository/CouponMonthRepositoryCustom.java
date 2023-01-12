package com.nhnacademy.bookpubshop.couponmonth.repository;

import com.nhnacademy.bookpubshop.couponmonth.dto.response.GetCouponMonthResponseDto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * CouponMonthRepository custom 을 위한 레포.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@NoRepositoryBean
public interface CouponMonthRepositoryCustom {
    /**
     * 이달의쿠폰 번호를 통해 이달의쿠폰을 조회하는 메서드.
     *
     * @param monthNo 쿠폰 조회를 위한 쿠폰 번호
     * @return 이달의쿠폰 조회 Dto
     */
    Optional<GetCouponMonthResponseDto> getCouponMonth(Long monthNo);

    /**
     * 전체 이달의쿠폰을 조회하는 메서드.
     *
     * @return 이달의쿠폰 조회 Dto 리스트
     */
    List<GetCouponMonthResponseDto> getCouponMonths();
}
