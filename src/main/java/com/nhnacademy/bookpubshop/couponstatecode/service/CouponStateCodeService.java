package com.nhnacademy.bookpubshop.couponstatecode.service;

import com.nhnacademy.bookpubshop.couponstatecode.dto.GetCouponStateCodeResponseDto;
import java.util.List;

/**
 * 쿠폰상태코드 서비스 인터페이스.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public interface CouponStateCodeService {

    /**
     * 쿠폰상태코드 단건 조회를 위한 메소드입니다.
     *
     * @param codeNo 쿠폰상태코드 번호
     * @return GetCouponStateCodeResponseDto 적용타겟 이름이 반환됩니다.
     */
    GetCouponStateCodeResponseDto getCouponStateCode(Integer codeNo);

    /**
     * 쿠폰상태코드 리스트 조회를 위한 메소드입니다.
     *
     * @return 전체 쿠폰상태코드 적용타겟 이름이 반환됩니다.
     */
    List<GetCouponStateCodeResponseDto> getCouponStateCodes();
}
