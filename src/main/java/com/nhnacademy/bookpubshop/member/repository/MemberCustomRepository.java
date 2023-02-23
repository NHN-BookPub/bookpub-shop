package com.nhnacademy.bookpubshop.member.repository;

import com.nhnacademy.bookpubshop.member.dto.response.LoginMemberResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.MemberAuthResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.MemberDetailResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.MemberResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.MemberStatisticsResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.MemberTierStatisticsResponseDto;
import com.nhnacademy.bookpubshop.member.entity.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * QueryDsl 을 사용하기위한 Repo 인터페이스.
 *
 * @author : 유호철
 * @since : 1.0
 */
@NoRepositoryBean
public interface MemberCustomRepository {
    /**
     * 멤버에 대한 상세정보를 받는 클래스입니다.
     *
     * @param memberNo 멤버 번호
     * @return 멤버상세정보 반환.
     */
    Optional<MemberDetailResponseDto> findByMemberDetails(Long memberNo);

    /**
     * 멤버에대한 통계를 뽑는 메서드입니다.
     *
     * @return 전체 멤버에대한 통계가 나옵니다.
     */
    MemberStatisticsResponseDto memberStatistics();

    /**
     * 멤버에대한 등급별 통계가 나옵니다.
     *
     * @return 등급별 통계가 리스트로 반환됩니다.
     */
    List<MemberTierStatisticsResponseDto> memberTierStatistics();

    /**
     * 멤버들의 정보를 페이징 객체로반환합니다.
     *
     * @param pageable 페이징값.
     * @return 페이된 멤버들의 정보가 반환됩니다.
     */
    Page<MemberResponseDto> findMembers(Pageable pageable);

    /**
     * 로그인 한 멤버의 정보를 불러오는 메소드.
     *
     * @param id 입력받은 id
     * @return 로그인 멤버 정보 dto.
     */
    LoginMemberResponseDto findByMemberLoginInfo(String id);

    /**
     * 인증 된 유저의 정보를 불러오는 메소드.
     *
     * @param memberNo 인증받은 유저의 번호.
     * @return 프론트 서버에 저장 될 정보.
     */
    MemberAuthResponseDto findByAuthMemberInfo(Long memberNo);

    /**
     * 멤버번호로 등급을 조회하는 메소드.
     *
     * @param memberNo 멤버 번호
     * @return 등급 번호
     */
    Integer findTierNoByMemberNo(Long memberNo);

    /**
     * 멤버 닉네임으로 멤버를 조회하는 메소드.
     *
     * @param nickname 닉네임.
     * @return 멤버.
     */
    Optional<Member> findMemberByMemberNickname(String nickname);

    /**
     * 닉네임으로 멤버를 검색합니다.
     *
     * @param pageable 페이징
     * @param search 검색할 문자
     * @return 멤버리스트
     */
    Page<MemberResponseDto> findMembersListByNickName(Pageable pageable, String search);

    /**
     * 아이디로 멤버를 검색합니다.
     *
     * @param pageable 페이징
     * @param search 검색할 문자
     * @return 멤버리스트
     */
    Page<MemberResponseDto> findMembersListById(Pageable pageable, String search);
}
