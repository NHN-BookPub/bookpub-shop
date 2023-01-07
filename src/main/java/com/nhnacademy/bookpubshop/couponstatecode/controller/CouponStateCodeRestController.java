package com.nhnacademy.bookpubshop.couponstatecode.controller;

import com.nhnacademy.bookpubshop.couponstatecode.dto.GetCouponStateCodeResponseDto;
import com.nhnacademy.bookpubshop.couponstatecode.service.CouponStateCodeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 쿠폰상태코드 Rest Api 컨트롤러.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CouponStateCodeRestController {
    private final CouponStateCodeService couponStateCodeService;

    /**
     * 쿠폰상태코드 단건 조회 Get.
     *
     * @param codeNo 쿠폰상태코드 번호
     * @return 쿠폰상태코드 적용타겟을 ResponseEntity 에 담아 반환합니다
     */
    @GetMapping("/coupon-state-codes/{codeNo}")
    public ResponseEntity<GetCouponStateCodeResponseDto> couponStateCodeDetail(
            @PathVariable("codeNo") Integer codeNo) {
        return new ResponseEntity<>(
                couponStateCodeService.getCouponStateCode(codeNo), HttpStatus.OK);
    }

    /**
     * Gets coupon state codes.
     *
     * @return 쿠폰상태코드 적용타겟 리스트를 ResponseEntity 에 담아 반환합니다.
     */
    @GetMapping("/coupon-state-codes")
    public ResponseEntity<List<GetCouponStateCodeResponseDto>> couponStateCodeList() {
        return new ResponseEntity<>(couponStateCodeService.getCouponStateCodes(), HttpStatus.OK);
    }
}
