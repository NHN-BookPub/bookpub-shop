package com.nhnacademy.bookpubshop.inquiry.service;

import com.nhnacademy.bookpubshop.inquiry.dto.request.CreateInquiryRequestDto;

/**
 * 상품문의 서비스 인터페이스.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public interface InquiryService {

    void createInquiry(Long memberNo, CreateInquiryRequestDto request);
}
