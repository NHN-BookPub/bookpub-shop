package com.nhnacademy.bookpubshop.member.repository;

import com.nhnacademy.bookpubshop.member.dto.SignUpMemberResponseDto;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.member.entity.QMember;
import com.nhnacademy.bookpubshop.tier.entity.QTier;
import com.querydsl.core.types.Projections;
import java.util.Optional;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

/**
 * MemberRepositoryCustom 구현체.
 *
 * @author : 임태원
 * @since : 1.0
 **/
public class MemberRepositoryImpl extends QuerydslRepositorySupport
        implements MemberRepositoryCustom {

    public MemberRepositoryImpl() {
        super(Member.class);
    }

    @Override
    public Optional<SignUpMemberResponseDto> findByMemberNickname(String nickname) {
        QMember member = QMember.member;
        QTier tier = QTier.tier;

        return Optional.of(
                from(member)
                        .innerJoin(member.tier, tier)
                        .select(Projections.constructor(
                                SignUpMemberResponseDto.class,
                                member.memberId,
                                member.memberNickname,
                                member.memberEmail,
                                tier.tierName
                        ))
                        .where(member.memberNickname.eq(nickname))
                        .fetchOne()
        );
    }

    @Override
    public Optional<SignUpMemberResponseDto> findByMemberId(String id) {
        QMember member = QMember.member;
        QTier tier = QTier.tier;

        return Optional.of(
                from(member)
                        .innerJoin(member.tier, tier)
                        .select(Projections.constructor(
                                SignUpMemberResponseDto.class,
                                member.memberId,
                                member.memberNickname,
                                member.memberEmail,
                                tier.tierName
                        ))
                        .where(member.memberId.eq(id))
                        .fetchOne()
        );
    }

    @Override
    public Optional<SignUpMemberResponseDto> findByMemberEmail(String email) {
        QMember member = QMember.member;
        QTier tier = QTier.tier;

        return Optional.of(
                from(member)
                        .innerJoin(member.tier, tier)
                        .select(Projections.constructor(
                                SignUpMemberResponseDto.class,
                                member.memberId,
                                member.memberNickname,
                                member.memberEmail,
                                tier.tierName
                        ))
                        .where(member.memberEmail.eq(email))
                        .fetchOne()
        );
    }
}
