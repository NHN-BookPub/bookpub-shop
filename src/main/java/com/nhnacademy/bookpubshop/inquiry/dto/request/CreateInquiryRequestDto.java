package com.nhnacademy.bookpubshop.inquiry.dto.request;

import lombok.Getter;

/**
 * Some description here.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Getter
public class CreateInquiryRequestDto {
    Long inquiryParentNo;
    Long productNo;
    Integer inquiryStateCodeNo;
    String inquiryContent;
    boolean inquiryDisplayed;
    boolean inquiryAnswered;

    //문의 일시, 문의 번호, 회원 번호 제거됨
    //회원번호는 pathvariable 로 받아 넣고 문의 번호 및 일시는 자동 default값 insert
}
