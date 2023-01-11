package com.nhnacademy.bookpubshop.product.relationship.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 상품저자생성시 반환하는 Dto.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class CreateProductAuthorResponseDto {
    private String productTitle;
    private String authorName;
}
