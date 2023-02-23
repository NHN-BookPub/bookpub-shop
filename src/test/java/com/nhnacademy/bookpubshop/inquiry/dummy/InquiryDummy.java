package com.nhnacademy.bookpubshop.inquiry.dummy;

import com.nhnacademy.bookpubshop.inquiry.dto.response.GetInquiryResponseDto;
import com.nhnacademy.bookpubshop.inquiry.dto.response.GetInquirySummaryMemberResponseDto;
import com.nhnacademy.bookpubshop.inquiry.dto.response.GetInquirySummaryProductResponseDto;
import com.nhnacademy.bookpubshop.inquiry.dto.response.GetInquirySummaryResponseDto;
import com.nhnacademy.bookpubshop.inquiry.entity.Inquiry;
import com.nhnacademy.bookpubshop.inquirystatecode.entity.InquiryStateCode;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.product.entity.Product;
import java.time.LocalDateTime;
import org.springframework.test.util.ReflectionTestUtils;

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

    public static GetInquirySummaryProductResponseDto getSummaryDummy() {
        return new GetInquirySummaryProductResponseDto(1L, 1L, 1L,
                "name", "nickName", "title",
                true, true, LocalDateTime.now());
    }

    public static GetInquirySummaryResponseDto getErrorInquiryDummy() {
        return new GetInquirySummaryResponseDto(1L, 1L, 1L,
                "name", "name",
                "title", "title", true,
                true, LocalDateTime.now());
    }

    public static GetInquirySummaryMemberResponseDto getMemberDummy() {
        return new GetInquirySummaryMemberResponseDto(1L, 1L, 1L, "codeName"
                , "title", "path", "title", true, true, LocalDateTime.now());
    }

    public static GetInquiryResponseDto getInquiryDummy() {
        GetInquiryResponseDto dto = new GetInquiryResponseDto();
        ReflectionTestUtils.setField(dto,"inquiryNo",1L);
        ReflectionTestUtils.setField(dto,"productNo",1L);
        ReflectionTestUtils.setField(dto,"memberNo",1L);
        ReflectionTestUtils.setField(dto,"inquiryStateCodeName","code");
        ReflectionTestUtils.setField(dto,"memberNickname","nick");
        ReflectionTestUtils.setField(dto,"productTitle","title");
        ReflectionTestUtils.setField(dto,"inquiryTitle","title");
        ReflectionTestUtils.setField(dto,"inquiryContent","content");
        ReflectionTestUtils.setField(dto,"inquiryDisplayed",true);
        ReflectionTestUtils.setField(dto,"inquiryAnswered",true);
        ReflectionTestUtils.setField(dto, "createdAt", LocalDateTime.now());
        return dto;

    }
}