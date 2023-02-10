package com.nhnacademy.bookpubshop.product.dto.request;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 상품 카테고리 수정을 위한 dto.
 *
 * @author : 박경서
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
public class ModifyProductCategoryRequestDto {
    @NotNull(message = "카테고리 번호는 필수입니다.")
    private List<Integer> categoriesNo;
}
