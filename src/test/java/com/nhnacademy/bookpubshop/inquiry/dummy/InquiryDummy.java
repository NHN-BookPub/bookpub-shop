package com.nhnacademy.bookpubshop.inquiry.dummy;

import com.nhnacademy.bookpubshop.inquiry.entity.Inquiry;
import com.nhnacademy.bookpubshop.inquirystatecode.entity.InquiryStateCode;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.product.entity.Product;

/**
 * Some description here.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public class InquiryDummy {
    public static Inquiry dummy(Inquiry parentInquiry, Member member,
                                Product product, InquiryStateCode inquiryStateCode) {
        return new Inquiry(
                null,
                parentInquiry,
                member,
                product,
                inquiryStateCode,
                "title",
                "content",
                true,
                false,
                null
        );
    }

}