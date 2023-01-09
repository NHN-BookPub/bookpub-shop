package com.nhnacademy.bookpubshop.product.relationship.dto;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

/**
 * 상품유형상태코드 생성시 사용할 Dto.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class CreateProductTypeStateCodeRequestDto {
    @NotNull
    @Length(min = 1, max = 20, message = "유형종류는 20자를 넘을 수 없습니다.")
    private String codeName;
    @NotNull
    private boolean codeUsed;
    @Length(min = 1, max = 100, message = "100자를 초과하였습니다.")
    private String codeInfo;
}
