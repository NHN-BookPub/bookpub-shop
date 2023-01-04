package com.nhnacademy.bookpubshop.member.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.tier.entity.Tier;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * Some description here
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

    Member member;
    Tier tier;

    @BeforeEach
    void setUp() {
        tier = new Tier(null, "tier");
        entityManager.persist(tier);

        member = new Member(null, tier, "id", "nickname", "taewon",
                "남성", 1234, 1231, "!@!#@ASD", "12345678", "email@email.com",
                LocalDateTime.now(), false, false, null, 0L, false);
    }

    @Test
    @DisplayName("멤버 save 테스트")
    void memberSaveTest() {
        entityManager.persist(member);
        entityManager.clear();

        Optional<Member> member = memberRepository.findById(1L);

        assertThat(member).isPresent();
        assertThat(member.get().getMemberId()).isEqualTo("id");
        assertThat(member.get().getMemberNickname()).isEqualTo("nickname");

    }
}