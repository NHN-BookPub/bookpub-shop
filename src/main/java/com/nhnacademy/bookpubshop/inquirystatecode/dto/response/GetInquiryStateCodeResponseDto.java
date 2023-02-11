package com.nhnacademy.bookpubshop.inquirystatecode.dto.response;

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
public class GetInquiryStateCodeResponseDto {
    private Integer inquiryCodeNo;

    private String inquiryCodeName;

}
