package com.nhnacademy.bookpubshop.purchase.controller;

import com.nhnacademy.bookpubshop.annotation.AdminAuth;
import com.nhnacademy.bookpubshop.purchase.dto.CreatePurchaseRequestDto;
import com.nhnacademy.bookpubshop.purchase.dto.GetPurchaseListResponseDto;
import com.nhnacademy.bookpubshop.purchase.service.PurchaseService;
import com.nhnacademy.bookpubshop.utils.PageResponse;
import com.nhnacademy.bookpubshop.wishlist.dto.response.GetAppliedMemberResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 구매이력에 관련된 controller 입니다.
 *
 * @author : 여운석
 * @since : 1.0
 */
@RestController
@RequiredArgsConstructor
public class PurchaseController {
    private final PurchaseService purchaseService;

    /**
     * 최신순으로 매입 이력을 반환합니다.
     * 페이징.
     *
     * @return 200, 최신순으로 매입 이력을 반환합니다.
     */
    @GetMapping("/token/purchases")
    @AdminAuth
    public ResponseEntity<PageResponse<GetPurchaseListResponseDto>> getPurchaseListDesc(
            Pageable pageable) {
        Page<GetPurchaseListResponseDto> content = purchaseService.getPurchaseListDesc(pageable);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new PageResponse<>(content));
    }

    /**
     * 상품번호로 구매이력을 조회하는 api.
     *
     * @param productNo 상품번호입니다.
     * @return 200, 구매이력 리스트가 반환됩니다.
     */
    @GetMapping("/token/purchases/{productNo}")
    @AdminAuth
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
     * @param purchaseNo 구매이력번호입니다.
     * @param request    수정시 사용되는 dto 입니다.
     * @return 201 반환.
     */
    @PutMapping("/token/purchases/{purchaseNo}")
    @AdminAuth
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
    @PostMapping("/token/purchases")
    @AdminAuth
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
    @PostMapping("/token/purchases/absorption")
    @AdminAuth
    public ResponseEntity<List<GetAppliedMemberResponseDto>> createPurchaseMerged(
            @RequestBody CreatePurchaseRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(purchaseService.createPurchaseMerged(request));
    }
}
