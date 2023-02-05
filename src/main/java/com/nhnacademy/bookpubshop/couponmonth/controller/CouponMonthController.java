package com.nhnacademy.bookpubshop.couponmonth.controller;

import com.nhnacademy.bookpubshop.annotation.AdminAuth;
import com.nhnacademy.bookpubshop.couponmonth.dto.request.CreateCouponMonthRequestDto;
import com.nhnacademy.bookpubshop.couponmonth.dto.request.ModifyCouponMonthRequestDto;
import com.nhnacademy.bookpubshop.couponmonth.dto.response.GetCouponMonthResponseDto;
import com.nhnacademy.bookpubshop.couponmonth.service.CouponMonthService;
import java.io.IOException;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 이달의쿠폰 RestController 입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@RestController
@RequiredArgsConstructor
public class CouponMonthController {
    private final CouponMonthService couponMonthService;

    /**
     * 이달의쿠폰을 등록하는 메서드입니다.
     *
     * @param request 등록할 정보를 담은 Dto
     * @return 성공 경우 201
     */
    @AdminAuth
    @PostMapping("/token/coupon-months")
    public ResponseEntity<Void> couponMonthAdd(
            @Valid @RequestBody CreateCouponMonthRequestDto request) {
        couponMonthService.createCouponMonth(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 이달의쿠폰을 수정하는 메서드입니다.
     *
     * @param request 수정할 정보를 담은 Dto
     * @return 성공 경우 201
     */
    @AdminAuth
    @PutMapping("/token/coupon-months")
    public ResponseEntity<Void> couponMonthModify(
            @Valid @RequestBody ModifyCouponMonthRequestDto request) {
        couponMonthService.modifyCouponMonth(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 이달의쿠폰을 삭제하는 메서드입니다.
     *
     * @return 성공 경우 200
     */
    @AdminAuth
    @DeleteMapping("/token/coupon-months/{monthNo}")
    public ResponseEntity<Void> couponMonthDelete(@PathVariable("monthNo") Long monthNo) {
        couponMonthService.deleteCouponMonth(monthNo);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 이달의쿠폰 전체 리스트를 조회하는 메서드입니다.
     *
     * @return 성공 경우 200, 이달의쿠폰 리스트 응답
     * @throws IOException 파일 입출력 에러
     */
    @GetMapping("/api/coupon-months")
    public ResponseEntity<List<GetCouponMonthResponseDto>> couponMonthList() throws IOException {

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(couponMonthService.getCouponMonths());
    }
}
