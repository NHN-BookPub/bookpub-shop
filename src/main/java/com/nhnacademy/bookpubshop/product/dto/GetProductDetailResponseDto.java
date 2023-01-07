package com.nhnacademy.bookpubshop.product.dto;

import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;

/**
 * 상품 상세페이지를 위한 Dto 입니다.
 * ProductPolicy, ProductTypeStateCode, ProductSaleStateCode
 * 위 3가지 클래스를 포함하고 있습니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class GetProductDetailResponseDto {
    private String title;
    private ProductPolicy productPolicy;
    private ProductTypeStateCode productTypeStateCode;
    private ProductSaleStateCode productSaleStateCode;
    private String productThumbnail;
    private String productIsbn;
    private String productDescription;
    private Long salesPrice;
    private Long viewCount;
    private Integer productStock;
    private LocalDateTime createdAt;
}
