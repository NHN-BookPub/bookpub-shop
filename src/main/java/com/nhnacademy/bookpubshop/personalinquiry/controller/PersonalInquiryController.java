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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    /**
     * 1대1문의를 생성하기 위한 메서드입니다.
     * 성공 시 201 반환.
     *
     * @param createDto 1대1문의 생성 시 필요한 정보를 담은 Dto
     * @return the response entity
     */
    @MemberAuth
    @PostMapping("/token/personal-inquiries/members/{memberNo}")
    public ResponseEntity<Void> personalInquiryAdd(
            @Valid @RequestBody CreatePersonalInquiryRequestDto createDto) {
        personalInquiryService.createPersonalInquiry(createDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 1대1문의를 삭제하기 위한 메서드입니다.
     * 성공 시 200 반환.
     *
     * @param personalInquiryNo 삭제할 1대1문의 번호
     * @return the response entity
     */
    @MemberAuth
    @DeleteMapping("/token/personal-inquiries/{personalInquiryNo}/members/{memberNo}")
    public ResponseEntity<Void> personalInquiryDelete(
            @PathVariable("personalInquiryNo") Long personalInquiryNo) {
        personalInquiryService.deletePersonalInquiry(personalInquiryNo);

        return ResponseEntity.ok().build();
    }

    /**
     * 1대1문의 전체 리스트를 조회하기 위한 메서드입니다.
     * 성공 시 200 반환.
     *
     * @param pageable 페이징 정보
     * @return the response entity
     */
    @AdminAuth
    @GetMapping("/token/personal-inquiries")
    public ResponseEntity<PageResponse<GetSimplePersonalInquiryResponseDto>> personalInquiryList(
            @PageableDefault Pageable pageable) {
        Page<GetSimplePersonalInquiryResponseDto> personalInquiries =
                personalInquiryService.getPersonalInquiries(pageable);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new PageResponse<>(personalInquiries));
    }

    /**
     * 해당 회원의 1대1문의 리스트를 조회하기 위한 메서드입니다.
     * 성공 시 200 반환.
     *
     * @param pageable 페이징 정보
     * @param memberNo 회원 번호
     * @return the response entity
     */
    @MemberAuth
    @GetMapping("/token/personal-inquiries/members/{memberNo}")
    public ResponseEntity<PageResponse<GetSimplePersonalInquiryResponseDto>>
        memberPersonalInquiryList(@PageableDefault Pageable pageable,
                              @PathVariable("memberNo") Long memberNo) {
        Page<GetSimplePersonalInquiryResponseDto> personalInquiries =
                personalInquiryService.getMemberPersonalInquiries(pageable, memberNo);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new PageResponse<>(personalInquiries));
    }

    /**
     * 1대1문의 단건 상세 조회를 위한 메서드입니다.
     * 성공 시 200 반환.
     *
     * @param inquiryNo 조회할 1대1문의 번호
     * @return the response entity
     */
    @MemberAndAuth
    @GetMapping("/token/personal-inquiries/{inquiryNo}/members/{memberNo}")
    public ResponseEntity<GetPersonalInquiryResponseDto> personalInquiryDetail(
            @PathVariable("inquiryNo") Long inquiryNo) {
        GetPersonalInquiryResponseDto personalInquiry =
                personalInquiryService.getPersonalInquiry(inquiryNo);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(personalInquiry);
    }
}
