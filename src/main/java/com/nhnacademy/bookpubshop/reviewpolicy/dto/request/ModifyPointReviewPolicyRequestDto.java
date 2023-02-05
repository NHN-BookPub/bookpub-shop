package com.nhnacademy.bookpubshop.reviewpolicy.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 상품평 정책 수정을 위한 Dto.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
public class ModifyPointReviewPolicyRequestDto {

    @NotNull(message = "수정할 상품평 정책 번호를 입력하세요")
    private Integer policyNo;

    @NotNull(message = "수정할 리뷰지급포인트를 입력하세요")
    @PositiveOrZero(message = "지급포인트는 0원 이상이어야합니다")
    private Long sendPoint;
}
