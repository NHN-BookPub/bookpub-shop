package com.nhnacademy.bookpubshop.coupon.repository;

import com.nhnacademy.bookpubshop.coupon.dto.response.GetCouponResponseDto;
import com.nhnacademy.bookpubshop.coupon.dto.response.GetOrderCouponResponseDto;
import com.nhnacademy.bookpubshop.coupon.entity.Coupon;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * CouponRepository custom 을 위한 레포.
 *
 * @author : 정유진, 임태원
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
    Optional<GetCouponResponseDto> findByCouponNo(Long couponNo);

    /**
     * 쿠폰 리스트 페이지를 조회하는 메서드.
     *
     * @return 쿠폰 조회 Dto 페이지
     */
    Page<GetCouponResponseDto> findAllBy(Pageable pageable, String searchKey, String search);

    /**
     * 멤버 사용가능한 쿠폰 리스트 조회를 위한 메서드.
     *
     * @param pageable 페이지
     * @param memberNo 멤버 번호
     * @return 쿠폰 정보를 담은 DTO 리스트
     */
    Page<GetCouponResponseDto> findPositiveCouponByMemberNo(Pageable pageable, Long memberNo);

    /**
     * 멤버 사용 불가능한 쿠폰 리스트 조회를 위한 메서드.
     *
     * @param pageable 페이지
     * @param memberNo 멤버 번호
     * @return 쿠폰 정보를 담은 DTO 리스트
     */
    Page<GetCouponResponseDto> findNegativeCouponByMemberNo(Pageable pageable, Long memberNo);

    /**
     * 멤버의 등급쿠폰 발급 유무를 확인하는 메서드입니다.
     *
     * @param memberNo    멤버 번호
     * @param tierCoupons 등급 쿠폰 리스트
     * @return 발급 유무
     */
    boolean existsTierCouponsByMemberNo(Long memberNo, List<Long> tierCoupons);

    /**
     * 주문에 사용될 쿠폰들을 조회하는 메서드입니다.
     *
     * @param memberNo      멤버 번호
     * @param productNoList 상품 번호 리스트
     * @return 주문에 사용될 쿠폰들의 정보를 담은 Dto 리스트
     */
    List<GetOrderCouponResponseDto> findByProductNo(Long memberNo, Long productNoList);

    /**
     * 멤버의 이달의 쿠폰 발급 여부를 확인하는 메서드입니다.
     *
     * @param memberNo   멤버 번호
     * @param templateNo 쿠폰 템플릿 번호
     * @return 발급 유무
     */
    boolean existsMonthCoupon(Long memberNo, Long templateNo);

    /**
     * 멤버의 이달의 쿠폰 리스트 발행 여부를 확인하는 메서드입니다.
     *
     * @param memberNo   멤버 번호
     * @param couponList 이달의 쿠폰 번호 리스트
     * @return 발급된 쿠폰 템플릿 번호
     */
    List<Long> existsMonthCouponList(Long memberNo, List<Long> couponList);

    /**
     * 주문번호로 해당 주문에 사용 된 쿠폰을 가져오는 메소드.
     *
     * @return 해당 주문에 사용 된 쿠폰.
     */
    List<Coupon> findByCouponByOrderNo(Long orderNo);

    /**
     * 주문 상품번호로 해당 주문상품에 사용 된 쿠폰을 가져오는 메소드.
     *
     * @return 해당 주문상품에 사용 된 쿠폰.
     */
    List<Coupon> findByCouponByOrderProductNo(Long orderProductNo);
}
