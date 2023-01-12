package com.nhnacademy.bookpubshop.member.service;

import com.nhnacademy.bookpubshop.member.dto.request.ModifyMemberEmailRequestDto;
import com.nhnacademy.bookpubshop.member.dto.request.ModifyMemberNicknameRequestDto;
import com.nhnacademy.bookpubshop.member.dto.request.SignUpMemberRequestDto;
import com.nhnacademy.bookpubshop.member.dto.response.MemberDetailResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.MemberResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.SignUpMemberResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 회원 레포지토리의 메소드를 이용하여 구현한 서버스입니다.
 *
 * @author : 임태원
 * @since : 1.0
 */
public interface MemberService {

    /**
     * 회원생성시 필요한 메서드입니다.
     *
     * @param signUpMemberRequestDto 회원가입시 필요한 회원정보가 기입됩니다.
     * @return signUpMemberResponseDto
     */
    SignUpMemberResponseDto signup(SignUpMemberRequestDto signUpMemberRequestDto);

    /**
     * 회원 정보중 닉네임의 수정을 위해 필요한 메서드입니다.
     *
     * @param memberNo   회원 번호.
     * @param requestDto 수정할 회원의 닉네임이 기입된다.
     */
    void modifyMemberNickName(Long memberNo, ModifyMemberNicknameRequestDto requestDto);

    /**
     * 회원 정보중 이메일을 수정하기위해 필요한 메서드입니다.
     *
     * @param memberNo   회원 번호.
     * @param requestDto 수정할 회원의 email 정보가 기입된다.
     */
    void modifyMemberEmail(Long memberNo, ModifyMemberEmailRequestDto requestDto);

    /**
     * 회원에대한 상세정보가 반환됩니다.
     *
     * @param memberNo 멤버번호.
     * @return MemberDetailResponseDto 멤버에대한 상세값 반환.
     */
    MemberDetailResponseDto getMemberDetails(Long memberNo);

    /**
     * 멤버전체를 조회하기위한 메서드입니다.
     *
     * @param pageable 페이징 정보가 기입.
     * @return page 페이징된 멤버정보가반환.
     */
    Page<MemberResponseDto> getMembers(Pageable pageable);

    /**
     * 사용자를 차단 혹은 차단풀기 할때 사용되는 메서드입니다.
     *
     * @param memberNo 멤버번호반환.
     */
    void blockMember(Long memberNo);

    /**
     * 멤버가 탈퇴할때 사용되는 메서드입니다.
     *
     * @param memberNo 멤버 번호기입.
     */
    void deleteMember(Long memberNo);
}
