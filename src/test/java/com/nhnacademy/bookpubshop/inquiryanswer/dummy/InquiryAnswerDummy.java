package com.nhnacademy.bookpubshop.inquiryanswer.dummy;

import com.nhnacademy.bookpubshop.inquiryanswer.entity.InquiryAnswer;
import com.nhnacademy.bookpubshop.personalinquiry.entity.PersonalInquiry;
import java.time.LocalDateTime;

/**
 * Some description here.
 *
 * @author : 유호철
 * @since : 1.0
 **/
public class InquiryAnswerDummy {

    public static InquiryAnswer dummy(PersonalInquiry personalInquiry) {
        return InquiryAnswer.builder()
                .answerContent("test_content")
                .personalInquiry(personalInquiry)
                .build();
    }
}
