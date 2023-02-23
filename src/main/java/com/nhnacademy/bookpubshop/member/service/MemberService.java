package com.nhnacademy.bookpubshop.member.service;

import com.nhnacademy.bookpubshop.member.dto.request.CreateAddressRequestDto;
import com.nhnacademy.bookpubshop.member.dto.request.ModifyMemberEmailRequestDto;
import com.nhnacademy.bookpubshop.member.dto.request.ModifyMemberNameRequestDto;
import com.nhnacademy.bookpubshop.member.dto.request.ModifyMemberNicknameRequestDto;
import com.nhnacademy.bookpubshop.member.dto.request.ModifyMemberPasswordRequest;
import com.nhnacademy.bookpubshop.member.dto.request.ModifyMemberPhoneRequestDto;
import com.nhnacademy.bookpubshop.member.dto.request.SignupDto;
import com.nhnacademy.bookpubshop.member.dto.response.LoginMemberResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.MemberAuthResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.MemberDetailResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.MemberPasswordResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.MemberResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.MemberStatisticsResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.MemberTierStatisticsResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.SignUpMemberResponseDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 회원 레포지토리의 메소드를 이용하여 구현한 서버스입니다.
 *
 * @author : 임태원, 유호철
 * @since : 1.0
 */
public interface MemberService {

    /**
     * 회원생성시 필요한 메서드입니다.
     *
     * @param signUpMemberRequestDto 회원가입시 필요한 회원정보가 기입됩니다.
     * @return signUpMemberResponseDto
     */
    SignUpMemberResponseDto signup(SignupDto signUpMemberRequestDto);

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


    /**
     * 멤버의 이름을 수정할때 쓰이는 메서드입니다.
     *
     * @param memberNo   the member no
     * @param memberName the member name
     */
    void modifyMemberName(Long memberNo, ModifyMemberNameRequestDto memberName);

    /**
     * 멤버의 휴대전화 번호를 변경할때 쓰이는 메서드입니다.
     *
     * @param memberNo    the member no
     * @param memberPhone the member phone
     */
    void modifyMemberPhone(Long memberNo, ModifyMemberPhoneRequestDto memberPhone);

    /**
     * 회원의 비밀번호를 수정하기위한 메서드입니다.
     *
     * @param memberNo 회원번호.
     * @param password 비밀번호.
     */
    void modifyMemberPassword(Long memberNo, ModifyMemberPasswordRequest password);

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

    /**
     * 멤버가 로그인할 때 사용되는 메서드입니다.
     *
     * @param loginId 멤버 로그인 아이디.
     * @return 로그인 성공정보 리턴.
     */
    LoginMemberResponseDto loginMember(String loginId);

    /**
     * 회원가입시 사용되는 아이디 중복체크 메서드입니다.
     *
     * @param id 회원가입 시도하는 id
     * @return 중복인지 아닌지 ture, false
     */
    boolean idDuplicateCheck(String id);

    /**
     * 회원가입시 사용되는 닉네임 중복체크 메서드입니다.
     *
     * @param nickName 회원가입 시도하는 nickName
     * @return 중복인지 아닌지 ture, false
     */
    boolean nickNameDuplicateCheck(String nickName);

    /**
     * 회원의 Pwd 를 받기위한 메서드입니다.
     *
     * @param memberNo 회원번호
     * @return encoding 된 회원의 번호가 반환됩니다.
     */
    MemberPasswordResponseDto getMemberPwd(Long memberNo);

    /**
     * 회원의 기주소지를 바꾸기위한 메서드입니다.
     *
     * @param memberNo  회원번호
     * @param addressNo 회원이 변경할 주소
     */
    void modifyMemberBaseAddress(Long memberNo, Long addressNo);

    /**
     * 회원의 주소를 추가하는 메서드입니다.
     *
     * @param memberNo   회원번호
     * @param requestDto 추가할 주소 정보
     */
    void addMemberAddress(Long memberNo, CreateAddressRequestDto requestDto);


    /**
     * 회원의 해당하는 주소를 삭제하기위한 메서드입니다.
     *
     * @param memberNo  회원번호
     * @param addressNo 주소번호
     */
    void deleteMemberAddress(Long memberNo, Long addressNo);

    /**
     * 회원 정보를 가져오는 메서드 입니다.
     *
     * @param memberNo 회원번호.
     * @return 인증된 회원정보.
     */
    MemberAuthResponseDto authMemberInfo(Long memberNo);

    /**
     * oauth로 가입한 회원의 정보를 가져오는 메소드 입니다.
     *
     * @param email front에서 넘겨준 체크하고싶은 정보.
     * @return 회원인지 아닌지.
     */
    boolean isOauthMember(String email);

    /**
     * 멤버 번호로 등급을 조회하는 메서드입니다.
     *
     * @param memberNo 멤버 번호
     * @return 등급 번호
     */
    Integer getTierByMemberNo(Long memberNo);

    /**
     * 닉네임으로 멤버를 검색합니다.
     *
     * @param pageable 페이징
     * @param search 검색할 문자
     * @return 멤버리스트
     */
    Page<MemberResponseDto> getMembersByNickName(Pageable pageable, String search);

    /**
     * 아이디로 멤버를 검색합니다.
     *
     * @param pageable 페이징
     * @param search 검색할 문자
     * @return 멤버리스트
     */
    Page<MemberResponseDto> getMembersById(Pageable pageable, String search);
}
