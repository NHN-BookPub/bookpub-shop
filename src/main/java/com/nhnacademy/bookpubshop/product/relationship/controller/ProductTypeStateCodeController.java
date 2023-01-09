package com.nhnacademy.bookpubshop.product.relationship.controller;

import com.nhnacademy.bookpubshop.product.relationship.dto.CreateProductTypeStateCodeRequestDto;
import com.nhnacademy.bookpubshop.product.relationship.dto.GetProductTypeStateCodeResponseDto;
import com.nhnacademy.bookpubshop.product.relationship.service.ProductTypeStateCodeService;
import java.util.List;
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
@RequestMapping("api/state/productType")
public class ProductTypeStateCodeController {
    private final ProductTypeStateCodeService productTypeStateCodeService;

    /**
     * 생성자입니다.
     */
    public ProductTypeStateCodeController(ProductTypeStateCodeService productTypeStateCodeService) {
        this.productTypeStateCodeService = productTypeStateCodeService;
    }

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
     * 유형을 수정하는 api 입니다.
     *
     * @param codeNo 유형번호입니다.
     * @param requestDto 수정에 사용할 Dto 클래스입니다.
     * @return 성공시 200, 수정된 객체를 반환합니다.
     */
    @PostMapping("/{codeNo}")
    public ResponseEntity<GetProductTypeStateCodeResponseDto> modifyTypeCodeById(
            @PathVariable Integer codeNo,
            @RequestBody CreateProductTypeStateCodeRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(productTypeStateCodeService.modifyTypeStateCode(codeNo, requestDto));
    }

    /**
     * 유형코드 사용여부를 수정하는 api.
     *
     * @param codeNo 유형코드번호입니다.
     * @param used   사용여부입니다.
     * @return 성공시 200, 객체를 반환합니다.
     */
    @DeleteMapping("/{codeNo}")
    public ResponseEntity<GetProductTypeStateCodeResponseDto> setUsedTypeCodeById(
            @PathVariable Integer codeNo,
            @RequestParam boolean used) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(productTypeStateCodeService.setUsedTypeCodeById(codeNo, used));
    }
}