package com.nhnacademy.bookpubshop.point.repository.impl;

import com.nhnacademy.bookpubshop.member.entity.QMember;
import com.nhnacademy.bookpubshop.point.dto.response.GetPointAdminResponseDto;
import com.nhnacademy.bookpubshop.point.dto.response.GetPointResponseDto;
import com.nhnacademy.bookpubshop.point.entity.PointHistory;
import com.nhnacademy.bookpubshop.point.entity.QPointHistory;
import com.nhnacademy.bookpubshop.point.repository.PointHistoryRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import java.time.LocalDateTime;
import java.util.Objects;
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
                .orderBy(pointHistory.createdAt.desc())
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
                .orderBy(pointHistory.createdAt.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetchAll();

        JPQLQuery<Long> count = from(pointHistory)
                .select(pointHistory.pointHistoryNo.count())
                .innerJoin(pointHistory.member, member)
                .where(pointHistory.member.memberNo.eq(memberNo));

        return PageableExecutionUtils.getPage(query.fetch(), pageable, count::fetchOne);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetPointAdminResponseDto> getPoints(Pageable pageable,
                                                    LocalDateTime start,
                                                    LocalDateTime end) {
        JPQLQuery<GetPointAdminResponseDto> content = from(pointHistory)
                .select(Projections.constructor(
                        GetPointAdminResponseDto.class,
                        member.memberId,
                        pointHistory.pointHistoryAmount,
                        pointHistory.pointHistoryReason,
                        pointHistory.createdAt,
                        pointHistory.pointHistoryIncreased)).distinct()
                .innerJoin(pointHistory.member, member)
                .where(checkDate(start, end))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset());

        JPQLQuery<Long> count = from(pointHistory)
                .innerJoin(pointHistory.member, member)
                .where(checkDate(start, end))
                .select(pointHistory.count()).distinct();

        return PageableExecutionUtils.getPage(content.fetch(), pageable, count::fetchOne);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetPointAdminResponseDto> getPointsBySearch(Pageable pageable, LocalDateTime start, LocalDateTime end, String search) {
        JPQLQuery<GetPointAdminResponseDto> content = from(pointHistory)
                .select(Projections.constructor(
                        GetPointAdminResponseDto.class,
                        member.memberId,
                        pointHistory.pointHistoryAmount,
                        pointHistory.pointHistoryReason,
                        pointHistory.createdAt,
                        pointHistory.pointHistoryIncreased)).distinct()
                .innerJoin(pointHistory.member, member)
                .where(checkDate(start, end)
                        .and(member.memberId.contains(search)))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset());

        JPQLQuery<Long> count = from(pointHistory)
                .innerJoin(pointHistory.member, member)
                .where(checkDate(start, end)
                        .and(member.memberId.contains(search)))
                .select(pointHistory.count()).distinct();

        return PageableExecutionUtils.getPage(content.fetch(), pageable, count::fetchOne);
    }

    /**
     * 시작일자와 종료일자를 기준으로 where 조건이 걸립니다.
     *
     * @param start 시작일자
     * @param end   종료일자
     * @return true, false 를 반환합니다.
     */
    private BooleanExpression checkDate(LocalDateTime start, LocalDateTime end) {
        if (Objects.isNull(start) || Objects.isNull(end)) {
            return pointHistory.createdAt
                    .between(LocalDateTime.now().minusMonths(1L), LocalDateTime.now());
        }
        return pointHistory.createdAt.between(start, end);
    }
}
