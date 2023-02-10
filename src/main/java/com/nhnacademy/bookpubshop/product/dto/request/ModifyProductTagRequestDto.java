package com.nhnacademy.bookpubshop.product.dto.request;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 상품 태그 수정 dto.
 *
 * @author : 박경서
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
public class ModifyProductTagRequestDto {
    @NotNull(message = "태그 번호는 필수입니다.")
    private List<Integer> tags;
}
