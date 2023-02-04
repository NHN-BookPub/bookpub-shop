package com.nhnacademy.bookpubshop.tier.relationship.controller;

import com.nhnacademy.bookpubshop.annotation.AdminAuth;
import com.nhnacademy.bookpubshop.annotation.MemberAuth;
import com.nhnacademy.bookpubshop.tier.relationship.dto.request.CreateTierCouponRequestDto;
import com.nhnacademy.bookpubshop.tier.relationship.dto.response.GetTierCouponResponseDto;
import com.nhnacademy.bookpubshop.tier.relationship.service.impl.TierCouponServiceImpl;
import com.nhnacademy.bookpubshop.utils.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * TierCoupon Api RestController 입니다.
 *
 * @author : 김서현
 * @since : 1.0
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/token/tier-coupons")
public class TierCouponController {

    private final TierCouponServiceImpl tierCouponService;

    /**
     * 등급 쿠폰을 조회하기 위한 메서드입니다.
     *
     * @param pageable 페이지 정보
     * @return 성공 경우 200 응답
     */
    @GetMapping
    @MemberAuth
    public ResponseEntity<PageResponse<GetTierCouponResponseDto>> tierCouponList(
            Pageable pageable) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new PageResponse<>(tierCouponService.getTierCoupons(pageable)));
    }

    /**
     * 등급 쿠폰 등록을 하는 메서드입니다.
     *
     * @param request 등급 쿠폰을 생성하기 위한 정보
     * @return 성공 경우 200 응답
     */
    @PostMapping
    @AdminAuth
    public ResponseEntity<Void> tierCouponAdd(@RequestBody CreateTierCouponRequestDto request) {
        tierCouponService.createTierCoupon(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .build();
    }

    /**
     * 등급 쿠폰 삭제를 위한 메서드입니다.
     *
     * @param templateNo 쿠폰 템플릿 번호
     * @param tierNo     등급 번호
     * @return 성공 경우 200 응답
     */
    @DeleteMapping
    @AdminAuth
    public ResponseEntity<Void> tierCouponDelete(@RequestParam Long templateNo,
                                                 @RequestParam Integer tierNo) {

        tierCouponService.deleteTierCoupon(templateNo, tierNo);

        return ResponseEntity.ok()
                .build();
    }

}
