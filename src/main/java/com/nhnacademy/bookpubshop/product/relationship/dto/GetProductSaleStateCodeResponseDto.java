package com.nhnacademy.bookpubshop.product.relationship.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 상품판매유형코드 반환에 사용하는 dto.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class GetProductSaleStateCodeResponseDto {
    private Integer codeNo;
    private String codeCategory;
    private boolean codeUsed;
    private String codeInfo;
}
