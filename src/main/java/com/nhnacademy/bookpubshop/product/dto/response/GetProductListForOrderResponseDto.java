package com.nhnacademy.bookpubshop.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Some description here.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class GetProductListForOrderResponseDto {
    private Long orderProductNo;
    private Long productNo;
    private String title;
    private String thumbnail;
    private Long salesPrice;
    private Integer productAmount;
    private String stateCode;
}
