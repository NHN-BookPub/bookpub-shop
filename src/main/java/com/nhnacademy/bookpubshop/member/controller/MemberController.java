package com.nhnacademy.bookpubshop.member.controller;

import com.nhnacademy.bookpubshop.member.dto.MemberSignupResponse;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
    public ResponseEntity<?> signup(@RequestBody MemberSignupResponse memberSignupResponse) {
        Member member = memberService.signup(memberSignupResponse);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        MultiValueMap<String, String> memberInfo = new LinkedMultiValueMap<>();
        memberInfo.add("member_id", member.getMemberId());
        memberInfo.add("member_nickname", member.getMemberNickname());
        memberInfo.add("member_email", member.getMemberEmail());
        memberInfo.add("member_tier", member.getTier().getTierName());

        return ResponseEntity.status(HttpStatus.CREATED)
                .headers(headers)
                .body(memberInfo);

    }
}
