package com.nhnacademy.bookpubshop.pricepolicy.controller;

import com.nhnacademy.bookpubshop.annotation.AdminAuth;
import com.nhnacademy.bookpubshop.pricepolicy.dto.request.CreatePricePolicyRequestDto;
import com.nhnacademy.bookpubshop.pricepolicy.dto.response.GetOrderPolicyResponseDto;
import com.nhnacademy.bookpubshop.pricepolicy.dto.response.GetPricePolicyResponseDto;
import com.nhnacademy.bookpubshop.pricepolicy.service.PricePolicyService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 가격정책 컨트롤러입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@RestController
@RequiredArgsConstructor
public class PricePolicyController {
    private final PricePolicyService pricePolicyService;

    /**
     * 모든 가격 정책을 반환합니다.
     *
     * @return 200, 가격정책 리스트 반환.
     */
    @GetMapping("/api/state/pricepolicies")
    public ResponseEntity<List<GetPricePolicyResponseDto>> getAllPolicies() {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(pricePolicyService.getPricePolicies());
    }

    /**
     * 정책을 등록합니다.
     *
     * @param request 등록을 위한 Dto.
     * @return 201 반환.
     */
    @PostMapping("/token/state/pricepolicies")
    @AdminAuth
    public ResponseEntity<Void> createPolicy(@Valid @RequestBody CreatePricePolicyRequestDto request) {
        pricePolicyService.createPricePolicy(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    /**
     * 정책이름으로 조회합니다.
     *
     * @param policyName 정책명.
     * @return 200, 정책리스트.
     */
    @GetMapping("/api/state/pricepolicies/{policyName}")
    public ResponseEntity<List<GetPricePolicyResponseDto>> getPoliciesByName(
            @PathVariable String policyName) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(pricePolicyService.getPricePoliciesByName(policyName));
    }

    /**
     * 주문에 사용할 정책을 불러오는 메소드.
     *
     * @return 포장비, 배송비 정책 반환.
     */
    @GetMapping("/api/state/pricepolicies/order")
    public ResponseEntity<List<GetOrderPolicyResponseDto>> getShipAndPackPolicy() {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(pricePolicyService.getOrderRequestPolicy());
    }

}
