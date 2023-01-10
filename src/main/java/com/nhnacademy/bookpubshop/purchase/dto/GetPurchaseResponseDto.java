package com.nhnacademy.bookpubshop.purchase.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;

/**
 * 구매이력 조회시 반환되는 dto.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class GetPurchaseResponseDto {
    private Long purchaseNo;
    private Long productNo;
    private Long purchasePrice;
    private LocalDateTime createdAt;
    private Long purchaseAmount;
}
