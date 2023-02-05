package com.nhnacademy.bookpubshop.couponstatecode.controller;

import com.nhnacademy.bookpubshop.annotation.AdminAuth;
import com.nhnacademy.bookpubshop.couponstatecode.dto.GetCouponStateCodeResponseDto;
import com.nhnacademy.bookpubshop.couponstatecode.service.CouponStateCodeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 쿠폰상태코드 Rest Api 컨트롤러.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@RestController
@RequiredArgsConstructor
public class CouponStateCodeRestController {
    private final CouponStateCodeService couponStateCodeService;

    /**
     * 쿠폰상태코드 단건 조회 Get.
     *
     * @param codeNo 쿠폰상태코드 번호
     * @return 쿠폰상태코드 적용타겟을 ResponseEntity 에 담아 반환합니다
     */
    @AdminAuth
    @GetMapping("/token/coupon-state-codes/{codeNo}")
    public ResponseEntity<GetCouponStateCodeResponseDto> couponStateCodeDetail(
            @PathVariable("codeNo") Integer codeNo) {

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(couponStateCodeService.getCouponStateCode(codeNo));
    }

    /**
     * 쿠폰상태코드 리스트 조회.
     *
     * @return 쿠폰상태코드 적용타겟 리스트를 ResponseEntity 에 담아 반환합니다.
     */
    @AdminAuth
    @GetMapping("/token/coupon-state-codes")
    public ResponseEntity<List<GetCouponStateCodeResponseDto>> couponStateCodeList() {

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(couponStateCodeService.getCouponStateCodes());
    }
}
