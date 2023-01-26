package com.nhnacademy.bookpubshop.coupon.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 쿠폰 등록을 위한 DTO 객체입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
public class CreateCouponRequestDto {
    @NotNull(message = "쿠폰템플릿 번호를 입력해주세요.")
    private Long templateNo;

    @NotBlank(message = "멤버 아이디를 입력해주세요.")
    private String memberId;
}
