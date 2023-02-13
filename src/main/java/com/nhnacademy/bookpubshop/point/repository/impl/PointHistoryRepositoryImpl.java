package com.nhnacademy.bookpubshop.point.repository.impl;

import com.nhnacademy.bookpubshop.member.entity.QMember;
import com.nhnacademy.bookpubshop.point.dto.response.GetPointResponseDto;
import com.nhnacademy.bookpubshop.point.entity.PointHistory;
import com.nhnacademy.bookpubshop.point.entity.QPointHistory;
import com.nhnacademy.bookpubshop.point.repository.PointHistoryRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;

/**
 * 포인트 레포 구현체.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@Slf4j
public class PointHistoryRepositoryImpl extends QuerydslRepositorySupport
        implements PointHistoryRepositoryCustom {
    private static final String ALL = "1";
    private static final String GET = "2";

    QPointHistory pointHistory = QPointHistory.pointHistory;
    QMember member = QMember.member;

    public PointHistoryRepositoryImpl() {
        super(PointHistory.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetPointResponseDto> getPointHistory(
            Pageable pageable, String type, Long memberNo) {
        if (type.equals(ALL)) {
            return getPointHistoryByType(pageable, memberNo);
        }

        return type.equals(GET)
                ? getPointHistoryByType(pageable, true, memberNo)
                : getPointHistoryByType(pageable, false, memberNo);
    }

    /**
     * 포인트 사용내역을 증감에 따라 가져오는 메소드.
     *
     * @param pageable    페이저블.
     * @param isIncreased 증감여부.
     * @param memberNo    회원번호.
     * @return 포인트 사용내용.
     */
    public Page<GetPointResponseDto> getPointHistoryByType(
            Pageable pageable, boolean isIncreased, Long memberNo) {
        JPQLQuery<GetPointResponseDto> query = from(pointHistory)
                .innerJoin(pointHistory.member, member)
                .select(Projections.constructor(
                        GetPointResponseDto.class,
                        pointHistory.pointHistoryAmount,
                        pointHistory.pointHistoryReason,
                        pointHistory.createdAt,
                        pointHistory.pointHistoryIncreased
                )).where(pointHistory.member.memberNo.eq(memberNo))
                .where(pointHistory.pointHistoryIncreased.eq(isIncreased))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset());

        JPQLQuery<Long> count = from(pointHistory)
                .select(pointHistory.pointHistoryNo.count())
                .innerJoin(pointHistory.member, member)
                .where(pointHistory.member.memberNo.eq(memberNo)
                        .and(pointHistory.pointHistoryIncreased.eq(isIncreased)));

        return PageableExecutionUtils.getPage(query.fetch(), pageable, count::fetchOne);
    }

    /**
     * 포인트 사용내역을 증감 상관없이 모두 가져오는 메소드.
     *
     * @param pageable 페이저블.
     * @param memberNo 회원번호.
     * @return 포인트 사용내용.
     */
    public Page<GetPointResponseDto> getPointHistoryByType(
            Pageable pageable, Long memberNo) {
        JPQLQuery<GetPointResponseDto> query = from(pointHistory)
                .innerJoin(pointHistory.member, member)
                .select(Projections.constructor(
                        GetPointResponseDto.class,
                        pointHistory.pointHistoryAmount,
                        pointHistory.pointHistoryReason,
                        pointHistory.createdAt,
                        pointHistory.pointHistoryIncreased
                )).where(pointHistory.member.memberNo.eq(memberNo))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetchAll();

        JPQLQuery<Long> count = from(pointHistory)
                .select(pointHistory.pointHistoryNo.count())
                .innerJoin(pointHistory.member, member)
                .where(pointHistory.member.memberNo.eq(memberNo));

        return PageableExecutionUtils.getPage(query.fetch(), pageable, count::fetchOne);
    }
}
