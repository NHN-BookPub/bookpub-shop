package com.nhnacademy.bookpubshop.inquiry.dto.response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class GetInquirySummaryResponseDto {
    private Long inquiryNo;
    private Long productNo;
    private Long memberNo;
    private String inquiryStateCodeName;
    private String memberNickname;
    private String productTitle;
    private String productIsbn;
    private List<String> productCategories = new ArrayList<>();
    private String productImagePath;
    private String inquiryTitle;
    private boolean inquiryDisplayed;
    private boolean inquiryAnswered;
    private LocalDateTime createdAt;

    public void addCategories(List<String> categories) {
        this.productCategories = categories;
    }
}
