package com.nhnacademy.bookpubshop.member.repository;

import com.nhnacademy.bookpubshop.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 멤버 리포지토리.
 *
 * @author : 임태원
 * @since : 1.0
 **/
public interface MemberRepository extends JpaRepository<Member, Long>, MemberCustomRepository {

    /**
     * 멤버 이메일이 있는지 확인하는 메소드.
     *
     * @param email 입력받은 email.
     * @return true, false
     */
    boolean existsByMemberEmail(String email);

    /**
     * 멤버 id 있는지 확인하는 메소드.
     *
     * @param id 입력받은 id.
     * @return true, false
     */
    boolean existsByMemberId(String id);

    /**
     * 멤버 nickname 있는지 확인하는 메소드.
     *
     * @param nickname 입력받은 nickname.
     * @return true, false
     */
    boolean existsByMemberNickname(String nickname);

}
