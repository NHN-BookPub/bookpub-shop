package com.nhnacademy.bookpubshop.purchase.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 구매이력 조회시 사용되는 dto class.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetPurchaseListResponseDto {
    private Long productNo;
    private String productTitle;
    private Long purchaseNo;
    private Integer purchaseAmount;
    private Long purchasePrice;
    private LocalDateTime createdAt;
}
