package com.nhnacademy.bookpubshop.inquirycode.dummy;

import com.nhnacademy.bookpubshop.inquirycode.entity.InquiryCode;

/**
 * 상품평 더미 클래스
 * 테스트시 해당 클래스를 쉽게 생성하기 위한 클래스 입니다.
 * 동료들과 협력하여 코딩할 때 유용합니다.
 *
 * @author : 임태원
 * @since : 1.0
 **/
public class InquiryCodeDummy {
    public static InquiryCode dummy() {
        return new InquiryCode(
                null,
                "name",
                true,
                "info"
        );
    }

}
