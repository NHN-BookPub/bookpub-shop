package com.nhnacademy.bookpubshop.coupon.service;

import com.fasterxml.jackson.core.JsonProcessingException;
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
    Page<GetCouponResponseDto> getCoupons(Pageable pageable, String searchKey, String search)
            throws IOException;

    /**
     * 주문에 사용될 쿠폰 리스트 조회를 위한 메서드.
     *
     * @param memberNo      멤버 번호
     * @param productNoList 상품 번호 리스트
     * @return 쿠폰 정보를 담은 dto 리스트
     */
    List<GetOrderCouponResponseDto> getOrderCoupons(Long memberNo, Long productNoList);

    /**
     * 멤버 사용가능한 쿠폰 리스트 조회를 위한 메서드.
     *
     * @param pageable 페이지
     * @param memberNo 멤버 번호
     * @return 쿠폰 정보를 담은 DTO 리스트
     */
    Page<GetCouponResponseDto> getPositiveCouponList(Pageable pageable, Long memberNo);

    /**
     * 멤버 사용 불가능한 쿠폰 리스트 조회를 위한 메서드.
     *
     * @param pageable 페이지
     * @param memberNo 멤버 번호.
     * @return 쿠폰 정보를 담은 DTO 리스트
     */
    Page<GetCouponResponseDto> getNegativeCouponList(Pageable pageable, Long memberNo);

    /**
     * 포인트쿠폰 사용 및 사용여부 수정을 위한 메서드.
     *
     * @param couponNo 쿠폰 번호
     * @param memberNo 회원 번호
     */
    void modifyPointCouponUsed(Long couponNo, Long memberNo);

    /**
     * 멤버의 등급쿠폰 발급 유무를 확인하는 메서드입니다.
     *
     * @param memberNo    멤버 번호
     * @param tierCoupons 등급 쿠폰 리스트
     * @return 발급 유무
     */
    boolean existsCouponsByMemberNo(Long memberNo, List<Long> tierCoupons);

    /**
     * 멤버에게 등급 쿠폰을 발급하는 메서드입니다.
     *
     * @param memberNo    멤버 번호
     * @param tierCoupons 등급 쿠폰 리스트
     */
    void issueTierCouponsByMemberNo(Long memberNo, List<Long> tierCoupons);

    /**
     * 이달의 쿠폰을 발행하는 메서드입니다.
     *
     * @param memberNo   멤버 번호
     * @param templateNo 쿠폰 템플릿 번호
     * @throws JsonProcessingException json error
     */
    void issueCouponMonth(Long memberNo, Long templateNo)
            throws JsonProcessingException;

    /**
     * 이달의 쿠폰 발행 여부를 확인하는 메서드입니다.
     *
     * @param memberNo   멤버 번호
     * @param templateNo 쿠폰 템플릿 번호
     * @return 쿠폰 발행 여부
     */
    boolean existsCouponMonthIssued(Long memberNo, Long templateNo);

    /**
     * 멤버의 이달의 쿠폰 리스트  발행 여부를 확인하는 메서드입니다.
     *
     * @param memberNo   멤버 번호
     * @param couponList 이달의 쿠폰 리스트
     * @return 이달의 쿠폰 발행 여부 리스트
     */
    List<Boolean> existsCouponMonthListIssued(Long memberNo, List<Long> couponList);
}
