package com.nhnacademy.bookpubshop.purchase.controller;

import com.nhnacademy.bookpubshop.purchase.dto.CreatePurchaseRequestDto;
import com.nhnacademy.bookpubshop.purchase.dto.GetPurchaseListResponseDto;
import com.nhnacademy.bookpubshop.purchase.service.PurchaseService;
import com.nhnacademy.bookpubshop.utils.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 구매이력에 관련된 controller 입니다.
 *
 * @author : 여운석
 * @since : 1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/purchases")
public class PurchaseController {
    private final PurchaseService purchaseService;

    /**
     * 최신순으로 매입 이력을 반환합니다.
     * 페이징.
     *
     * @return 200, 최신순으로 매입 이력을 반환합니다.
     */
    @GetMapping
    public ResponseEntity<PageResponse<GetPurchaseListResponseDto>> getPurchaseListDesc(
            Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(purchaseService.getPurchaseListDesc(pageable));
    }

    /**
     * 상품번호로 구매이력을 조회하는 api.
     *
     * @param productNo 상품번호입니다.
     * @return 200, 구매이력 리스트가 반환됩니다.
     */
    @GetMapping("/{productNo}")
    public ResponseEntity<PageResponse<GetPurchaseListResponseDto>> getPurchaseByProductNo(
            @PathVariable Long productNo,
            Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(purchaseService.getPurchaseByProductNo(productNo, pageable));
    }

    /**
     * 구매이력을 수정하는 api.
     *
     * @param purchaseNo  구매이력번호입니다.
     * @param request 수정시 사용되는 dto 입니다.
     * @return 201 반환.
     */
    @PutMapping("/{purchaseNo}")
    public ResponseEntity<Void> modifyPurchase(
            @PathVariable Long purchaseNo,
            @RequestBody CreatePurchaseRequestDto request) {
        purchaseService.modifyPurchase(purchaseNo, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    /**
     * 구매이력을 생성하는 api.
     *
     * @param request 생성시 사용되는 dto class.
     * @return 201 반환.
     */
    @PostMapping
    public ResponseEntity<Void> createPurchase(
            @RequestBody CreatePurchaseRequestDto request) {
        purchaseService.createPurchase(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    /**
     * 매입이력 등록과 동시에 상품의 재고가 올라갑니다.
     *
     * @param request Dto 입니다.
     * @return 201 반환.
     */
    @PostMapping("/absorption")
    public ResponseEntity<Void> createPurchaseMerged(
            @RequestBody CreatePurchaseRequestDto request) {
        purchaseService.createPurchaseMerged(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }
}
