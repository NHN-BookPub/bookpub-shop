package com.nhnacademy.bookpubshop.member.controller;

import static com.nhnacademy.bookpubshop.annotation.aspect.AuthorizationPointCut.AUTH_MEMBER_INFO;

import com.nhnacademy.bookpubshop.annotation.AdminAuth;
import com.nhnacademy.bookpubshop.annotation.MemberAndAuth;
import com.nhnacademy.bookpubshop.annotation.MemberAuth;
import com.nhnacademy.bookpubshop.member.dto.request.CreateAddressRequestDto;
import com.nhnacademy.bookpubshop.member.dto.request.IdRequestDto;
import com.nhnacademy.bookpubshop.member.dto.request.LoginMemberRequestDto;
import com.nhnacademy.bookpubshop.member.dto.request.ModifyMemberEmailRequestDto;
import com.nhnacademy.bookpubshop.member.dto.request.ModifyMemberNameRequestDto;
import com.nhnacademy.bookpubshop.member.dto.request.ModifyMemberNicknameRequestDto;
import com.nhnacademy.bookpubshop.member.dto.request.ModifyMemberPasswordRequest;
import com.nhnacademy.bookpubshop.member.dto.request.ModifyMemberPhoneRequestDto;
import com.nhnacademy.bookpubshop.member.dto.request.NickRequestDto;
import com.nhnacademy.bookpubshop.member.dto.request.OauthMemberCreateRequestDto;
import com.nhnacademy.bookpubshop.member.dto.request.SignUpMemberRequestDto;
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
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.web.bind.annotation.RestController;


/**
 * 회원 정보를 다루는 컨트롤러 입니다.
 *
 * @author : 임태원, 유호철
 * @since : 1.0
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;

    /**
     * 회원정보를 받고 저장하는 메소드.
     *
     * @param memberDto 회원정보가 입력.
     * @return 회원정보 저장성공 or 실패정보가 담긴 엔티티 반환.
     */
    @PostMapping("/api/signup")
    public ResponseEntity<SignUpMemberResponseDto> signup(
            @Valid @RequestBody SignUpMemberRequestDto memberDto) {
        SignUpMemberResponseDto memberInfo = memberService.signup(memberDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(memberInfo);
    }

    /**
     * 회원정보를 받고 저장하는 메소드.
     *
     * @param memberDto 회원정보가 입력.
     * @return 회원정보 저장성공 or 실패정보가 담긴 엔티티 반환.
     */
    @PostMapping("/api/oauth/signup")
    public ResponseEntity<SignUpMemberResponseDto> signup(
            @Valid @RequestBody OauthMemberCreateRequestDto memberDto) {
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
    @PostMapping("/api/signup/idCheck")
    public boolean idDuplicateCheck(@RequestBody IdRequestDto requestDto) {
        return memberService.idDuplicateCheck(requestDto.getId());
    }

    /**
     * 닉네임 중복체크 요청을 받아 검사하는 메소드.
     *
     * @param requestDto front에서 요청한 nickname.
     * @return 중복이면 true, 아니면 false
     */
    @PostMapping("/api/signup/nickCheck")
    public boolean nickDuplicateCheck(@RequestBody NickRequestDto requestDto) {
        return memberService.nickNameDuplicateCheck(requestDto.getNickname());
    }

    /**
     * 자사 회원의 accessToken을 가져와 사용자의 정보 요청하는 메소드.
     *
     * @param request request.
     * @return 인증된 사용자의 정보.
     */
    @GetMapping("/token/auth")
    public ResponseEntity<MemberDetailResponseDto> authMemberInfo(HttpServletRequest request) {
        Long memberNo = Long.parseLong(request.getHeader(AUTH_MEMBER_INFO));

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(memberService.getMemberDetails(memberNo));
    }


    /**
     * 닉네임 변경시 사용되는 메서드입니다.
     * 성공시 201 반환.
     *
     * @param memberNo   멤버 번호가 기입된다.
     * @param requestDto 수정할 닉네임이 기입.
     * @return response entity
     */
    @PutMapping("/token/members/{memberNo}/nickName")
    @MemberAuth
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
    @PutMapping("/token/members/{memberNo}/email")
    @MemberAuth
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
     * @param memberNo 회원번호가 기입됩니다.
     * @param dto      휴대전화번호가 기입됩니다.
     * @return the response entity
     */
    @PutMapping("/token/members/{memberNo}/phone")
    @MemberAuth
    public ResponseEntity<Void> memberModifyPhone(@PathVariable("memberNo") Long memberNo,
                                                  @Valid
                                                  @RequestBody ModifyMemberPhoneRequestDto dto) {
        memberService.modifyMemberPhone(memberNo, dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    /**
     * 회원만접근이 가능합니다.
     * 회원의 휴대전화를 수정할때 쓰이는 메서드입니다.
     * 성공시 201 을 반환합니다.
     *
     * @param memberNo 회원번호가 기입됩니다.
     * @param dto      휴대전화번호가 기입됩니다.
     * @return the response entity
     */
    @PutMapping("/token/members/{memberNo}/name")
    @MemberAuth
    public ResponseEntity<Void> memberModifyName(@PathVariable("memberNo") Long memberNo,
                                                 @Valid @RequestBody
                                                 ModifyMemberNameRequestDto dto) {
        memberService.modifyMemberName(memberNo, dto);
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
    @GetMapping("/api/members/{memberNo}")
    public ResponseEntity<MemberDetailResponseDto> memberDetails(
            @PathVariable("memberNo") Long memberNo) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(memberService.getMemberDetails(memberNo));
    }

    /**
     * 인증된 사용자만 사용가능한 GET 입니다.
     * 200 이 반환됩니다.
     *
     * @return the response entity
     */
    @MemberAuth
    @GetMapping("/token/members/{memberNo}")
    public ResponseEntity<MemberDetailResponseDto> memberDetailsAuth(
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
    @GetMapping("/token/admin/members")
    @AdminAuth
    public ResponseEntity<PageResponse<MemberResponseDto>> memberList(Pageable pageable) {
        Page<MemberResponseDto> members = memberService.getMembers(pageable);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new PageResponse<>(members));
    }

    /**
     * 닉네임으로 멤버를 검색합니다.
     *
     * @param pageable 페이징
     * @param search 검색할 문자
     * @return 성공시 200, 멤버리스트
     */
    @GetMapping("/token/admin/members/{search}/nick")
    @AdminAuth
    public ResponseEntity<PageResponse<MemberResponseDto>> memberListByNick(
            Pageable pageable, @PathVariable String search) {
        Page<MemberResponseDto> members =
                memberService.getMembersByNickName(pageable, search);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new PageResponse<>(members));
    }

    /**
     * 아이디로 멤버를 검색합니다.
     *
     * @param pageable 페이징
     * @param search 검색할 문자
     * @return 성공시 200, 멤버리스트
     */
    @GetMapping("/token/admin/members/{search}/id")
    @AdminAuth
    public ResponseEntity<PageResponse<MemberResponseDto>> memberListById(
            Pageable pageable, @PathVariable String search) {
        Page<MemberResponseDto> members =
                memberService.getMembersById(pageable, search);

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
    @PutMapping("/token/members/{memberNo}")
    @MemberAndAuth
    public ResponseEntity<Void> memberDelete(@PathVariable("memberNo") Long memberNo) {
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
    @PutMapping("/token/admin/members/{memberNo}")
    @AdminAuth
    public ResponseEntity<Void> memberBlock(@PathVariable("memberNo") Long memberNo) {
        memberService.blockMember(memberNo);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    /**
     * 회원의 로그인을 위한 api 입니다.
     * 성공시 200 이반환됩니다.
     *
     * @param loginMemberRequestDto the login member request dto
     * @return the response entity
     */
    @PostMapping("/api/login")
    public ResponseEntity<LoginMemberResponseDto> memberLogin(
            @RequestBody LoginMemberRequestDto loginMemberRequestDto) {
        String memberId = loginMemberRequestDto.getMemberId();

        LoginMemberResponseDto loginInfo = memberService.loginMember(memberId);

        if (Objects.isNull(loginInfo)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .build();
        }

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
    @GetMapping("/token/admin/members/statistics")
    @AdminAuth
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
    @GetMapping("/token/admin/tier/statistics")
    @AdminAuth
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
    @GetMapping("/token/members/{memberNo}/password-check")
    @MemberAuth
    public ResponseEntity<MemberPasswordResponseDto> memberPasswordCheck(
            @PathVariable("memberNo") Long memberNo) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(memberService.getMemberPwd(memberNo));
    }

    /**
     * 회원만 접근가능합니다.
     * 회원의 패스워드가 수정될때 사용되는 메서드입니다.
     * 성공시 201 이 반환됩니다.
     *
     * @param memberNo 회원번호
     * @param request  수정할 회원의 비밀번호가 기입됩니다.
     * @return the response entity
     */
    @PutMapping("/token/members/{memberNo}/password")
    @MemberAuth
    public ResponseEntity<Void> memberModifyPassword(@PathVariable("memberNo") Long memberNo,
                                                     @RequestBody
                                                     ModifyMemberPasswordRequest request) {
        memberService.modifyMemberPassword(memberNo, request);
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
    @PutMapping("/token/members/{memberNo}/addresses/{addressNo}")
    @MemberAuth
    public ResponseEntity<Void> memberModifyBaseAddress(@PathVariable("memberNo") Long memberNo,
                                                        @PathVariable("addressNo") Long addressNo) {
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
    @PostMapping("/token/members/{memberNo}/addresses")
    @MemberAuth
    public ResponseEntity<Void> memberAddressAdd(@PathVariable("memberNo") Long memberNo,
                                                 @Valid @RequestBody
                                                 CreateAddressRequestDto requestDto) {
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
    @DeleteMapping("/token/members/{memberNo}/addresses/{addressNo}")
    @MemberAuth
    public ResponseEntity<Void> memberAddressDelete(@PathVariable("memberNo") Long memberNo,
                                                    @PathVariable("addressNo") Long addressNo) {
        memberService.deleteMemberAddress(memberNo, addressNo);
        return ResponseEntity.ok()
                .build();
    }

    /**
     * oauth로 로그인 시도한 사람이 북펍의 db에 있는지 없는지 확인하는 메소드 입니다.
     *
     * @param email front에서 요청한 확인하고자 하는 정보.
     * @return 맞는지 아닌지.
     */
    @GetMapping("/api/oauth/{email}")
    public ResponseEntity<Boolean> oauthMemberCheck(@PathVariable String email) {
        boolean oauthMember = memberService.isOauthMember(email);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(oauthMember);
    }

    /**
     * 멤버 번호로 등급번호를 조회하는 메서드입니다다.
     *
     * @param memberNo 멤버 번호
     * @return 등급 번호
     */
    @MemberAuth
    @GetMapping("/token/members/{memberNo}/tier")

    public ResponseEntity<Integer> getTierNoByMemberNo(@PathVariable Long memberNo) {
        Integer tierNo = memberService.getTierByMemberNo(memberNo);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(tierNo);
    }

}
