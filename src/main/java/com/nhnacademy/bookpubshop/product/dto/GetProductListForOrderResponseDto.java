package com.nhnacademy.bookpubshop.product.dto;

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
    private Long productNo;
    private String thumbnailPath;
    private String title;
    private Long salesPrice;
    private Integer productAmount;
}
