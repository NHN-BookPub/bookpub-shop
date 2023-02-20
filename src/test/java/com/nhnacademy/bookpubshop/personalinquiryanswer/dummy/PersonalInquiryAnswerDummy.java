package com.nhnacademy.bookpubshop.personalinquiryanswer.dummy;

import com.nhnacademy.bookpubshop.personalinquiry.entity.PersonalInquiry;
import com.nhnacademy.bookpubshop.personalinquiryanswer.entity.PersonalInquiryAnswer;

/**
 * 1대1 문의 답변 더미입니다.
 * 테스트 시 사용됩니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
public class PersonalInquiryAnswerDummy {

    public static PersonalInquiryAnswer dummy(PersonalInquiry personalInquiry) {
        return PersonalInquiryAnswer.builder()
                .answerContent("test_content")
                .personalInquiry(personalInquiry)
                .build();
    }

    public static PersonalInquiryAnswer dummy2(PersonalInquiry personalInquiry) {
        return new PersonalInquiryAnswer(
                1L,
                personalInquiry,
                "content"
        );
    }
}
