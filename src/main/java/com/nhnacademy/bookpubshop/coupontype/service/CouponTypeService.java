package com.nhnacademy.bookpubshop.coupontype.service;

import com.nhnacademy.bookpubshop.coupontype.dto.response.GetCouponTypeResponseDto;
import java.util.List;

/**
 * 쿠폰유형 서비스 인터페이스.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public interface CouponTypeService {

    /**
     * 쿠폰유형 단건 조회를 위한 메소드입니다.
     *
     * @param typeNo 쿠폰유형번호
     * @return GetCouponTypeResponseDto 쿠폰유형이름이 반환됩니다.
     */
    GetCouponTypeResponseDto getCouponType(Long typeNo);

    /**
     * 쿠폰유형 리스트 조회를 위한 메소드입니다.
     *
     * @return GetCouponTypeResponseDto 쿠폰유형이름 리스트가 반환됩니다.
     */
    List<GetCouponTypeResponseDto> getCouponTypes();
}
