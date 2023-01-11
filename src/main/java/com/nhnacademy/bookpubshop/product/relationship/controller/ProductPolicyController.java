package com.nhnacademy.bookpubshop.product.relationship.controller;

import com.nhnacademy.bookpubshop.product.relationship.dto.CreateModifyProductPolicyRequestDto;
import com.nhnacademy.bookpubshop.product.relationship.dto.GetProductPolicyResponseDto;
import com.nhnacademy.bookpubshop.product.relationship.service.ProductPolicyService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 상품정책(포인트) controller 입니다.
 *
 * @author : 여운석
 * @since : 1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/policy/product")
public class ProductPolicyController {
    private final ProductPolicyService productPolicyService;

    /**
     * 상품생성을 위한 api 입니다.
     *
     * @param requestDto 상품을 등록하기 위한 Dto.
     * @return 성공시 201과 생성된 객체를 반환합니다.
     */
    @PostMapping
    public ResponseEntity<GetProductPolicyResponseDto> createProductPolicy(
            @Valid @RequestBody CreateModifyProductPolicyRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(productPolicyService.createProductPolicy(requestDto));

    }

    /**
     * 번호로 상품 포인트 정책을 확인합니다.
     *
     * @param policyNo 상품번호입니다.
     * @return 성공시 200, 찾은 객체를 반환합니다.
     */
    @GetMapping("/{policyNo}")
    public ResponseEntity<GetProductPolicyResponseDto> getProductPolicy(
            @PathVariable Integer policyNo) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(productPolicyService.getProductPolicyById(policyNo));
    }

    /**
     * 상품정책 수정을 위한 api 입니다.
     *
     * @param policyNo 정책번호입니다.
     * @param policy 수정을 위한 Dto 입니다.
     * @return response entity
     */
    @PutMapping("/{policyNo}")
    public ResponseEntity<GetProductPolicyResponseDto> modifyProductPolicy(
            @PathVariable Integer policyNo,
            @Valid @RequestBody CreateModifyProductPolicyRequestDto policy) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(productPolicyService.modifyProductPolicyById(policyNo, policy));
    }

    /**
     * 정책을 삭제하기 위한 api 입니다.
     *
     * @param policyNo 정책번호입니다.
     * @return 성공시 200을 반환합니다.
     */
    @DeleteMapping("/{policyNo}")
    public ResponseEntity<Void> deleteProductPolicy(@PathVariable Integer policyNo) {
        productPolicyService.deleteProductPolicyById(policyNo);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
