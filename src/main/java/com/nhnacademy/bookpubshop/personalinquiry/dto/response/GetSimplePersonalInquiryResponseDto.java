package com.nhnacademy.bookpubshop.personalinquiry.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 1대1문의 간단한 정보 조회시 정보를 담을 dto.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class GetSimplePersonalInquiryResponseDto {
    private Long inquiryNo;
    private String memberNickname;
    private String inquiryTitle;
    private boolean inquiryAnswered;
    private LocalDateTime createdAt;
}
