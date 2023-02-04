package com.nhnacademy.bookpubshop.reviewpolicy.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 상품평 정책 등록을 위한 Dto.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
public class CreateReviewPolicyRequestDto {

    @NotNull(message = "리뷰지급포인트를 입력해주세요.")
    @PositiveOrZero(message = "지급포인트는 0원 이상이어야합니다.")
    private Long sendPoint;
}
