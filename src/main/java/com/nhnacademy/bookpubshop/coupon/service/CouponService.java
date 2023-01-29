package com.nhnacademy.bookpubshop.coupon.service;

import com.nhnacademy.bookpubshop.coupon.dto.request.CreateCouponRequestDto;
import com.nhnacademy.bookpubshop.coupon.dto.response.GetCouponResponseDto;
import com.nhnacademy.bookpubshop.coupon.dto.response.GetOrderCouponResponseDto;
import java.io.IOException;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Coupon 서비스를 위한 인터페이스.
 *
 * @author : 정유진
 * @since : 1.0
 */
public interface CouponService {

    /**
     * 쿠폰 생성을 위한 메서드.
     *
     * @param createRequestDto 쿠폰 생성에 필요한 정보를 담은 Dto
     */
    void createCoupon(CreateCouponRequestDto createRequestDto);

    /**
     * 쿠폰 사용여부 수정을 위한 메서드.
     *
     * @param couponNo 수정할 쿠폰 번호
     */
    void modifyCouponUsed(Long couponNo);

    /**
     * 쿠폰 단건 조회를 위한 메서드.
     *
     * @param couponNo 조회할 쿠폰 번호
     * @return GetCouponResponseDto 쿠폰 조회에 필요한 정보를 담은 Dto
     */
    GetCouponResponseDto getCoupon(Long couponNo);

    /**
     * 쿠폰 전체조회를 위한 메서드.
     *
     * @param pageable 조회할 쿠폰 페이지 정보
     * @return 조회된 쿠폰 페이지
     */
    Page<GetCouponResponseDto> getCoupons(Pageable pageable, String searchKey, String search) throws IOException;

    /**
     * 주문에 사용될 쿠폰 리스트 조회를 위한 메서드.
     *
     * @param memberNo      멤버 번호
     * @param productNoList 상품 번호 리스트
     * @return 쿠폰 정보를 담은 dto 리스트
     */
    List<GetOrderCouponResponseDto> getOrderCoupons(Long memberNo, List<Long> productNoList);
}
