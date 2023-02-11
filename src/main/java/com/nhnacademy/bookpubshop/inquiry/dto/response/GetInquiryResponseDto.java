package com.nhnacademy.bookpubshop.inquiry.dto.response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Some description here.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
public class GetInquiryResponseDto {
    private Long inquiryNo;
    private Long productNo;
    private Long memberNo;
    private String inquiryStateCodeName;
    private String memberNickname;
    private String productTitle;
    private String inquiryTitle;
    private String inquiryContent;
    private boolean inquiryDisplayed;
    private boolean inquiryAnswered;
    private List<GetInquiryResponseDto> childInquiries = new ArrayList<>();
    private LocalDateTime createdAt;

    public void addChild(List<GetInquiryResponseDto> childInquiries) {
        this.childInquiries = childInquiries;
    }
}
