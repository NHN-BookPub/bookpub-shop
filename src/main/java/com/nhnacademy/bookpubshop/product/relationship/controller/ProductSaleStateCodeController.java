package com.nhnacademy.bookpubshop.product.relationship.controller;

import com.nhnacademy.bookpubshop.annotation.AdminAuth;
import com.nhnacademy.bookpubshop.product.relationship.dto.GetProductSaleStateCodeResponseDto;
import com.nhnacademy.bookpubshop.product.relationship.service.ProductSaleStateCodeService;
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
 * 상품판매정책코드에 관련된 컨트롤러입니다.
 *
 * @author : 여운석, 박경서
 * @since : 1.0
 */
@RestController
@RequiredArgsConstructor
public class ProductSaleStateCodeController {
    private final ProductSaleStateCodeService productSaleStateCodeService;

    /**
     * 모든 정책코드를 조회하는 api.
     *
     * @return 성공시 200과 모든 정책코드의 리스트가 반환됩니다.
     */
    @GetMapping("/token/state/productSale")
    @AdminAuth
    public ResponseEntity<List<GetProductSaleStateCodeResponseDto>> productSaleStateCodeList() {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(productSaleStateCodeService.getAllProductSaleStateCode());
    }

    /**
     * 사용중인 모든 정책 코드를 조회하는 api.
     *
     * @return 사용중인 코드
     */
    @GetMapping("/api/state/productSale/used")
    public ResponseEntity<List<GetProductSaleStateCodeResponseDto>> productSaleStateCodeUsedList() {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(productSaleStateCodeService.getAllProductSaleStateCodeUsed());
    }

    /**
     * 정책코드 번호로 조회하는 api.
     *
     * @param codeNo 정책코드번호입니다.
     * @return 성공시 200, 객체를 반환합니다.
     */
    @GetMapping("/api/state/productSale/{codeNo}")
    public ResponseEntity<GetProductSaleStateCodeResponseDto> productSaleStateCodeDetails(
            @PathVariable Integer codeNo) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(productSaleStateCodeService.getSaleCodeById(codeNo));
    }


    /**
     * 판매정책을 사용 여부를 수정하는 api.
     *
     * @param codeNo 정책번호입니다.
     * @param used   사용여부입니다.
     * @return 성공시 201
     */
    @PutMapping("/token/state/productSale/{codeNo}")
    @AdminAuth
    public ResponseEntity<Void> productSaleStateCodeModifyUsed(
            @PathVariable Integer codeNo,
            @RequestParam boolean used) {
        productSaleStateCodeService.setUsedSaleCodeById(codeNo, used);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .build();
    }

}