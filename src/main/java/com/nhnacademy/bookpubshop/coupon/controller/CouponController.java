package com.nhnacademy.bookpubshop.coupon.controller;

import com.nhnacademy.bookpubshop.coupon.dto.request.CreateCouponRequestDto;
import com.nhnacademy.bookpubshop.coupon.dto.response.GetCouponResponseDto;
import com.nhnacademy.bookpubshop.coupon.dto.response.GetOrderCouponResponseDto;
import com.nhnacademy.bookpubshop.coupon.service.CouponService;
import com.nhnacademy.bookpubshop.utils.PageResponse;
import java.io.IOException;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 쿠폰 RestController 입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CouponController {

    private final CouponService couponService;

    /**
     * 쿠폰 전체 리스트를 조회하는 메서드입니다.
     * 관리자만 사용하는 메서드입니다.
     *
     * @return 성공 경우 200, 태그 페이지 응답
     */
    @GetMapping("/coupons")
    public ResponseEntity<PageResponse<GetCouponResponseDto>> couponList(Pageable pageable,
                                                                         @RequestParam(value = "searchKey", required = false) String searchKey,
                                                                         @RequestParam(value = "search", required = false) String search) throws IOException {
        Page<GetCouponResponseDto> content = couponService.getCoupons(pageable, searchKey, search);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new PageResponse<>(content));
    }

    /**
     * 단건 쿠폰을 조회하는 메서드입니다.
     *
     * @param couponNo 쿠폰을 조회하기 위한 쿠폰번호
     * @return 성공 경우 200, 태그 정보 응답
     */
    @GetMapping("/coupons/{couponNo}")
    public ResponseEntity<GetCouponResponseDto> couponDetail(
            @PathVariable("couponNo") Long couponNo) {

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(couponService.getCoupon(couponNo));
    }

    /**
     * 쿠폰 등록을 하는 메서드입니다.
     *
     * @param request 쿠폰 생성을 위한 쿠폰 정보 Dto
     * @return 성공 경우 201 응답
     */
    @PostMapping("/coupons")
    public ResponseEntity<Void> couponAdd(@Valid @RequestBody CreateCouponRequestDto request) {
        couponService.createCoupon(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    /**
     * 쿠폰의 사용여부를 수정하기 위한 메소드입니다.
     *
     * @param couponNo 수정할 쿠폰 번호
     * @return 성공 경우 201 응답
     */
    @PutMapping("/coupons/{couponNo}/used")
    public ResponseEntity<Void> couponModifyUsed(@PathVariable("couponNo") Long couponNo) {
        couponService.modifyCouponUsed(couponNo);

        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    /**
     * 주문에 필요한 쿠폰을 조회하는 메서드입니다.
     *
     * @param memberNo  멤버 번호
     * @param productNo 상품 번호 리스트
     * @return 사용할 수 있는 쿠폰 리스트 반환
     */
    @GetMapping("/coupons/members/{memberNo}/order")
    public ResponseEntity<List<GetOrderCouponResponseDto>> orderCouponList(
            @PathVariable("memberNo") Long memberNo,
            @RequestParam("productNo") List<Long> productNo) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(couponService.getOrderCoupons(memberNo, productNo));
    }

    /**
     * 마이페이지에서 사용가능한 쿠폰을 조회하는 메소드입니다.
     *
     * @param memberNo 멤버번호
     * @param pageable 페이지
     * @return 사용할 수 있는 쿠폰 리스트 반환
     */
    @GetMapping("/coupons/members/{memberNo}/positive")
    public ResponseEntity<PageResponse<GetCouponResponseDto>> memberPositiveCouponList(
            @PathVariable("memberNo") Long memberNo, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new PageResponse<>(couponService.getPositiveCouponList(pageable, memberNo)));
    }

    /**
     * 마이페이지에서 사용불가능한 쿠폰을 조회하는 메소드입니다.
     *
     * @param memberNo 멤버번호
     * @param pageable 페이지
     * @return 사용불가능한 쿠폰 리스트 반환
     */
    @GetMapping("/coupons/members/{memberNo}/negative")
    public ResponseEntity<PageResponse<GetCouponResponseDto>> memberNegativeCouponList(
            @PathVariable("memberNo") Long memberNo, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new PageResponse<>(couponService.getNegativeCouponList(pageable, memberNo)));
    }
}