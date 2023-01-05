package com.nhnacademy.bookpubshop.point.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.member.dummy.MemberDummy;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.point.dummy.PointHistoryDummy;
import com.nhnacademy.bookpubshop.point.entity.PointHistory;
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
 * 포인트내역 레포지토리 테스트입니다.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@DataJpaTest
class PointHistoryRepositoryTest {
    @Autowired
    PointHistoryRepository pointHistoryRepository;

    @Autowired
    TestEntityManager entityManager;

    Tier tier;
    Member member;
    PointHistory pointHistory;

    @BeforeEach
    void setUp() {
        tier = TierDummy.dummy();
        member = MemberDummy.dummy(tier);
        pointHistory = PointHistoryDummy.dummy(member);

        entityManager.persist(tier);
        entityManager.persist(member);
    }

    @Test
    @DisplayName("포인트내역 저장 테스트")
    void PointHistorySaveTest() {
        entityManager.persist(pointHistory);
        entityManager.clear();

        Optional<PointHistory> findPointHistory
                = pointHistoryRepository.findById(1L);

        assertThat(findPointHistory).isPresent();
        assertThat(findPointHistory.get().getPointHistoryAmount())
                .isEqualTo(981008L);
        assertThat(findPointHistory.get().getMember().getMemberNickname())
                .isEqualTo("nickname");
    }
}