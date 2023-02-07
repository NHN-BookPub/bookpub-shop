package com.nhnacademy.bookpubshop.couponpolicy.controller;

import com.nhnacademy.bookpubshop.annotation.AdminAuth;
import com.nhnacademy.bookpubshop.couponpolicy.dto.request.CreateCouponPolicyRequestDto;
import com.nhnacademy.bookpubshop.couponpolicy.dto.request.ModifyCouponPolicyRequestDto;
import com.nhnacademy.bookpubshop.couponpolicy.dto.response.GetCouponPolicyResponseDto;
import com.nhnacademy.bookpubshop.couponpolicy.service.CouponPolicyService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 쿠폰정책에 관한 컨트롤러입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@RestController
@RequiredArgsConstructor
public class CouponPolicyController {

    private final CouponPolicyService couponPolicyService;

    /**
     * 쿠폰정책 등록을 위한 메소드입니다.
     *
     * @param request CreateCouponPolicyRequestDto 쿠폰정책 등록을 위한 값 기입
     * @return 성공시 응답코드 CREATED 201이 반환된다.
     */
    @AdminAuth
    @PostMapping("/token/coupon-policies")
    public ResponseEntity<Void> couponPolicyAdd(
            @Valid @RequestBody CreateCouponPolicyRequestDto request) {
        couponPolicyService.addCouponPolicy(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    /**
     * 쿠폰정책 수정을 위한 메소드입니다.
     *
     * @param request ModifyCouponPolicyRequestDto 쿠폰정책 수정을 위한 값 기입
     * @return 성공시 응답코드 CREATED 201이 반환된다.
     */
    @AdminAuth
    @PutMapping("/token/coupon-policies")
    public ResponseEntity<Void> couponPolicyModify(
            @Valid @RequestBody ModifyCouponPolicyRequestDto request) {
        couponPolicyService.modifyCouponPolicy(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .build();
    }

    /**
     * 쿠폰정책 단건 조회를 위한 메소드입니다.
     *
     * @param policyNo 쿠폰정책번호
     * @return 성공했을시 응답코드 OK 200이 반환되고, 쿠폰정책 조회 정보값이 담긴다.
     */
    @AdminAuth
    @GetMapping("/token/coupon-policies/{policyNo}")
    public ResponseEntity<GetCouponPolicyResponseDto> couponPolicyDetail(
            @PathVariable("policyNo") Integer policyNo) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(couponPolicyService.getCouponPolicy(policyNo));
    }

    /**
     * 쿠폰정책 리스트 조회를 위한 메소드입니다.
     *
     * @return the 성공했을시 응답코드 OK 200이 반환되고, 쿠폰정책 조회 정보값 리스트가 담긴다.
     */
    @AdminAuth
    @GetMapping("/token/coupon-policies")
    public ResponseEntity<List<GetCouponPolicyResponseDto>> couponPolicyList() {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(couponPolicyService.getCouponPolicies());
    }
}
