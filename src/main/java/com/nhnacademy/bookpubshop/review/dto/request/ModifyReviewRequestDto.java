package com.nhnacademy.bookpubshop.review.dto.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 상품평 수정을 위한 Dto.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
public class ModifyReviewRequestDto {
    @NotNull(message = "별점을 입력해주세요")
    @Min(value = 1, message = "별점은 1점 이상이어야합니다")
    @Max(value = 5, message = "별점은 5점 이하여야합니다")
    private Integer reviewStar;
    @NotBlank(message = "상품평 내용을 입력해주세요")
    @Size(max = 500, message = "내용은 500자 이하만 가능합니다")
    private String reviewContent;
}
