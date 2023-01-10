package com.nhnacademy.bookpubshop.purchase.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;

/**
 * 구매이력 저장시 사용하는 dto class.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class SavePurchaseRequestDto {
    private Long productNo;
    private Long purchasePrice;
    private LocalDateTime createdAt;
    private Long purchaseAmount;
}
