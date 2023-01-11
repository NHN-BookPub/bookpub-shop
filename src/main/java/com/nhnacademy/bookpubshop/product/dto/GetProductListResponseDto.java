package com.nhnacademy.bookpubshop.product.dto;

/**
 * 상품 간단 리스트를 위한 Dto 입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
public class GetProductListResponseDto {
    private Long productNo;
    private String thumbnailPath;
    private String title;
    private Long productStock;
    private Long salesPrice;
    private Integer saleRate;
}