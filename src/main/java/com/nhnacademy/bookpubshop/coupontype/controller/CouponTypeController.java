package com.nhnacademy.bookpubshop.coupontype.controller;

import com.nhnacademy.bookpubshop.coupontype.dto.response.GetCouponTypeResponseDto;
import com.nhnacademy.bookpubshop.coupontype.service.CouponTypeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 쿠폰유형 Rest Api 컨트롤러.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@RestController
@RequiredArgsConstructor
public class CouponTypeController {

    private final CouponTypeService couponTypeService;

    /**
     * 쿠폰유형 단건 조회.
     *
     * @param typeNo 쿠폰유형번호
     * @return GetCouponTypeResponseDto 를 담은 response entity
     */
    @GetMapping("/token/coupon-types/{typeNo}")
    public ResponseEntity<GetCouponTypeResponseDto> couponTypeDetail(
            @PathVariable("typeNo") Long typeNo) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(couponTypeService.getCouponType(typeNo));
    }

    /**
     * 쿠폰유형 리스트 조회.
     *
     * @return GetCouponTypeResponseDto 리스트를 담은 Response entity
     */
    @GetMapping("/token/coupon-types")
    public ResponseEntity<List<GetCouponTypeResponseDto>> couponTypeList() {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(couponTypeService.getCouponTypes());
    }
}
