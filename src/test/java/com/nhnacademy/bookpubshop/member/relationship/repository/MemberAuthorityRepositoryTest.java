package com.nhnacademy.bookpubshop.member.relationship.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import com.nhnacademy.bookpubshop.authority.dummy.AuthorityDummy;
import com.nhnacademy.bookpubshop.authority.entity.Authority;
import com.nhnacademy.bookpubshop.member.dummy.MemberDummy;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.member.relationship.dummy.MemberAuthorityDummy;
import com.nhnacademy.bookpubshop.member.relationship.entity.MemberAuthority;
import com.nhnacademy.bookpubshop.tier.dummy.TierDummy;
import com.nhnacademy.bookpubshop.tier.entity.Tier;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * 멤버 권한 연관관계 레포지토리 테스트.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@DataJpaTest
class MemberAuthorityRepositoryTest {
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    MemberAuthorityRepository memberAuthorityRepository;

    Tier tier;
    Member member;
    Authority authority;
    MemberAuthority memberAuthority;

    @BeforeEach
    void setUp() {
        tier = TierDummy.dummy();
        member = MemberDummy.dummy(tier);
        authority = AuthorityDummy.dummy();

        entityManager.persist(tier);
        entityManager.persist(member);
        entityManager.persist(authority);
        memberAuthority = MemberAuthorityDummy.dummy(member, authority);
    }

    @Test
    @DisplayName("멤버 권한 연관관계 저장 테스트")
    void MemberAuthoritySaveTest() {
        entityManager.persist(memberAuthority);
        entityManager.flush();
        entityManager.clear();

        Optional<MemberAuthority> findMemberAuthority
                = memberAuthorityRepository.findById(memberAuthority.getId());

        assertThat(findMemberAuthority).isPresent();
        assertThat(findMemberAuthority.get().getAuthority().getAuthorityName())
                .isEqualTo("ROLE_ADMIN");
        assertThat(findMemberAuthority.get().getMember().getMemberNickname())
                .isEqualTo("nickname");
    }
}