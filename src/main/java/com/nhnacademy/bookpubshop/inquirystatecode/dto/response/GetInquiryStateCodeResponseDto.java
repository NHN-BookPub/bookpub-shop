package com.nhnacademy.bookpubshop.inquirystatecode.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 상품문의코드를 조회하기 위한 dto.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class GetInquiryStateCodeResponseDto {
    private Integer inquiryCodeNo;

    private String inquiryCodeName;

}
