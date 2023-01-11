package com.nhnacademy.bookpubshop.member.relationship.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.authority.dummy.AuthorityDummy;
import com.nhnacademy.bookpubshop.authority.entity.Authority;
import com.nhnacademy.bookpubshop.member.dummy.MemberAuthorityDummy;
import com.nhnacademy.bookpubshop.member.dummy.MemberDummy;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.member.relationship.entity.MemberAuthority;
import com.nhnacademy.bookpubshop.tier.dummy.TierDummy;
import com.nhnacademy.bookpubshop.tier.entity.BookPubTier;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * 멤버권한 Repo 테스트입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
@DataJpaTest
class MemberAuthorityRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MemberAuthorityRepository memberAuthorityRepository;

    MemberAuthority memberAuthority;
    BookPubTier bookPubTier;
    Member member;
    Authority authorityDummy;

    @BeforeEach
    void setUp() {
        bookPubTier = TierDummy.dummy();
        member = MemberDummy.dummy(bookPubTier);
        authorityDummy = AuthorityDummy.dummy();
        memberAuthority = MemberAuthorityDummy.dummy(member, authorityDummy);
    }

    @DisplayName("멤버권한관계테이블 세이브 테스트")
    @Test
    void memberAuthoritySaveTest() {
        entityManager.persist(bookPubTier);
        entityManager.persist(member);
        entityManager.persist(authorityDummy);
        entityManager.persist(memberAuthority);

        entityManager.persist(memberAuthority.getAuthority());
        MemberAuthority persist = entityManager.persist(memberAuthority);

        Optional<MemberAuthority> result = memberAuthorityRepository.findById(persist.getId());
        assertThat(result).isPresent();
        assertThat(result.get().getAuthority()).isEqualTo(persist.getAuthority());
        assertThat(result.get().getMember()).isEqualTo(persist.getMember());
        assertThat(result.get().getId()).isEqualTo(persist.getId());
        assertThat(result.get().getId().getMemberNo()).isEqualTo(member.getMemberNo());
        assertThat(result.get().getId().getAuthorityNo()).isEqualTo(authorityDummy.getAuthorityNo());
    }

}