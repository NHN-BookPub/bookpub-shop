package com.nhnacademy.bookpubshop.inquiry.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 모든 상품문의 조회 시, 간단한 상품문의 정보를 담기 위한 dto.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetInquirySummaryResponseDto {
    private Long inquiryNo;
    private Long productNo;
    private Long memberNo;
    private String inquiryStateCodeName;
    private String memberNickname;
    private String productTitle;
    private String inquiryTitle;
    private boolean inquiryDisplayed;
    private boolean inquiryAnswered;
    private LocalDateTime createdAt;
}
