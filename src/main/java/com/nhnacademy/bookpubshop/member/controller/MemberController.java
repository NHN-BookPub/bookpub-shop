package com.nhnacademy.bookpubshop.member.controller;

import com.nhnacademy.bookpubshop.member.dto.request.LoginMemberRequestDto;
import com.nhnacademy.bookpubshop.member.dto.request.ModifyMemberEmailRequestDto;
import com.nhnacademy.bookpubshop.member.dto.request.ModifyMemberNicknameRequestDto;
import com.nhnacademy.bookpubshop.member.dto.request.SignUpMemberRequestDto;
import com.nhnacademy.bookpubshop.member.dto.response.LoginMemberResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.MemberDetailResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.MemberResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.SignUpMemberResponseDto;
import com.nhnacademy.bookpubshop.member.service.MemberService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 회원 정보를 다루는 컨트롤러 입니다.
 *
 * @author : 임태원, 유호철
 * @since : 1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {
    private final MemberService memberService;

    /**
     * 회원정보를 받고 저장하는 메소드.
     *
     * @param memberDto 회원정보가 입력.
     * @return 회원정보 저장성공 or 실패정보가 담긴 엔티티 반환.
     */
    @PostMapping("/signup")
    public ResponseEntity<SignUpMemberResponseDto> signup(
            @Valid @RequestBody SignUpMemberRequestDto memberDto) {
        SignUpMemberResponseDto memberInfo = memberService.signup(memberDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(memberInfo);
    }

    /**
     * 닉네임 변경시 사용되는 메서드입니다.
     * 성공시 201 반환.
     *
     * @param memberNo   멤버 번호가 기입된다.
     * @param requestDto 수정할 닉네임이 기입.
     * @return response entity
     */
    @PutMapping("/members/{memberNo}/nickName")
    public ResponseEntity<Void> memberModifyNickname(
            @PathVariable("memberNo") Long memberNo,
            @Valid @RequestBody ModifyMemberNicknameRequestDto requestDto) {
        memberService.modifyMemberNickName(memberNo, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    /**
     * 이메일 변경시 사용되는 메서드 입니다.
     * 성공시 201 반환됩니다.
     *
     * @param memberNo   멤버번호가 반환됩니다.
     * @param requestDto 수정할 이메일이 기입.
     * @return response entity
     */
    @PutMapping("/members/{memberNo}/email")
    public ResponseEntity<Void> memberModifyEmail(
            @PathVariable("memberNo") Long memberNo,
            @Valid @RequestBody ModifyMemberEmailRequestDto requestDto) {
        memberService.modifyMemberEmail(memberNo, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    /**
     * 멤버에대한 상세정보가 반환됩니다.
     * 성공시 200 반환.
     *
     * @param memberNo 멤버 번호가 기입.
     * @return response entity
     */
    @GetMapping("/members/{memberNo}")
    public ResponseEntity<MemberDetailResponseDto> memberDetails(
            @PathVariable("memberNo") Long memberNo) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(memberService.getMemberDetails(memberNo));
    }

    /**
     * 멤버에 대한 전체 정보가 반환 관리자만 접근가능.
     * 성공시 200 반환.
     *
     * @param pageable 페이징 정보.
     * @return response entity
     */
    @GetMapping("/admin/members")
    public ResponseEntity<Page<MemberResponseDto>> memberList(Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(memberService.getMembers(pageable));
    }

    /**
     * 단일 멤버에대한 차단 및 복구를 수행할 수 있습니다.
     *
     * @param memberNo 멤버 정보가 기입.
     * @return response entity
     */
    @PutMapping("/admin/members/{memberNo}")
    public ResponseEntity<Void> memberBlock(@PathVariable("memberNo") Long memberNo) {
        memberService.blockMember(memberNo);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginMemberResponseDto> memberLogin(
            @RequestBody LoginMemberRequestDto loginMemberRequestDto) {
        String memberId = loginMemberRequestDto.getMemberId();

        LoginMemberResponseDto loginInfo = memberService.loginMember(memberId);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(loginInfo);
    }

}
