package com.nhnacademy.bookpubshop.point.dto.request;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 포인트 선물 request dto.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@NoArgsConstructor
@Getter
public class PointGiftRequestDto {
    @NotNull
    private String nickname;
    @NotNull
    private Long pointAmount;
}
