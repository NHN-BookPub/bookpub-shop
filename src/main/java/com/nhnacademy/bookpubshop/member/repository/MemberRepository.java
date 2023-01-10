package com.nhnacademy.bookpubshop.member.repository;

import com.nhnacademy.bookpubshop.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 멤버 리포지토리.
 *
 * @author : 임태원
 * @since : 1.0
 **/
public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByMemberEmail(String email);

    boolean existsByMemberId(String id);

    boolean existsByMemberNickname(String nickname);
}
