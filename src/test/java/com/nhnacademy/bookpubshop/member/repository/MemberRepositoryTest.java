package com.nhnacademy.bookpubshop.member.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.member.dummy.MemberDummy;
import com.nhnacademy.bookpubshop.tier.dummy.TierDummy;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.tier.entity.BookPubTier;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * 멤버 레포지토리 테스트
 *
 * @author : 임태원
 * @since : 1.0
 **/
@DataJpaTest
class MemberRepositoryTest {
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    MemberRepository memberRepository;

    BookPubTier bookPubTier;
    Member member;

    @BeforeEach
    void setUp() {
        bookPubTier = TierDummy.dummy();
        member = MemberDummy.dummy(bookPubTier);
    }

    @Test
    @DisplayName("멤버 save 테스트")
    void memberSaveTest() {
        entityManager.persist(bookPubTier);
        Member persist = entityManager.persist(member);

        Optional<Member> member = memberRepository.findById(persist.getMemberNo());

        assertThat(member).isPresent();
        assertThat(member.get().getMemberId()).isEqualTo("id");
        assertThat(member.get().getMemberNickname()).isEqualTo("nickname");
    }
}