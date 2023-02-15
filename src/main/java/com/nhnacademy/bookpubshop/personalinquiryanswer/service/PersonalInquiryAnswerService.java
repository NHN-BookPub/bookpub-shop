package com.nhnacademy.bookpubshop.personalinquiryanswer.service;

import com.nhnacademy.bookpubshop.personalinquiryanswer.dto.request.CreatePersonalInquiryAnswerRequestDto;

/**
 * Some description here.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public interface PersonalInquiryAnswerService {
    void createPersonalInquiryAnswer(CreatePersonalInquiryAnswerRequestDto createDto);

    void deletePersonalInquiryAnswer(Long answerNo);
}
