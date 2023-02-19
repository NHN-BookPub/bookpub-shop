package com.nhnacademy.bookpubshop.personalinquiry.dummy;

import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.personalinquiry.entity.PersonalInquiry;

/**
 * 1:1문의 더미클래스입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
public class PersonalInquiryDummy {

    public static PersonalInquiry dummy(Member member) {

        return PersonalInquiry.builder()
                .member(member)
                .inquiryTitle("testTitle")
                .inquiryContent("testContent")
                .build();
    }

    public static PersonalInquiry dummy2(Member member) {

        return new PersonalInquiry(
                1L,
                member,
                "title",
                "content",
                true,
                false
        );
    }
}
