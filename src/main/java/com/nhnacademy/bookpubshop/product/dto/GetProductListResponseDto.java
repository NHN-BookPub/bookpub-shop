package com.nhnacademy.bookpubshop.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;

/**
 * 상품 간단 리스트를 위한 Dto 입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class GetProductListResponseDto {
    private Long productNo;
    private String thumbnailPath;
    private String title;
    private Integer productStock;
    private Long salesPrice;
    private Integer saleRate;
    private boolean deleted;
    private LocalDateTime publishedAt;
}