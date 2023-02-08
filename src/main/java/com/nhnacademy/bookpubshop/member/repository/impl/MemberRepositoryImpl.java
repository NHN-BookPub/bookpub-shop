package com.nhnacademy.bookpubshop.member.repository.impl;

import com.nhnacademy.bookpubshop.authority.entity.QAuthority;
import com.nhnacademy.bookpubshop.member.dto.response.IdPwdMemberDto;
import com.nhnacademy.bookpubshop.member.dto.response.LoginMemberResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.MemberAuthResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.MemberDetailResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.MemberResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.MemberStatisticsResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.MemberTierStatisticsResponseDto;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.member.entity.QMember;
import com.nhnacademy.bookpubshop.member.exception.MemberNotFoundException;
import com.nhnacademy.bookpubshop.member.relationship.entity.QMemberAuthority;
import com.nhnacademy.bookpubshop.member.relationship.exception.MemberAuthoritiesNotFoundException;
import com.nhnacademy.bookpubshop.member.repository.MemberCustomRepository;
import com.nhnacademy.bookpubshop.tier.entity.QBookPubTier;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;

/**
 * 멤버 테이블을 QueryDsl 을 사용하기위한 클래스입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
public class MemberRepositoryImpl extends QuerydslRepositorySupport
        implements MemberCustomRepository {

    public MemberRepositoryImpl() {
        super(Member.class);
    }


    @Override
    public Optional<MemberDetailResponseDto> findByMemberDetails(Long memberNo) {
        QMember member = QMember.member;
        QBookPubTier tier = QBookPubTier.bookPubTier;
        QMemberAuthority memberAuthority = QMemberAuthority.memberAuthority;
        QAuthority authority = QAuthority.authority;

        Optional<Member> content = Optional.ofNullable(from(member)
                .leftJoin(memberAuthority)
                .on(member.memberNo.eq(memberAuthority.member.memberNo))
                .leftJoin(member.tier, tier)
                .innerJoin(memberAuthority.authority, authority)
                .where(member.memberNo.eq(memberNo))
                .select(member)
                .fetchOne());

        return content.map(MemberDetailResponseDto::new);
    }

    @Override
    public MemberStatisticsResponseDto memberStatistics() {
        QMember member = QMember.member;
        QBookPubTier tier = QBookPubTier.bookPubTier;

        return from(member)
                .leftJoin(member.tier, tier)
                .select(
                        Projections.constructor(MemberStatisticsResponseDto.class,
                                member.memberNo.count().as("memberCnt"),
                                ExpressionUtils.as(
                                        JPAExpressions.select(member.memberNo.count())
                                                .where(member.memberBlocked.ne(true)
                                                        .and(member.memberDeleted.ne(true)))
                                                .from(member)
                                        , "currentMemberCnt"),
                                ExpressionUtils.as(
                                        JPAExpressions.select(member.memberNo.count())
                                                .where(member.memberDeleted.eq(true))
                                                .from(member)
                                        , "deleteMemberCnt"),
                                ExpressionUtils.as(
                                        JPAExpressions.select(member.memberNo.count())
                                                .where(member.memberBlocked.eq(true))
                                                .from(member)
                                        , "blockMemberCnt")))
                .fetchOne();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<MemberTierStatisticsResponseDto> memberTierStatistics() {
        QMember member = QMember.member;
        QBookPubTier tier = QBookPubTier.bookPubTier;

        return from(member)
                .leftJoin(member.tier, tier)
                .select(Projections.constructor(
                        MemberTierStatisticsResponseDto.class,
                        member.tier.tierName.as("tierName"),
                        member.tier.tierValue.as("tierValue"),
                        member.tier.tierNo.count().as("tierCnt")
                ))
                .groupBy(tier.tierName)
                .distinct()
                .fetch();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Page<MemberResponseDto> findMembers(Pageable pageable) {
        QMember member = QMember.member;
        QBookPubTier tier = QBookPubTier.bookPubTier;

        JPQLQuery<Long> count = from(member)
                .select(member.count())
                .innerJoin(member.tier, tier);

        List<MemberResponseDto> content = from(member)
                .innerJoin(member.tier, tier)
                .select(
                        Projections.constructor(MemberResponseDto.class,
                                member.memberNo,
                                member.tier.tierName.as("tier"),
                                member.memberId,
                                member.memberNickname.as("nickname"),
                                member.memberName.as("name"),
                                member.memberGender.as("gender"),
                                member.memberBirthYear.as("birthYear"),
                                member.memberBirthMonth.as("birthMonth"),
                                member.memberEmail.as("email"),
                                member.memberPoint.as("point"),
                                member.socialJoined.as("isSocial"),
                                member.memberDeleted.as("idDeleted"),
                                member.memberBlocked.as("isBlocked")
                        )
                ).offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LoginMemberResponseDto findByMemberLoginInfo(String id) {
        QMember member = QMember.member;
        QMemberAuthority memberAuthority = QMemberAuthority.memberAuthority;

        Optional<IdPwdMemberDto> findMember = Optional.ofNullable(from(member)
                .select(Projections.constructor(IdPwdMemberDto.class,
                        member.memberNo,
                        member.memberId,
                        member.memberPwd))
                .where(member.memberId.eq(id))
                .fetchOne());

        Optional<List<String>> memberAuthorities = Optional.of(from(memberAuthority)
                .innerJoin(memberAuthority.member, member)
                .select(memberAuthority.authority.authorityName)
                .where(member.memberId.eq(id))
                .fetch());

        IdPwdMemberDto responseMember =
                findMember.orElseThrow(() -> new MemberNotFoundException(id));
        List<String> authorities =
                memberAuthorities.orElseThrow(MemberAuthoritiesNotFoundException::new);

        return new LoginMemberResponseDto(
                responseMember.getMemberNo(),
                responseMember.getMemberId(),
                responseMember.getMemberPwd(),
                authorities);
    }

    @Override
    public MemberAuthResponseDto findByAuthMemberInfo(Long memberNo) {
        QMember member = QMember.member;
        QMemberAuthority memberAuthority = QMemberAuthority.memberAuthority;

        Optional<IdPwdMemberDto> findMember = Optional.ofNullable(from(member)
                .select(Projections.constructor(IdPwdMemberDto.class,
                        member.memberNo,
                        member.memberId,
                        member.memberPwd))
                .where(member.memberNo.eq(memberNo))
                .fetchOne());

        List<String> memberAuthorities = from(memberAuthority)
                .innerJoin(memberAuthority.member, member)
                .select(memberAuthority.authority.authorityName)
                .where(member.memberNo.eq(memberNo))
                .fetch();

        IdPwdMemberDto responseMember = findMember.orElseThrow(
                () -> new MemberNotFoundException(String.valueOf(memberNo)));

        return new MemberAuthResponseDto(
                responseMember.getMemberNo(),
                responseMember.getMemberPwd(),
                memberAuthorities);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer findTierNoByMemberNo(Long memberNo) {
        QMember member = QMember.member;
        QBookPubTier tier = QBookPubTier.bookPubTier;

        return from(member)
                .join(member.tier, tier)
                .where(member.memberNo.eq(memberNo))
                .select(tier.tierNo)
                .fetchOne();

    }
}
