package com.nhnacademy.bookpubshop.product.relationship.dto;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * 상품판매유형코드 생성에 사용할 Dto.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
public class CreateProductSaleStateCodeRequestDto {
    @Length(min = 1, max = 20, message = "상품판매여부코드 길이는 20자를 넘을 수 없습니다.")
    private String codeCategory;
    @NotNull
    private boolean codeUsed;
    @Length(min = 1, max = 100, message = "100자를 초과하였습니다.")
    private String codeInfo;
}
