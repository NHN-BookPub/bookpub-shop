package com.nhnacademy.bookpubshop.member.repository;

import com.nhnacademy.bookpubshop.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 멤버 리포지토리.
 *
 * @author : 임태원
 * @since : 1.0
 **/
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByMemberNickname(String nickname);

    Optional<Member> findByMemberId(String id);

    Optional<Member> findByMemberEmail(String email);
}
