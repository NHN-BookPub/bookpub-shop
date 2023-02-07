package com.nhnacademy.bookpubshop.inquiry.controller;

import com.nhnacademy.bookpubshop.annotation.MemberAuth;
import com.nhnacademy.bookpubshop.inquiry.dto.request.CreateInquiryRequestDto;
import com.nhnacademy.bookpubshop.inquiry.service.InquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 상품문의를 다루기 위한 컨트롤러입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Controller
@RequiredArgsConstructor
public class InquiryController {
    private final InquiryService inquiryService;


    @MemberAuth
    @PostMapping("/token/inquiries/members/{memberNo}")
    public ResponseEntity<Void> inquiryAdd(@PathVariable Long memberNo,
                                           @RequestBody CreateInquiryRequestDto request) {
        inquiryService.createInquiry(memberNo, request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
