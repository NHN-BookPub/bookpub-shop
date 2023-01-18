package com.nhnacademy.bookpubshop.product.relationship.controller;

import com.nhnacademy.bookpubshop.product.relationship.dto.CreateProductTypeStateCodeRequestDto;
import com.nhnacademy.bookpubshop.product.relationship.dto.GetProductTypeStateCodeResponseDto;
import com.nhnacademy.bookpubshop.product.relationship.service.ProductTypeStateCodeService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
@RequestMapping("/api/state/productType")
public class ProductTypeStateCodeController {
    private final ProductTypeStateCodeService productTypeStateCodeService;

    /**
     * 모든 유형코드를 조회하는 api.
     *
     * @return 성공시 200, 모든 유형코드가 담긴 리스트를 반환합니다.
     */
    @GetMapping
    public ResponseEntity<List<GetProductTypeStateCodeResponseDto>> getAllTypeCodes() {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(productTypeStateCodeService.getAllTypeStateCodes());
    }

    /**
     * 사용중인 상품 유형 코드를 받는 API.
     *
     * @return 사용중인 모든 유형 코드
     */
    @GetMapping("/used")
    public ResponseEntity<List<GetProductTypeStateCodeResponseDto>> getAllTypeCodesUsed() {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(productTypeStateCodeService.getAllTypeStateCodesUsed());
    }

    /**
     * 유형 코드를 등록하는 api.
     *
     * @param requestDto 요청할 dto를 받습니다.
     * @return 성공시 201 반환.
     */
    @PostMapping
    public ResponseEntity<Void> createTypeCode(
            @Valid @RequestBody CreateProductTypeStateCodeRequestDto requestDto) {
        productTypeStateCodeService.createTypeStateCode(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .build();
    }

    /**
     * 유형번호로 조회하는 api.
     *
     * @param codeNo 유형 번호입니다.
     * @return 성공시 200, 객체를 반환합니다.
     */
    @GetMapping("/{codeNo}")
    public ResponseEntity<GetProductTypeStateCodeResponseDto> getTypeCodeById(
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
    @DeleteMapping("/{codeNo}")
    public ResponseEntity<Void> setUsedTypeCodeById(
            @PathVariable Integer codeNo,
            @RequestParam boolean used) {
        productTypeStateCodeService.setUsedTypeCodeById(codeNo, used);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .build();
    }
}