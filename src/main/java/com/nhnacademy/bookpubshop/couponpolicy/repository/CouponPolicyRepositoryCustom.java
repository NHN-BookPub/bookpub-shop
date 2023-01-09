package com.nhnacademy.bookpubshop.couponpolicy.repository;

import com.nhnacademy.bookpubshop.couponpolicy.dto.response.GetCouponPolicyResponseDto;
import java.util.List;
import java.util.Optional;

/**
 * 쿠폰정책 Repo Custom 인터페이스입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public interface CouponPolicyRepositoryCustom {

    /**
     * 쿠폰정책번호를 통해 쿠폰정책 정보를 반환.
     *
     * @param policyNo 쿠폰정책번호
     * @return optional 로되어있는 쿠폰정책 반환.
     */
    Optional<GetCouponPolicyResponseDto> findByPolicyNo(Integer policyNo);

    /**
     * 모든 쿠폰정책 정보 리스트를 반환.
     *
     * @return list 쿠폰정책 정보반환.
     */
    List<GetCouponPolicyResponseDto> findByAll();
}
