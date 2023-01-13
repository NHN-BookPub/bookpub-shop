package com.nhnacademy.bookpubshop.purchase.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 구매이력 조회시 사용되는 dto class.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class GetPurchaseListResponseDto {
    private Long productNo;
    private Long purchaseNo;
    private Integer purchaseAmount;
    private Long purchasePrice;
}
