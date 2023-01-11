package com.nhnacademy.bookpubshop.point.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.member.dummy.MemberDummy;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.point.dummy.PointHistoryDummy;
import com.nhnacademy.bookpubshop.point.entity.PointHistory;
import com.nhnacademy.bookpubshop.tier.dummy.TierDummy;
import com.nhnacademy.bookpubshop.tier.entity.BookPubTier;
import java.time.LocalDateTime;
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

    BookPubTier bookPubTier;
    Member member;
    PointHistory pointHistory;

    @BeforeEach
    void setUp() {
        bookPubTier = TierDummy.dummy();
        member = MemberDummy.dummy(bookPubTier);
        pointHistory = PointHistoryDummy.dummy(member);

        entityManager.persist(bookPubTier);
        entityManager.persist(member);
    }

    @Test
    @DisplayName("포인트내역 저장 테스트")
    void PointHistorySaveTest() {
        LocalDateTime now = LocalDateTime.now();
        PointHistory persist = entityManager.persist(pointHistory);

        Optional<PointHistory> findPointHistory
                = pointHistoryRepository.findById(1L);

        assertThat(findPointHistory).isPresent();
        assertThat(findPointHistory.get().getPointHistoryAmount()).isEqualTo(persist.getPointHistoryAmount());
        assertThat(findPointHistory.get().getMember().getMemberNickname()).isEqualTo(persist.getMember().getMemberNickname());
        assertThat(findPointHistory.get().getPointHistoryNo()).isEqualTo(persist.getPointHistoryNo());
        assertThat(findPointHistory.get().isPointHistoryIncreased()).isEqualTo(persist.isPointHistoryIncreased());
        assertThat(findPointHistory.get().getPointHistoryReason()).isEqualTo(persist.getPointHistoryReason());
        assertThat(findPointHistory.get().getCreatedAt()).isAfter(now);
    }
}