package com.nhnacademy.bookpubshop.personalinquiry.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 1대1문의 상세 조회시 정보를 담을 dto.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetPersonalInquiryResponseDto {
    private Long inquiryNo;
    private String memberNickname;
    private String inquiryTitle;
    private String inquiryContent;
    private boolean inquiryAnswered;
    private LocalDateTime createdAt;
    private Long inquiryAnswerNo;
    private String inquiryAnswerContent;
    private LocalDateTime answerCreatedAt;
}
