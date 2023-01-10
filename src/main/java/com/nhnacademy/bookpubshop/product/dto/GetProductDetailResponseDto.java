package com.nhnacademy.bookpubshop.product.dto;

import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

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
    private Long productNo;
    private String productIsbn;
    private String title;
    private Integer pageCount;
    private String productDescription;
    private String productThumbnail;
    private Long salesPrice;
    private Integer salesRate;
    private Integer productPriority;
    private Integer productStock;
    private LocalDateTime publishDate;
    private boolean productSubscribed;
    private ProductSaleStateCode saleCode;
    private ProductTypeStateCode typeCode;
    private ProductPolicy productPolicy;
}
