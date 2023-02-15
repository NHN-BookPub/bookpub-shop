package com.nhnacademy.bookpubshop.personalinquiry.repository.impl;

import com.nhnacademy.bookpubshop.member.entity.QMember;
import com.nhnacademy.bookpubshop.personalinquiry.dto.response.GetPersonalInquiryResponseDto;
import com.nhnacademy.bookpubshop.personalinquiry.dto.response.GetSimplePersonalInquiryResponseDto;
import com.nhnacademy.bookpubshop.personalinquiry.entity.PersonalInquiry;
import com.nhnacademy.bookpubshop.personalinquiry.entity.QPersonalInquiry;
import com.nhnacademy.bookpubshop.personalinquiry.repository.PersonalInquiryRepositoryCustom;
import com.nhnacademy.bookpubshop.personalinquiryanswer.entity.QPersonalInquiryAnswer;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;

/**
 * 1대1문의 레포지토리 구현체입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public class PersonalInquiryRepositoryImpl extends QuerydslRepositorySupport
        implements PersonalInquiryRepositoryCustom {
    public PersonalInquiryRepositoryImpl() {
        super(PersonalInquiry.class);
    }

    QPersonalInquiry personalInquiry = QPersonalInquiry.personalInquiry;
    QPersonalInquiryAnswer personalInquiryAnswer = QPersonalInquiryAnswer.personalInquiryAnswer;
    QMember member = QMember.member;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetSimplePersonalInquiryResponseDto> findMemberPersonalInquiries(
            Pageable pageable, Long memberNo) {
        List<GetSimplePersonalInquiryResponseDto> content =
                from(personalInquiry)
                        .innerJoin(personalInquiry.member, member)
                        .select(Projections.constructor(
                                GetSimplePersonalInquiryResponseDto.class,
                                personalInquiry.personalInquiryNo,
                                member.memberNickname,
                                personalInquiry.inquiryTitle,
                                personalInquiry.inquiryAnswered,
                                personalInquiry.createdAt
                        ))
                        .where(personalInquiry.inquiryDeleted.isFalse()
                                .and(personalInquiry.member.memberNo.eq(memberNo)))
                        .orderBy(personalInquiry.createdAt.desc())
                        .limit(pageable.getPageSize())
                        .offset(pageable.getOffset())
                        .fetch();

        JPQLQuery<Long> count = from(personalInquiry)
                .innerJoin(personalInquiry.member, member)
                .where(personalInquiry.inquiryDeleted.isFalse()
                        .and(personalInquiry.member.memberNo.eq(memberNo)))
                .select(personalInquiry.count());

        return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetSimplePersonalInquiryResponseDto> findPersonalInquiries(Pageable pageable) {
        List<GetSimplePersonalInquiryResponseDto> content =
                from(personalInquiry)
                        .innerJoin(personalInquiry.member, member)
                        .select(Projections.constructor(
                                GetSimplePersonalInquiryResponseDto.class,
                                personalInquiry.personalInquiryNo,
                                member.memberNickname,
                                personalInquiry.inquiryTitle,
                                personalInquiry.inquiryAnswered,
                                personalInquiry.createdAt
                        ))
                        .where(personalInquiry.inquiryDeleted.isFalse())
                        .orderBy(personalInquiry.inquiryAnswered.asc())
                        .orderBy(personalInquiry.createdAt.desc())
                        .limit(pageable.getPageSize())
                        .offset(pageable.getOffset())
                        .fetch();

        JPQLQuery<Long> count = from(personalInquiry)
                .innerJoin(personalInquiry.member, member)
                .where(personalInquiry.inquiryDeleted.isFalse())
                .select(personalInquiry.count());

        return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<GetPersonalInquiryResponseDto> findPersonalInquiry(Long personalInquiryNo) {
        return Optional.of(from(personalInquiry)
                .innerJoin(personalInquiry.member, member)
                .leftJoin(personalInquiryAnswer)
                .on(personalInquiry.personalInquiryNo
                        .eq(personalInquiryAnswer.personalInquiry.personalInquiryNo))
                .select(Projections.fields(
                        GetPersonalInquiryResponseDto.class,
                        personalInquiry.personalInquiryNo.as("inquiryNo"),
                        member.memberNickname,
                        personalInquiry.inquiryTitle,
                        personalInquiry.inquiryContent,
                        personalInquiry.inquiryAnswered,
                        personalInquiry.createdAt,
                        personalInquiryAnswer.answerNo.as("inquiryAnswerNo"),
                        personalInquiryAnswer.answerContent.as("inquiryAnswerContent"),
                        personalInquiryAnswer.createdAt.as("answerCreatedAt")
                ))
                .where(personalInquiry.personalInquiryNo.eq(personalInquiryNo))
                .fetchOne());
    }
}
