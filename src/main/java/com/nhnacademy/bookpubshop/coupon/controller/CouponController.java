package com.nhnacademy.bookpubshop.coupon.controller;

import com.nhnacademy.bookpubshop.coupon.dto.request.CreateCouponRequestDto;
import com.nhnacademy.bookpubshop.coupon.dto.request.ModifyCouponRequestDto;
import com.nhnacademy.bookpubshop.coupon.dto.response.GetCouponResponseDto;
import com.nhnacademy.bookpubshop.coupon.service.CouponService;
import com.nhnacademy.bookpubshop.utils.PageResponse;
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
import org.springframework.web.bind.annotation.RequestMapping;
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
     *
     * @return 성공 경우 200, 태그 페이지 응답
     */
    @GetMapping("/coupons")
    public ResponseEntity<PageResponse<GetCouponResponseDto>> couponList(Pageable pageable) {
        Page<GetCouponResponseDto> content = couponService.getCoupons(pageable);

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
    public ResponseEntity<GetCouponResponseDto> couponList(
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
    public ResponseEntity<Void> couponAdd(CreateCouponRequestDto request) {
        couponService.createCoupon(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    /**
     * 쿠폰 수정을 하는 메서드입니다.
     *
     * @param request 쿠폰 수정을 위한 쿠폰 정보 Dto
     * @return 성공 경우 201 응답
     */
    @PutMapping("/coupons/modify")
    public ResponseEntity<Void> couponAdd(ModifyCouponRequestDto request) {
        couponService.modifyCouponUsed(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }
}
