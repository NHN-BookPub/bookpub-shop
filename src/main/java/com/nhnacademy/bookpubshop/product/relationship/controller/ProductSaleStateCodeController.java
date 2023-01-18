package com.nhnacademy.bookpubshop.product.relationship.controller;

import com.nhnacademy.bookpubshop.product.relationship.dto.CreateProductSaleStateCodeRequestDto;
import com.nhnacademy.bookpubshop.product.relationship.dto.GetProductSaleStateCodeResponseDto;
import com.nhnacademy.bookpubshop.product.relationship.service.ProductSaleStateCodeService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 상품판매정책코드에 관련된 컨트롤러입니다.
 *
 * @author : 여운석
 * @since : 1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/state/productSale")
public class ProductSaleStateCodeController {
    private final ProductSaleStateCodeService productSaleStateCodeService;

    /**
     * 모든 정책코드를 조회하는 api.
     *
     * @return 성공시 200과 모든 정책코드의 리스트가 반환됩니다.
     */
    @GetMapping
    public ResponseEntity<List<GetProductSaleStateCodeResponseDto>> getAllSaleStateCode() {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(productSaleStateCodeService.getAllProductSaleStateCode());
    }

    /**
     * 상품판매정책코드를 생성하는 api.
     *
     * @param requestDto 생성에 사용하는 Dto.
     * @return 성공시 201 반환합니다.
     */
    @PostMapping
    public ResponseEntity<Void> createProductSaleStateCode(
            @Valid @RequestBody CreateProductSaleStateCodeRequestDto requestDto) {
        productSaleStateCodeService.createSaleCode(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .build();
    }

    /**
     * 정책코드 번호로 조회하는 api.
     *
     * @param codeNo 정책코드번호입니다.
     * @return 성공시 200, 객체를 반환합니다.
     */
    @GetMapping("/{codeNo}")
    public ResponseEntity<GetProductSaleStateCodeResponseDto> getProductSaleStateCodeById(
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
    @PutMapping("/{codeNo}")
    public ResponseEntity<Void> setUsedSaleCodeById(
            @PathVariable Integer codeNo,
            @RequestParam boolean used) {
        productSaleStateCodeService.setUsedSaleCodeById(codeNo, used);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .build();
    }
}