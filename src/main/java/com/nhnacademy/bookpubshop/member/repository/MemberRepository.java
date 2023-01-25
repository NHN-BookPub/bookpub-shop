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


    /**
     * 멤버 id로 멤버를 가져오는 메소드.
     * 쿠폰 등록 시 사용되는 메소드입니다.
     *
     * @param memberId 멤버 id
     * @return 멤버 entity
     */
    Optional<Member> findByMemberId(String memberId);
}
