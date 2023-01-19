package com.nhnacademy.bookpubshop.coupontemplate.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * front로 파일을 보내기 위해 변환한 Dto.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RestGetCouponTemplateResponseDto {
    private Long templateNo;
    private String templateName;
    private String templateImage;
    private LocalDateTime issuedAt;
    private LocalDateTime finishedAt;
}
