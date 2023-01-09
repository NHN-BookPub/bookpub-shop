package com.nhnacademy.bookpubshop.member.relationship.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.authority.dummy.AuthorityDummy;
import com.nhnacademy.bookpubshop.member.dummy.MemberAuthorityDummy;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.member.relationship.entity.MemberAuthority;
import com.nhnacademy.bookpubshop.tier.entity.BookPubTier;
import com.nhnacademy.bookpubshop.tier.repository.TierRepository;
import java.time.LocalDateTime;
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
    @Autowired
    private TierRepository tierRepository;

    @BeforeEach
    void setUp() {
        memberAuthority = MemberAuthorityDummy.dummy(
                memberDummy(new BookPubTier(null,"tier")), AuthorityDummy.dummy());
    }

    @DisplayName("멤버권한관계테이블 세이브 테스트")
    @Test
    void memberAuthoritySaveTest() {
        entityManager.persist(memberAuthority.getAuthority());
        MemberAuthority persist = entityManager.persist(memberAuthority);

        Optional<MemberAuthority> result = memberAuthorityRepository.findById(persist.getId());
        assertThat(result).isPresent();
        assertThat(result.get().getAuthority()).isEqualTo(persist.getAuthority());
        assertThat(result.get().getMember()).isEqualTo(persist.getMember());
        assertThat(result.get().getId()).isEqualTo(persist.getId());
    }

    private Member memberDummy(BookPubTier bookPubTier) {
        Member testMember = new Member(null, bookPubTier, "test_id", "test_nickname", "test_name", "남", 22, 819, "test_pwd", "01012341234",
                "test@test.com", LocalDateTime.now(), false, false, null, 0L, false);
        entityManager.persist(testMember.getTier());
        return entityManager.persist(testMember);
    }
}