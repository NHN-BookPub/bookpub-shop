package com.nhnacademy.bookpubshop.product.dto.request;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 연관 관계 상품 dto.
 *
 * @author : 박경서
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
public class CreateRelationProductRequestDto {
    private List<Long> relationProducts;
}
