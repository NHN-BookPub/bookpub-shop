package com.nhnacademy.bookpubshop.point.service.impl;

import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.member.exception.MemberNotFoundException;
import com.nhnacademy.bookpubshop.member.repository.MemberRepository;
import com.nhnacademy.bookpubshop.point.dto.request.PointGiftRequestDto;
import com.nhnacademy.bookpubshop.point.dto.response.GetPointResponseDto;
import com.nhnacademy.bookpubshop.point.entity.PointHistory;
import com.nhnacademy.bookpubshop.point.repository.PointHistoryRepository;
import com.nhnacademy.bookpubshop.point.service.PointService;
import com.nhnacademy.bookpubshop.utils.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 포인트 서비스 구현체.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PointServiceImpl implements PointService {
    private static final String GIFT = "포인트 선물";
    private final PointHistoryRepository pointHistoryRepository;
    private final MemberRepository memberRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public PageResponse<GetPointResponseDto> getPointHistory(
            Pageable pageable, String type, Long memberNo) {
        return new PageResponse<>(pointHistoryRepository
                .getPointHistory(pageable, type, memberNo));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void giftPoint(PointGiftRequestDto giftRequestDto) {
        Member giveMember =
                memberRepository.findById(giftRequestDto.getMemberNo())
                .orElseThrow(MemberNotFoundException::new);
        Member receiveMember =
                memberRepository.findMemberByMemberNickname(giftRequestDto.getNickname())
                .orElseThrow(MemberNotFoundException::new);

        giveMember.decreaseMemberPoint(giftRequestDto.getPointAmount());
        receiveMember.increaseMemberPoint(giftRequestDto.getPointAmount());
        updatePointHistory(giftRequestDto, giveMember, receiveMember);
    }

    /**
     * 선물 주고 받은 내역 기록 메소드.
     *
     * @param giftRequestDto 선물 요청 dto.
     * @param giveMember 주는 사람.
     * @param receiveMember 받는사람.
     */
    private void updatePointHistory(PointGiftRequestDto giftRequestDto,
                                    Member giveMember, Member receiveMember) {
        pointHistoryRepository.save(PointHistory.builder()
                .member(giveMember)
                .pointHistoryAmount(giftRequestDto.getPointAmount())
                .pointHistoryReason(GIFT)
                .pointHistoryIncreased(false)
                .build()
        );

        pointHistoryRepository.save(PointHistory.builder()
                .member(receiveMember)
                .pointHistoryAmount(giftRequestDto.getPointAmount())
                .pointHistoryReason(GIFT)
                .pointHistoryIncreased(true)
                .build()
        );
    }
}
