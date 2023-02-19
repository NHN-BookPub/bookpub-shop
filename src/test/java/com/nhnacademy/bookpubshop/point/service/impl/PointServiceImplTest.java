package com.nhnacademy.bookpubshop.point.service.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nhnacademy.bookpubshop.member.dummy.MemberDummy;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.member.exception.MemberNotFoundException;
import com.nhnacademy.bookpubshop.member.repository.MemberRepository;
import com.nhnacademy.bookpubshop.point.dto.request.PointGiftRequestDto;
import com.nhnacademy.bookpubshop.point.dto.response.GetPointAdminResponseDto;
import com.nhnacademy.bookpubshop.point.dto.response.GetPointResponseDto;
import com.nhnacademy.bookpubshop.point.repository.PointHistoryRepository;
import com.nhnacademy.bookpubshop.point.service.PointService;
import com.nhnacademy.bookpubshop.tier.dummy.TierDummy;
import com.nhnacademy.bookpubshop.tier.entity.BookPubTier;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * 포인트 서비스 테스트.
 *
 * @author : 임태원
 * @since : 1.0
 **/
class PointServiceImplTest {
    PointService pointService;
    PointHistoryRepository pointHistoryRepository;
    MemberRepository memberRepository;
    BookPubTier tier;
    Member member;
    Member member2;

    @BeforeEach
    void setUp() {
        pointHistoryRepository = Mockito.mock(PointHistoryRepository.class);
        memberRepository = Mockito.mock(MemberRepository.class);
        pointService = new PointServiceImpl(
                pointHistoryRepository,
                memberRepository
        );
        tier = TierDummy.dummy();
        member = MemberDummy.dummy(tier);
        member2 = MemberDummy.dummy2(tier);
    }

    @Test
    @DisplayName("포인트 내역리스트를 불러오는 메소드")
    void getPointHistory() {
        GetPointResponseDto dto = new GetPointResponseDto(
                100L,
                "reason",
                LocalDateTime.of(2023, 2, 18, 0, 0),
                true
        );
        List<GetPointResponseDto> dto1 = new ArrayList<>();
        dto1.add(dto);
        Pageable pageable = Pageable.ofSize(10);

        Page<GetPointResponseDto> page = PageableExecutionUtils.getPage(dto1, pageable, dto1::size);
        when(pointHistoryRepository.getPointHistory(any(), anyString(), anyLong()))
                .thenReturn(page);

        pointService.getPointHistory(pageable, "1", 1L);

        verify(pointHistoryRepository, times(1)).getPointHistory(any(), anyString(), anyLong());
    }

    @Test
    @DisplayName("포인트 선물하는 메소드 성공")
    void giftPoint() {
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.of(member));
        when(memberRepository.findMemberByMemberNickname(anyString()))
                .thenReturn(Optional.of(member2));

        PointGiftRequestDto dto = new PointGiftRequestDto();
        ReflectionTestUtils.setField(dto, "nickname", "test_nickname");
        ReflectionTestUtils.setField(dto, "pointAmount", 100L);

        pointService.giftPoint(1L, dto);

        verify(memberRepository, times(1)).findById(anyLong());
        verify(memberRepository, times(1)).findMemberByMemberNickname(anyString());
    }

    @Test
    @DisplayName("포인트 선물하는 메소드 실패 - 선물 보내는 사람이 없음")
    void giftPoint_fail_giftPeople_no() {
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        when(memberRepository.findMemberByMemberNickname(anyString()))
                .thenReturn(Optional.of(member));

        PointGiftRequestDto dto = new PointGiftRequestDto();
        ReflectionTestUtils.setField(dto, "nickname", "nickname");
        ReflectionTestUtils.setField(dto, "pointAmount", 100L);

        assertThatThrownBy(() -> pointService.giftPoint(member.getMemberNo(), dto))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessageContaining(MemberNotFoundException.MESSAGE);
    }

    @Test
    @DisplayName("포인트 선물하는 메소드 성공 - 선물 받는 사람이 없음")
    void giftPoint_fail_receivePeople_no() {
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.of(member));
        when(memberRepository.findMemberByMemberNickname(anyString()))
                .thenReturn(Optional.empty());

        PointGiftRequestDto dto = new PointGiftRequestDto();
        ReflectionTestUtils.setField(dto, "nickname", "nickname");
        ReflectionTestUtils.setField(dto, "pointAmount", 100L);

        assertThatThrownBy(() -> pointService.giftPoint(member.getMemberNo(), dto))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessageContaining(MemberNotFoundException.MESSAGE);
    }

    @Test
    @DisplayName("모든 회원의 포인트 내역 조회")
    void getPoints() {
        GetPointAdminResponseDto dto = new GetPointAdminResponseDto(
                "memberId",
                100L,
                "reason",
                LocalDateTime.of(2023, 2, 18, 0, 0),
                true
        );
        List<GetPointAdminResponseDto> dto1 = new ArrayList<>();
        dto1.add(dto);
        Pageable pageable = Pageable.ofSize(10);

        Page<GetPointAdminResponseDto> page = PageableExecutionUtils.getPage(dto1, pageable, dto1::size);

        when(pointHistoryRepository.getPoints(any(), any(), any()))
                .thenReturn(page);

        pointService.getPoints(pageable, LocalDateTime.of(2023, 2, 1, 0, 0), LocalDateTime.of(2024, 2, 1, 0, 0));

        verify(pointHistoryRepository, times(1)).getPoints(any(), any(), any());

    }
}