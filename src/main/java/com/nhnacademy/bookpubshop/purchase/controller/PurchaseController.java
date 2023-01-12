package com.nhnacademy.bookpubshop.purchase.controller;

import com.nhnacademy.bookpubshop.purchase.dto.GetPurchaseResponseDto;
import com.nhnacademy.bookpubshop.purchase.dto.SavePurchaseRequestDto;
import com.nhnacademy.bookpubshop.purchase.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 구매이력에 관련된 controller 입니다.
 *
 * @author : 여운석
 * @since : 1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/purchase")
public class PurchaseController {
    private final PurchaseService purchaseService;

    /**
     * 상품번호로 구매이력을 조회하는 api.
     *
     * @param productNo 상품번호입니다.
     * @return 구매이력 리스트가 반환됩니다.
     */
    @GetMapping("/{productNo}")
    public ResponseEntity<List<GetPurchaseResponseDto>> getPurchaseByProductNo(
            @PathVariable Long productNo) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(purchaseService.getPurchaseByProductNo(productNo));
    }

    /**
     * 구매이력을 수정하는 api.
     *
     * @param purchaseNo  구매이력번호입니다.
     * @param request 수정시 사용되는 dto 입니다.
     * @return 수정된 객체가 반환됩니다.
     */
    @PutMapping("/{purchaseNo}")
    public ResponseEntity<GetPurchaseResponseDto> modifyPurchase(
            @PathVariable Long purchaseNo,
            @RequestBody SavePurchaseRequestDto request) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(purchaseService.modifyPurchase(purchaseNo, request));
    }

    /**
     * 구매이력을 생성하는 api.
     *
     * @param request 생성시 사용되는 dto class.
     * @return 생성된 객체를 반환합니다.
     */
    @PostMapping
    public ResponseEntity<GetPurchaseResponseDto> createPurchase(
            @RequestBody SavePurchaseRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(purchaseService.createPurchase(request));
    }
}
