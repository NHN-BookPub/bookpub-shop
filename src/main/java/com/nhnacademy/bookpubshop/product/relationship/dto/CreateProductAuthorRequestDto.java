package com.nhnacademy.bookpubshop.product.relationship.dto;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 상품저자생성시 사용하는 Dto.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
public class CreateProductAuthorRequestDto {
    @NotNull
    private Long productNo;
    @NotNull
    private Integer authorNo;
}
