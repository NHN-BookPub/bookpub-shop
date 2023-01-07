package com.nhnacademy.bookpubshop.member.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookpubshop.member.dto.SignUpMemberRequestDto;
import com.nhnacademy.bookpubshop.member.dto.SignUpMemberResponseDto;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 회원 정보를 다루는 컨트롤러 입니다.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {
    private final MemberService memberService;

    /**
     * 회원정보를 받고 저장하는 메소드.
     *
     * @return 회원정보 저장성공 or 실패정보가 담긴 엔티티 반환.
     */
    @PostMapping("/signup")
    public ResponseEntity<String> signup(
            @RequestBody SignUpMemberRequestDto memberDto) throws JsonProcessingException {
        String defaultTier = "basic";

        Member member = memberService.signup(memberDto, defaultTier);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        SignUpMemberResponseDto memberInfo
                = new SignUpMemberResponseDto(
                member.getMemberId(),
                member.getMemberNickname(),
                member.getMemberEmail(),
                member.getTier().getTierName()
        );

        ObjectMapper objectMapper = new ObjectMapper();
        String memberInfoJson = objectMapper.writeValueAsString(memberInfo);

        return ResponseEntity.status(HttpStatus.CREATED)
                .headers(headers)
                .body(memberInfoJson);
    }
}
