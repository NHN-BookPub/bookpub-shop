package com.nhnacademy.bookpubshop.couponmonth.service;

import com.nhnacademy.bookpubshop.couponmonth.dto.request.CreateCouponMonthRequestDto;
import com.nhnacademy.bookpubshop.couponmonth.dto.request.ModifyCouponMonthRequestDto;
import com.nhnacademy.bookpubshop.couponmonth.dto.response.GetCouponMonthResponseDto;
import java.io.IOException;
import java.util.List;

/**
 * CouponMonth 서비스 인터페이스입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public interface CouponMonthService {

    /**
     * 이달의쿠폰 생성을 위한 메서드.
     *
     * @param createRequestDto 이달의쿠폰 생성에 필요한 정보를 담은 Dto
     */
    void createCouponMonth(CreateCouponMonthRequestDto createRequestDto);

    /**
     * 이달의쿠폰 수정을 위한 메서드.
     *
     * @param modifyRequestDto 이달의쿠폰 수정에 필요한 정보를 담은 Dto
     */
    void modifyCouponMonth(ModifyCouponMonthRequestDto modifyRequestDto);

    /**
     * 이달의쿠폰 삭제를 위한 메서드.
     *
     * @param monthNo 삭제할 이달의쿠폰 번호
     */
    void deleteCouponMonth(Long monthNo);

    /**
     * 이달의쿠폰 단건 조회를 위한 메서드.
     *
     * @param monthNo 조회할 이달의쿠폰 번호
     * @return GetCouponMonthResponseDto 쿠폰 조회에 필요한 정보를 담은 Dto
     */
    GetCouponMonthResponseDto getCouponMonth(Long monthNo);

    /**
     * 이달의쿠폰 전체 리스트 조회를 위한 메서드.
     *
     * @return 쿠폰 조회에 필요한 정보를 담은 Dto 리스트
     */
    List<GetCouponMonthResponseDto> getCouponMonths() throws IOException;
}
