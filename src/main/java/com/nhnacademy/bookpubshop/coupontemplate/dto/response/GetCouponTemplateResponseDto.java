package com.nhnacademy.bookpubshop.coupontemplate.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 쿠폰템플릿 조회를 위한 DTO 객체입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetCouponTemplateResponseDto {
    private String templateName;
    private String templateImage;
    private LocalDateTime issuedAt;
    private LocalDateTime finishedAt;
}
