package com.nhnacademy.bookpubshop.wishlist.dto.request;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 위시리스트 수정을위한 Dto.
 *
 * @author : 박경서
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
public class ModifyWishlistAlarmRequestDto {
    @NotNull(message = "빈 값은 안됩니다.")
    private Long productNo;
}
