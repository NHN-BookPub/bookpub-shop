package com.nhnacademy.bookpubshop.couponpolicy.service;

import com.nhnacademy.bookpubshop.couponpolicy.dto.request.CreateCouponPolicyRequestDto;
import com.nhnacademy.bookpubshop.couponpolicy.dto.request.ModifyCouponPolicyRequestDto;
import com.nhnacademy.bookpubshop.couponpolicy.dto.response.GetCouponPolicyResponseDto;
import java.util.List;

/**
 * 쿠폰정책 서비스 인터페이스입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public interface CouponPolicyService {


    /**
     * 쿠폰정책을 생성하기위한 메소드입니다.
     *
     * @param createCouponPolicyRequestDto 쿠폰정책 생성을 위한 정보들이 기입됩니다.
     */
    void addCouponPolicy(CreateCouponPolicyRequestDto createCouponPolicyRequestDto);

    /**
     * 쿠폰정책을 수정하기위한 메소드입니다.
     *
     * @param modifyCouponPolicyRequestDto 쿠폰정책 수정을 위한 정보들이 기입됩니다.
     */
    void modifyCouponPolicy(ModifyCouponPolicyRequestDto modifyCouponPolicyRequestDto);

    /**
     * 쿠폰정책 단건 조회를 위한 메소드입니다.
     *
     * @param policyNo 쿠폰정책번호
     * @return GetCouponPolicyResponseDto 쿠폰정책에 관한 정보들이 반환됩니다.
     */
    GetCouponPolicyResponseDto getCouponPolicy(Integer policyNo);

    /**
     * 모든 쿠폰 리스트 조회를 위한 메소드입니다.
     *
     * @return list 쿠폰정책에 관한 정보 리스트가 반환됩니다
     */
    List<GetCouponPolicyResponseDto> getCouponPolicies();
}
