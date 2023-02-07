package com.nhnacademy.bookpubshop.product.relationship.controller;

import com.nhnacademy.bookpubshop.annotation.AdminAuth;
import com.nhnacademy.bookpubshop.product.relationship.dto.GetProductTypeStateCodeResponseDto;
import com.nhnacademy.bookpubshop.product.relationship.service.ProductTypeStateCodeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 상품유형코드 controller입니다.
 *
 * @author : 여운석
 * @since : 1.0
 */
@RestController
@RequiredArgsConstructor
public class ProductTypeStateCodeController {
    private final ProductTypeStateCodeService productTypeStateCodeService;

    /**
     * 모든 유형코드를 조회하는 api.
     *
     * @return 성공시 200, 모든 유형코드가 담긴 리스트를 반환합니다.
     */
    @GetMapping("/token/state/productType")
    @AdminAuth
    public ResponseEntity<List<GetProductTypeStateCodeResponseDto>> typeCodeList() {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(productTypeStateCodeService.getAllTypeStateCodes());
    }

    /**
     * 사용중인 상품 유형 코드를 받는 API.
     *
     * @return 사용중인 모든 유형 코드
     */
    @GetMapping("/api/state/productType/used")
    public ResponseEntity<List<GetProductTypeStateCodeResponseDto>> typeCodesUsedList() {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(productTypeStateCodeService.getAllTypeStateCodesUsed());
    }

    /**
     * 유형번호로 조회하는 api.
     *
     * @param codeNo 유형 번호입니다.
     * @return 성공시 200, 객체를 반환합니다.
     */
    @GetMapping("/api/state/productType/{codeNo}")
    public ResponseEntity<GetProductTypeStateCodeResponseDto> typeCodeDetails(
            @PathVariable Integer codeNo) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(productTypeStateCodeService.getTypeStateCodeById(codeNo));
    }

    /**
     * 유형코드 사용여부를 수정하는 api.
     *
     * @param codeNo 유형코드번호입니다.
     * @param used   사용여부입니다.
     * @return 성공시 201, 객체를 반환합니다.
     */
    @PutMapping("/token/state/productType/{codeNo}")
    @AdminAuth
    public ResponseEntity<Void> typeCodeModifyUsed(
            @PathVariable Integer codeNo,
            @RequestParam boolean used) {
        productTypeStateCodeService.setUsedTypeCodeById(codeNo, used);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .build();
    }
}