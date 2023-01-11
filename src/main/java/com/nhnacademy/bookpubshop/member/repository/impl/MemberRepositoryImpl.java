package com.nhnacademy.bookpubshop.member.repository.impl;

import com.nhnacademy.bookpubshop.authority.entity.Authority;
import com.nhnacademy.bookpubshop.authority.entity.QAuthority;
import com.nhnacademy.bookpubshop.member.dto.response.LoginMemberResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.MemberDetailResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.MemberResponseDto;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.member.entity.QMember;
import com.nhnacademy.bookpubshop.member.exception.MemberNotFoundException;
import com.nhnacademy.bookpubshop.member.relationship.entity.QMemberAuthority;
import com.nhnacademy.bookpubshop.member.relationship.exception.MemberAuthoritiesNotFoundException;
import com.nhnacademy.bookpubshop.member.repository.MemberCustomRepository;
import com.nhnacademy.bookpubshop.tier.entity.QBookPubTier;
import com.querydsl.core.types.Projections;
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
        // 권한들? 이 들어가게 되면 수정
        return Optional.ofNullable(from(member, memberAuthority)
                .innerJoin(member.tier, tier)
                .innerJoin(memberAuthority.member, member)
                .innerJoin(memberAuthority.authority, authority)
                .where(member.memberNo.eq(memberNo))
                .select(Projections.constructor(MemberDetailResponseDto.class,
                        member.memberNo,
                        tier.tierName,
                        member.memberNickname.as("nickname"),
                        member.memberGender.as("gender"),
                        member.memberBirthMonth.as("birthMonth"),
                        member.memberBirthYear.as("birthYear"),
                        member.memberPhone.as("phone"),
                        member.memberEmail.as("email"),
                        member.memberPoint.as("point"),
                        authority.authorityName.as("authority")
                )).fetchOne());
    }


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
                                member.socialJoined.as("isSocial")
                        )
                ).offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
    }

    @Override
    public LoginMemberResponseDto findByMemberLoginInfo(String id, String pwd) {
        QMember member = QMember.member;
        QMemberAuthority memberAuthority = QMemberAuthority.memberAuthority;

        Optional<String> findMemberId = Optional.ofNullable(from(member)
                .select(member.memberId)
                .where(member.memberId.eq(id))
                .where(member.memberPwd.eq(pwd))
                .fetchOne());

        Optional<List<Authority>> memberAuthorities = Optional.of(from(memberAuthority)
                .innerJoin(memberAuthority.member, member)
                .select(memberAuthority.authority)
                .fetch());

        String memberId = findMemberId.orElseThrow(() -> new MemberNotFoundException(id));

        List<Authority> authorities = memberAuthorities.orElseThrow(MemberAuthoritiesNotFoundException::new);

        return new LoginMemberResponseDto(memberId, authorities);
    }
}
