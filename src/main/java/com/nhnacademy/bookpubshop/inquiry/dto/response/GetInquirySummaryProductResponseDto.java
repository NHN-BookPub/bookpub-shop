package com.nhnacademy.bookpubshop.inquiry.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Some description here.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class GetInquirySummaryProductResponseDto {
    private Long inquiryNo;
    private Long productNo;
    private Long memberNo;
    private String inquiryStateCodeName;
    private String memberNickname;
    private String inquiryTitle;
    private boolean inquiryDisplayed;
    private boolean inquiryAnswered;
    private LocalDateTime createdAt;
}
