package com.nhnacademy.bookpubshop.point.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.bookpubshop.member.dummy.MemberDummy;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.point.dto.response.GetPointAdminResponseDto;
import com.nhnacademy.bookpubshop.point.dto.response.GetPointResponseDto;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
    Pageable pageable;

    @BeforeEach
    void setUp() {
        bookPubTier = TierDummy.dummy();
        member = MemberDummy.dummy(bookPubTier);
        pointHistory = PointHistoryDummy.dummy(member);

        entityManager.persist(bookPubTier);
        entityManager.persist(member);
        pointHistory = entityManager.persist(pointHistory);

        pageable = Pageable.ofSize(10);


    }

    @Test
    @DisplayName("포인트내역 저장 테스트")
    void PointHistorySaveTest() {
        Optional<PointHistory> findPointHistory
                = pointHistoryRepository.findById(1L);

        assertThat(findPointHistory).isPresent();
        assertThat(findPointHistory.get().getPointHistoryAmount()).isEqualTo(pointHistory.getPointHistoryAmount());
        assertThat(findPointHistory.get().getMember().getMemberNickname()).isEqualTo(pointHistory.getMember().getMemberNickname());
        assertThat(findPointHistory.get().getPointHistoryNo()).isEqualTo(pointHistory.getPointHistoryNo());
        assertThat(findPointHistory.get().isPointHistoryIncreased()).isEqualTo(pointHistory.isPointHistoryIncreased());
        assertThat(findPointHistory.get().getPointHistoryReason()).isEqualTo(pointHistory.getPointHistoryReason());
        assertThat(findPointHistory.get().getCreatedAt()).isEqualTo(pointHistory.getCreatedAt());
    }

    @Test
    @DisplayName("포인트의 모든 기록을 불러오는 메소드")
    void getPointHistoryByType() {

        Page<GetPointResponseDto> point
                = pointHistoryRepository.getPointHistory(pageable, "1", member.getMemberNo());

        assertThat(point.getContent().get(0).getPointHistoryReason()).isEqualTo(pointHistory.getPointHistoryReason());
        assertThat(point.getContent().get(0).getPointHistoryAmount()).isEqualTo(pointHistory.getPointHistoryAmount());
        assertThat(point.getContent().get(0).getCreatedAt()).isEqualTo(pointHistory.getCreatedAt());
    }

    @Test
    @DisplayName("포인트의 사용 기록을 불러오는 메소드")
    void getPointHistoryByType_useHistory() {

        Page<GetPointResponseDto> point
                = pointHistoryRepository.getPointHistory(pageable, "2", member.getMemberNo());

        assertThat(point.getContent().get(0).getPointHistoryReason()).isEqualTo(pointHistory.getPointHistoryReason());
        assertThat(point.getContent().get(0).getPointHistoryAmount()).isEqualTo(pointHistory.getPointHistoryAmount());
        assertThat(point.getContent().get(0).getCreatedAt()).isEqualTo(pointHistory.getCreatedAt());
    }

    @Test
    @DisplayName("포인트의 소모 기록을 불러오는 메소드")
    void getPointHistoryByType_spendHistory() {
        Page<GetPointResponseDto> point
                = pointHistoryRepository.getPointHistory(pageable, "3", member.getMemberNo());

        assertThat(point.getContent().size()).isZero();
    }

    @Test
    @DisplayName("어드민이 모든 회원의 포인트 내역을 조회하는 메소드")
    void getPoints() {
        Page<GetPointAdminResponseDto> point
                = pointHistoryRepository.getPoints(pageable, LocalDateTime.of(2023, 2, 1, 0, 0), LocalDateTime.of(2024, 2, 1, 0, 0));

        assertThat(point.getContent().get(0).getPointHistoryReason()).isEqualTo(pointHistory.getPointHistoryReason());
        assertThat(point.getContent().get(0).getPointHistoryAmount()).isEqualTo(pointHistory.getPointHistoryAmount());
        assertThat(point.getContent().get(0).getCreatedAt()).isEqualTo(pointHistory.getCreatedAt());
        assertThat(point.getContent().get(0).isIncreased()).isTrue();
        assertThat(point.getContent().get(0).getMemberId()).isEqualTo(member.getMemberId());
    }

    @Test
    @DisplayName("어드민이 모든 회원의 포인트 내역을 조회 -> 시작 또는 끝이 null일 경우")
    void getPoints_fail() {
        Page<GetPointAdminResponseDto> point
                = pointHistoryRepository.getPoints(pageable, LocalDateTime.of(2023, 2, 1, 0, 0), null);

        assertThat(point.getContent().get(0).getPointHistoryReason()).isEqualTo(pointHistory.getPointHistoryReason());
        assertThat(point.getContent().get(0).getPointHistoryAmount()).isEqualTo(pointHistory.getPointHistoryAmount());
        assertThat(point.getContent().get(0).getCreatedAt()).isEqualTo(pointHistory.getCreatedAt());
        assertThat(point.getContent().get(0).isIncreased()).isTrue();
        assertThat(point.getContent().get(0).getMemberId()).isEqualTo(member.getMemberId());
    }
}