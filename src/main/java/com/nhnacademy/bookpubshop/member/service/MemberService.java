package com.nhnacademy.bookpubshop.member.service;

import com.nhnacademy.bookpubshop.member.dto.request.ModifyMemberEmailRequestDto;
import com.nhnacademy.bookpubshop.member.dto.request.ModifyMemberNicknameRequestDto;
import com.nhnacademy.bookpubshop.member.dto.request.SignUpMemberRequestDto;
import com.nhnacademy.bookpubshop.member.dto.response.MemberDetailResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.MemberResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.MemberStatisticsResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.MemberTierStatisticsResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.SignUpMemberResponseDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * The interface Member service.
 */
public interface MemberService {

    /**
     * Signup sign up member response dto.
     *
     * @param signUpMemberRequestDto the sign up member request dto
     * @return the sign up member response dto
     */
    SignUpMemberResponseDto signup(SignUpMemberRequestDto signUpMemberRequestDto);

    /**
     * Modify member nick name.
     *
     * @param memberNo   the member no
     * @param requestDto the request dto
     */
    void modifyMemberNickName(Long memberNo, ModifyMemberNicknameRequestDto requestDto);

    /**
     * Modify member email.
     *
     * @param memberNo   the member no
     * @param requestDto the request dto
     */
    void modifyMemberEmail(Long memberNo, ModifyMemberEmailRequestDto requestDto);

    /**
     * Gets member details.
     *
     * @param memberNo the member no
     * @return the member details
     */
    MemberDetailResponseDto getMemberDetails(Long memberNo);

    /**
     * Gets members.
     *
     * @param pageable the pageable
     * @return the members
     */
    Page<MemberResponseDto> getMembers(Pageable pageable);

    /**
     * Block member.
     *
     * @param memberNo the member no
     */
    void blockMember(Long memberNo);

    /**
     * 멤버가 탈퇴할때 사용되는 메서드입니다.
     *
     * @param memberNo 멤버 번호기입.
     */
    void deleteMember(Long memberNo);


    /**
     * 멤버의 등급별 통계를 얻기위한 메서드입니다.
     *
     * @return the tier statistics
     */
    List<MemberTierStatisticsResponseDto> getTierStatistics();

    /**
     * 멤버의 통계를 얻기위한 메서드입니다.
     *
     * @return the member statistics
     */
    MemberStatisticsResponseDto getMemberStatistics();
}
