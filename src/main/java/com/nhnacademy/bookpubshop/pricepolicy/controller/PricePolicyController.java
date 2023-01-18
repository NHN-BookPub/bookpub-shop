package com.nhnacademy.bookpubshop.pricepolicy.controller;

import com.nhnacademy.bookpubshop.pricepolicy.dto.CreatePricePolicyRequestDto;
import com.nhnacademy.bookpubshop.pricepolicy.dto.GetPricePolicyResponseDto;
import com.nhnacademy.bookpubshop.pricepolicy.service.PricePolicyService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 가격정책 컨트롤러입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/state/pricePolicy")
public class PricePolicyController {
    private final PricePolicyService pricePolicyService;

    /**
     * 모든 가격 정책을 반환합니다.
     *
     * @return 200, 가격정책 리스트 반환.
     */
    @GetMapping
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
    @PostMapping
    public ResponseEntity<Void> createPolicy(CreatePricePolicyRequestDto request) {
        pricePolicyService.createPricePolicy(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    /**
     * 정책의 가격을 수정합니다.
     *
     * @param policyNo 정책번호.
     * @param fee 수정할 가격.
     * @return 201 반환.
     */
    @PutMapping("/{policyNo}")
    public ResponseEntity<Void> modifyPolicy(@PathVariable Integer policyNo,
                                             @RequestParam Long fee) {
        pricePolicyService.modifyPricePolicyFee(policyNo, fee);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    /**
     * 정책번호로 조회합니다.
     *
     * @param policyNo 정책번호.
     * @return 200, 단건 정책 반환.
     */
    @GetMapping("{policyNo}")
    public ResponseEntity<GetPricePolicyResponseDto> getPolicyByNo(
            @PathVariable Integer policyNo) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(pricePolicyService.getPricePolicyById(policyNo));
    }
}
