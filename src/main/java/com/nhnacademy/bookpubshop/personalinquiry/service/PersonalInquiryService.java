package com.nhnacademy.bookpubshop.personalinquiry.service;

import com.nhnacademy.bookpubshop.personalinquiry.dto.request.CreatePersonalInquiryRequestDto;
import com.nhnacademy.bookpubshop.personalinquiry.dto.response.GetPersonalInquiryResponseDto;
import com.nhnacademy.bookpubshop.personalinquiry.dto.response.GetSimplePersonalInquiryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Some description here.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public interface PersonalInquiryService {
    void createPersonalInquiry(CreatePersonalInquiryRequestDto createDto);

    void deletePersonalInquiry(Long personalInquiryNo);

    Page<GetSimplePersonalInquiryResponseDto> getMemberPersonalInquiries(Pageable pageable, Long memberNo);

    Page<GetSimplePersonalInquiryResponseDto> getPersonalInquiries(Pageable pageable);

    GetPersonalInquiryResponseDto getPersonalInquiry(Long inquiryNo);
}
