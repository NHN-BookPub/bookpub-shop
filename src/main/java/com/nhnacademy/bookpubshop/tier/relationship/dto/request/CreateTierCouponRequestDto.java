package com.nhnacademy.bookpubshop.tier.relationship.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 등급 쿠폰 등록을 위한 DTO.
 *
 * @author : 김서현
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
public class CreateTierCouponRequestDto {

    @NotBlank(message = "쿠폰템플릿 번호를 입력해주세요.")
    private Long templateNo;

    @NotBlank(message = "등급 번호를 입력해주세요.")
    private Integer tierNo;

}
