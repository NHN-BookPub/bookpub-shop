package com.nhnacademy.bookpubshop.member.controller;

import com.nhnacademy.bookpubshop.member.dto.request.CreateAddressRequestDto;
import com.nhnacademy.bookpubshop.member.dto.request.IdRequestDto;
import com.nhnacademy.bookpubshop.member.dto.request.LoginMemberRequestDto;
import com.nhnacademy.bookpubshop.member.dto.request.ModifyMemberPasswordRequest;
import com.nhnacademy.bookpubshop.member.dto.request.ModifyMemberEmailRequestDto;
import com.nhnacademy.bookpubshop.member.dto.request.ModifyMemberNameRequestDto;
import com.nhnacademy.bookpubshop.member.dto.request.ModifyMemberNicknameRequestDto;
import com.nhnacademy.bookpubshop.member.dto.request.ModifyMemberPhoneRequestDto;
import com.nhnacademy.bookpubshop.member.dto.request.SignUpMemberRequestDto;
import com.nhnacademy.bookpubshop.member.dto.request.NickRequestDto;
import com.nhnacademy.bookpubshop.member.dto.response.LoginMemberResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.MemberDetailResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.MemberPasswordResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.MemberResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.MemberStatisticsResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.MemberTierStatisticsResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.SignUpMemberResponseDto;
import com.nhnacademy.bookpubshop.member.service.MemberService;
import com.nhnacademy.bookpubshop.utils.PageResponse;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
     * id 중복체크 요청을 받아 검사하는 메소드.
     *
     * @param requestDto front에서 요청한 id.
     * @return 중복이면 true, 아니면 false
     */
    @PostMapping("/signup/idCheck")
    public boolean idDuplicateCheck(@RequestBody IdRequestDto requestDto) {
        return memberService.idDuplicateCheck(requestDto.getId());
    }

    /**
     * 닉네임 중복체크 요청을 받아 검사하는 메소드.
     *
     * @param requestDto front에서 요청한 nickname.
     * @return 중복이면 true, 아니면 false
     */
    @PostMapping("/signup/nickCheck")
    public boolean nickDuplicateCheck(@RequestBody NickRequestDto requestDto) {
        return memberService.nickNameDuplicateCheck(requestDto.getNickname());
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
     * 회원만접근이 가능합니다.
     * 회원의 휴대전화를 수정할때 쓰이는 메서드입니다.
     * 성공시 201 을 반환합니다.
     *
     * @param memberNo   회원번호가 기입됩니다.
     * @param requestDto 휴대전화번호가 기입됩니다.
     * @return the response entity
     */
    @PutMapping("/members/{memberNo}/phone")
    public ResponseEntity<Void> memberModifyPhone(@PathVariable("memberNo") Long memberNo,
                                                  @Valid @RequestBody ModifyMemberPhoneRequestDto requestDto){
        memberService.modifyMemberPhone(memberNo, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    /**
     * 회원만접근이 가능합니다.
     * 회원의 휴대전화를 수정할때 쓰이는 메서드입니다.
     * 성공시 201 을 반환합니다.
     *
     * @param memberNo   회원번호가 기입됩니다.
     * @param requestDto 휴대전화번호가 기입됩니다.
     * @return the response entity
     */
    @PutMapping("/members/{memberNo}/name")
    public ResponseEntity<Void> memberModifyName(@PathVariable("memberNo") Long memberNo,
                                                  @Valid @RequestBody ModifyMemberNameRequestDto requestDto){
        memberService.modifyMemberName(memberNo, requestDto);
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
    public ResponseEntity<PageResponse<MemberResponseDto>> memberList(Pageable pageable) {
        Page<MemberResponseDto> members = memberService.getMembers(pageable);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new PageResponse<>(members));
    }

    /**
     * 멤버가 회원탈퇴를 했을경우 실행되는 메서드입니다.
     * 회원만 접근가능.
     * 성공시 201반환.
     *
     * @param memberNo 회원식별할수있는 번호.
     * @return 성공시 201
     */
    @PutMapping("/members/{memberNo}")
    public ResponseEntity<Void> memberDelete(@PathVariable("memberNo") Long memberNo){
        memberService.deleteMember(memberNo);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
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

    /**
     * 관리자만 접근가능합니다.
     * 멤버에대한 통계를 반환합니다.
     * 성공시 200 이 반환됩니다.
     *
     * @return the response entity
     */
    @GetMapping("/admin/members/statistics")
    public ResponseEntity<MemberStatisticsResponseDto> memberStatistics() {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(memberService.getMemberStatistics());
    }

    /**
     * 관리자만 접근가능합니다.
     * 회원의 등급별 통계를 반환합니다.
     * 성공시 200이 반환됩니다.
     *
     * @return the response entity
     */
    @GetMapping("/admin/tier/statistics")
    public ResponseEntity<List<MemberTierStatisticsResponseDto>> memberTierStatistics() {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(memberService.getTierStatistics());
    }

    /**
     * 회원만 접근가능합니다.
     * 회원의 패스워드를 검사하기위한 메서드입니다.
     * 회원의 password 를 반환합니다.
     * 성공시 200이 반환됩니다.
     *
     * @param memberNo 회원번호
     * @return the response entity
     */
    @GetMapping("/members/{memberNo}/password-check")
    public ResponseEntity<MemberPasswordResponseDto> memberPasswordCheck(@PathVariable("memberNo") Long memberNo){
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(memberService.getMemberPwd(memberNo));
    }

    /**
     *  회원만 접근가능합니다.
     *  회원의 패스워드가 수정될때 사용되는 메서드입니다.
     *  성공시 201 이 반환됩니다.
     *
     * @param memberNo 회원번호
     * @param request  수정할 회원의 비밀번호가 기입됩니다.
     * @return the response entity
     */
    @PutMapping("/members/{memberNo}/password")
    public ResponseEntity<Void> memberModifyPassword(@PathVariable("memberNo") Long memberNo,
                                                     @RequestBody ModifyMemberPasswordRequest request) {
        memberService.modifyMemberPassword(memberNo,request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();

    }

    /**
     * 회원만 접근가능합니다.
     * 회원의 기준주소지를 변경하기위한 메서드입니다.
     * 성공시 201 이 반환됩니다.
     *
     * @param memberNo  회원번호가 기입됩니다.
     * @param addressNo 회원의 주소번호가 기입됩니다.
     * @return the response entity
     */
    @PutMapping("/members/{memberNo}/addresses/{addressNo}")
    public ResponseEntity<Void> memberModifyBaseAddress(@PathVariable("memberNo") Long memberNo,
                                                        @PathVariable("addressNo") Long addressNo){
        memberService.modifyMemberBaseAddress(memberNo, addressNo);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    /**
     * 회원만 접근할수있습니다.
     * 회원의 주소를 첨가하기위한 메서드입니다.
     * 성공시 201 이 반환됩니다.
     *
     * @param memberNo   the member no
     * @param requestDto the request dto
     * @return the response entity
     */
    @PostMapping("/members/{memberNo}/addresses")
    public ResponseEntity<Void> memberAddressAdd(@PathVariable("memberNo") Long memberNo,
                                                 @Valid @RequestBody CreateAddressRequestDto requestDto) {
        memberService.addMemberAddress(memberNo, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    /**
     * 회원만 접근할수있습니다.
     * 회원의 주소를 지우기위한 메서드입니다.
     * 성공시 200 이반환됩니다.
     *
     * @param memberNo  the member no
     * @param addressNo the address no
     * @return the response entity
     */
    @DeleteMapping("/members/{memberNo}/addresses/{addressNo}")
    public ResponseEntity<Void> memberAddressDelete(@PathVariable("memberNo") Long memberNo,
                                                    @PathVariable("addressNo") Long addressNo){
        memberService.deleteMemberAddress(memberNo, addressNo);
        return ResponseEntity.ok()
                .build();
    }

}
