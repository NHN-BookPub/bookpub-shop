package com.nhnacademy.bookpubshop.tier.relationship.service;

import com.nhnacademy.bookpubshop.tier.relationship.dto.request.CreateTierCouponRequestDto;
import com.nhnacademy.bookpubshop.tier.relationship.dto.response.GetTierCouponResponseDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * TierCouponService 인터페이스.
 *
 * @author : 김서현
 * @since : 1.0
 **/
public interface TierCouponService {

    /**
     * 등급 쿠폰 전체 조회를 위한 메서드.
     *
     * @param pageable 페이지 정보.
     * @return 등급 쿠폰 페이지.
     */
    Page<GetTierCouponResponseDto> getTierCoupons(Pageable pageable);

    /**
     * 등급 쿠폰 등록을 위한 메서드.
     *
     * @param request 등급 쿠폰 정보.
     */
    void createTierCoupon(CreateTierCouponRequestDto request);

    /**
     * 등급 쿠폰 삭제를 위한 메서드.
     *
     * @param templateNo 쿠폰 템플릿 번호.
     * @param tierNo     쿠폰 번호.
     */
    void deleteTierCoupon(Long templateNo, Integer tierNo);

    /**
     * 등급번호로 해당 등급쿠폰(템플릿 번호) 조회를 위한 메서드.
     *
     * @param tierNo 등급 번호
     * @return 템플릿 번호 리스트
     */
    List<Long> getTierCouponsByTierNo(Integer tierNo);
}
