package com.nhnacademy.bookpubshop.product.relationship.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 상품판매유형코드 반환에 사용하는 dto.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetProductTypeStateCodeResponseDto {
    private Integer codeNo;
    private String codeName;
    private boolean codeUsed;
    private String codeInfo;
}
