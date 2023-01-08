package com.nhnacademy.bookpubshop.member.repository;

import com.nhnacademy.bookpubshop.member.dto.SignUpMemberResponseDto;
import java.util.Optional;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Member 테이블 Querydsl 사용하기 위한 인터페이스.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@NoRepositoryBean
public interface MemberRepositoryCustom {

    /**
     * 닉네임으로 멤버를 조회하는 메소드.
     *
     * @param nickname 유저의 닉네임.
     * @return Member entity.
     */
    Optional<SignUpMemberResponseDto> findByMemberNickname(String nickname);

    /**
     * 아이디로 멤버를 조회하는 메소드.
     *
     * @param id 유저의 아이디.
     * @return Member entity.
     */
    Optional<SignUpMemberResponseDto> findByMemberId(String id);

    /**
     * 이메일로 멤버를 조회하는 메소드.
     *
     * @param email 유저의 이메일.
     * @return Member entity.
     */
    Optional<SignUpMemberResponseDto> findByMemberEmail(String email);
}
