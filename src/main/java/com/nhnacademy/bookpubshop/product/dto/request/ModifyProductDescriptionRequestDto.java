package com.nhnacademy.bookpubshop.product.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * 상품 설명 수정 dto.
 *
 * @author : 박경서
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
public class ModifyProductDescriptionRequestDto {
    @NotBlank(message = "설명은 비어있으면 안됩니다.")
    @Length(max = 5000, message = "5000자를 넘길 수 없습니다.")
    private String description;
}
