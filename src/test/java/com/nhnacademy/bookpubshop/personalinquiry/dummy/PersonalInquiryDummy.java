package com.nhnacademy.bookpubshop.personalinquiry.dummy;

import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.personalinquiry.entity.PersonalInquiry;
import java.time.LocalDateTime;

/**
 * 1:1문의 더미클래스입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
public class PersonalInquiryDummy {

    public static PersonalInquiry dummy(Member member) {

        return new PersonalInquiry(
                null,
                member,
                "test_title",
                "test_content",
                "test_image",
                false,
                LocalDateTime.now(),
                false);
    }
}
