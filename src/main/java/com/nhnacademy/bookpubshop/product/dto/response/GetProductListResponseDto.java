package com.nhnacademy.bookpubshop.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

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
    private String title;
    private String thumbnail;
    private Integer productStock;
    private Long salesPrice;
    private Integer saleRate;
    private boolean productSubscribed;
    private Long productPrice;
    private boolean deleted;
}