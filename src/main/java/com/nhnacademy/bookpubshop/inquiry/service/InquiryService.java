package com.nhnacademy.bookpubshop.inquiry.service;

import com.nhnacademy.bookpubshop.inquiry.dto.request.CreateInquiryRequestDto;
import com.nhnacademy.bookpubshop.inquiry.dto.response.GetInquiryResponseDto;
import com.nhnacademy.bookpubshop.inquiry.dto.response.GetInquirySummaryMemberResponseDto;
import com.nhnacademy.bookpubshop.inquiry.dto.response.GetInquirySummaryProductResponseDto;
import com.nhnacademy.bookpubshop.inquiry.dto.response.GetInquirySummaryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 상품문의 서비스 인터페이스.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public interface InquiryService {
    boolean verifyWritableInquiry(Long memberNo, Long productNo);

    void createInquiry(Long memberNo, CreateInquiryRequestDto request);

//    String addInquiryImage(MultipartFile image) throws IOException;

    void deleteInquiryAnswer(Long inquiryNo);

    void deleteInquiry(Long inquiryNo);

    void modifyCompleteInquiry(Long inquiryNo);

    Page<GetInquirySummaryProductResponseDto> getSummaryInquiriesByProduct(Pageable pageable, Long productNo);

    Page<GetInquirySummaryResponseDto> getSummaryInquiries(Pageable pageable);

    Page<GetInquirySummaryMemberResponseDto> getMemberInquiries(Pageable pageable, Long memberNo);

    GetInquiryResponseDto getInquiry(Long inquiryNo);
}
