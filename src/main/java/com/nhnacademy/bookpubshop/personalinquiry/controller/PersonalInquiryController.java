package com.nhnacademy.bookpubshop.personalinquiry.controller;

import com.nhnacademy.bookpubshop.annotation.AdminAuth;
import com.nhnacademy.bookpubshop.annotation.MemberAndAuth;
import com.nhnacademy.bookpubshop.annotation.MemberAuth;
import com.nhnacademy.bookpubshop.personalinquiry.dto.request.CreatePersonalInquiryRequestDto;
import com.nhnacademy.bookpubshop.personalinquiry.dto.response.GetPersonalInquiryResponseDto;
import com.nhnacademy.bookpubshop.personalinquiry.dto.response.GetSimplePersonalInquiryResponseDto;
import com.nhnacademy.bookpubshop.personalinquiry.service.PersonalInquiryService;
import com.nhnacademy.bookpubshop.utils.PageResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 1대1 문의를 다루기 위한 컨트롤러입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Slf4j
@RestController
@RequiredArgsConstructor
public class PersonalInquiryController {
    private final PersonalInquiryService personalInquiryService;

    @MemberAuth
    @PostMapping("/token/personal-inquiries/members/{memberNo}")
    public ResponseEntity<Void> personalInquiryAdd(@Valid @RequestBody CreatePersonalInquiryRequestDto createDto) {
        personalInquiryService.createPersonalInquiry(createDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @MemberAuth
    @PutMapping("/token/personal-inquiries/{personalInquiryNo}/members/{memberNo}/cancel")
    public ResponseEntity<Void> personalInquiryDelete(
            @PathVariable("personalInquiryNo") Long personalInquiryNo) {
        personalInquiryService.deletePersonalInquiry(personalInquiryNo);

        return ResponseEntity.ok().build();
    }

    @AdminAuth
    @GetMapping("/token/personal-inquiries")
    public ResponseEntity<PageResponse<GetSimplePersonalInquiryResponseDto>> personalInquiryList(
            @PageableDefault Pageable pageable) {
        Page<GetSimplePersonalInquiryResponseDto> personalInquiries =
                personalInquiryService.getPersonalInquiries(pageable);

        return ResponseEntity.ok()
                .body(new PageResponse<>(personalInquiries));
    }

    @MemberAuth
    @GetMapping("/token/personal-inquiries/members/{memberNo}")
    public ResponseEntity<PageResponse<GetSimplePersonalInquiryResponseDto>> memberPersonalInquiryList(
            @PageableDefault Pageable pageable,
            @PathVariable("memberNo") Long memberNo) {
        Page<GetSimplePersonalInquiryResponseDto> personalInquiries =
                personalInquiryService.getMemberPersonalInquiries(pageable, memberNo);

        return ResponseEntity.ok()
                .body(new PageResponse<>(personalInquiries));
    }

    @MemberAndAuth
    @GetMapping("/token/personal-inquiries/{inquiryNo}/members/{memberNo}")
    public ResponseEntity<GetPersonalInquiryResponseDto> personalInquiryDetail(
            @PathVariable("inquiryNo") Long inquiryNo) {
        GetPersonalInquiryResponseDto personalInquiry =
                personalInquiryService.getPersonalInquiry(inquiryNo);

        return ResponseEntity.ok().body(personalInquiry);
    }
}
