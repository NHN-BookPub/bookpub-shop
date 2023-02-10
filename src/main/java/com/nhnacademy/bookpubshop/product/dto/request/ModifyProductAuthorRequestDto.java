package com.nhnacademy.bookpubshop.product.dto.request;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 작가 번호 dto.
 *
 * @author : 박경서
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
public class ModifyProductAuthorRequestDto {
    @NotNull(message = "저자 번호는 필수입니다.")
    private List<Integer> authors;
}
