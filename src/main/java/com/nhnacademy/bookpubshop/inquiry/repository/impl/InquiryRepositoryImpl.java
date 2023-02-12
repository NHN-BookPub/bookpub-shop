package com.nhnacademy.bookpubshop.inquiry.repository.impl;

import com.nhnacademy.bookpubshop.category.entity.QCategory;
import com.nhnacademy.bookpubshop.file.entity.QFile;
import com.nhnacademy.bookpubshop.inquiry.dto.response.GetInquiryResponseDto;
import com.nhnacademy.bookpubshop.inquiry.dto.response.GetInquirySummaryMemberResponseDto;
import com.nhnacademy.bookpubshop.inquiry.dto.response.GetInquirySummaryProductResponseDto;
import com.nhnacademy.bookpubshop.inquiry.dto.response.GetInquirySummaryResponseDto;
import com.nhnacademy.bookpubshop.inquiry.entity.Inquiry;
import com.nhnacademy.bookpubshop.inquiry.entity.QInquiry;
import com.nhnacademy.bookpubshop.inquiry.repository.InquiryRepositoryCustom;
import com.nhnacademy.bookpubshop.inquirystatecode.entity.QInquiryStateCode;
import com.nhnacademy.bookpubshop.member.entity.QMember;
import com.nhnacademy.bookpubshop.order.entity.QBookpubOrder;
import com.nhnacademy.bookpubshop.order.relationship.entity.QOrderProduct;
import com.nhnacademy.bookpubshop.product.entity.QProduct;
import com.nhnacademy.bookpubshop.product.relationship.entity.QProductCategory;
import com.nhnacademy.bookpubshop.state.FileCategory;
import com.nhnacademy.bookpubshop.state.InquiryState;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;

/**
 * 상품문의 레포지토리 구현체입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Slf4j
public class InquiryRepositoryImpl extends QuerydslRepositorySupport
        implements InquiryRepositoryCustom {

    QInquiry inquiry = QInquiry.inquiry;
    QOrderProduct orderProduct = QOrderProduct.orderProduct;
    QBookpubOrder order = QBookpubOrder.bookpubOrder;
    QMember member = QMember.member;
    QProduct product = QProduct.product;
    QProductCategory productCategory = QProductCategory.productCategory;
    QFile file = QFile.file;
    QCategory category = QCategory.category;
    QInquiryStateCode inquiryStateCode = QInquiryStateCode.inquiryStateCode;


    public InquiryRepositoryImpl() {
        super(Inquiry.class);
    }

    //문의 관련 -> 모두 회원만 가능함,,? 1:1채팅 제외??
    // 상품문의(문의하기, 문의 내역 보기)
    // 불량상품 신고(신고하기, 신고 내역 보기)
    // 1:1 문의(문의하기, 문의 내역 보기)
    // 1:1 채팅(메인페이지에서 채팅하기, 채팅 내역 보기?)

    // 구매한 회원만 문의 가능, 비공개 및 공개 선택, tui editor 사용
    // 문의는 여러번 가능하며 depth는 한개만

    //관리자 -> 문의글 20개 페이징, 전체문의글 카운팅 및 답변완료 카운팅, 미완료된 문의글 부터 보이게,
    //문의 완료처리
    //문의 검색 가능 -> 상품 카테고리 + (상품명 + 상품코드 + 질문)

    // 회원 구현 ->
    // 문의 하기
    // 문의 내역 보기
    //

    // 관리자 구현 ->


    @Override
    public boolean existsPurchaseHistoryByMemberNo(Long memberNo, Long productNo) {
        return from(order)
                .innerJoin(order.member, member)
                .innerJoin(orderProduct)
                .on(orderProduct.order.orderNo.eq(order.orderNo))
                .where(order.member.memberNo.eq(memberNo))
                .where(orderProduct.product.productNo.eq(productNo))
                .fetchFirst() != null;
    }

    @Override
    public Page<GetInquirySummaryProductResponseDto> findSummaryInquiriesByProduct(Pageable pageable, Long productNo) {
        JPQLQuery<Long> count = from(inquiry)
                .innerJoin(inquiry.member, member)
                .innerJoin(inquiry.product, product)
                .innerJoin(inquiry.inquiryStateCode, inquiryStateCode)
                .where(inquiry.inquiryStateCode.inquiryCodeName.notIn(
                                InquiryState.ANSWER.getName(), InquiryState.ERROR.getName())
                        .and(product.productNo.eq(productNo))
                        .and(inquiry.parentInquiry.isNull()))
                .select(inquiry.count());

        List<GetInquirySummaryProductResponseDto> inquiries =
                from(inquiry)
                        .innerJoin(inquiry.member, member)
                        .innerJoin(inquiry.product, product)
                        .innerJoin(inquiry.inquiryStateCode, inquiryStateCode)
                        .where(inquiry.inquiryStateCode.inquiryCodeName.notIn(
                                        InquiryState.ANSWER.getName())
                                .and(product.productNo.eq(productNo))
                                .and(inquiry.parentInquiry.isNull()))
                        .select(Projections.constructor(
                                GetInquirySummaryProductResponseDto.class,
                                inquiry.inquiryNo,
                                product.productNo,
                                member.memberNo,
                                inquiryStateCode.inquiryCodeName,
                                member.memberNickname,
                                inquiry.inquiryTitle,
                                inquiry.inquiryDisplayed,
                                inquiry.inquiryAnswered,
                                inquiry.createdAt
                        ))
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();


        return PageableExecutionUtils.getPage(inquiries, pageable, count::fetchOne);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetInquirySummaryResponseDto> findSummaryInquiries(Pageable pageable) {
        JPQLQuery<Long> count = from(inquiry)
                .innerJoin(inquiry.product, product)
                .innerJoin(inquiry.inquiryStateCode, inquiryStateCode)
                .leftJoin(file)
                .on(inquiry.product.productNo.eq(file.product.productNo))
                .where(((file.fileCategory.eq(FileCategory.PRODUCT_THUMBNAIL.getCategory()))
                        .or(file.fileCategory.isNull()))
                        .and(inquiry.parentInquiry.isNull())
                        .and(inquiry.inquiryStateCode.inquiryCodeName.notIn(
                                InquiryState.ANSWER.getName(), InquiryState.ERROR.getName())))
                .select(inquiry.count());

        List<GetInquirySummaryResponseDto> inquiries =
                from(inquiry)
                        .innerJoin(inquiry.product, product)
                        .innerJoin(inquiry.inquiryStateCode, inquiryStateCode)
                        .leftJoin(file)
                        .on(inquiry.product.productNo.eq(file.product.productNo))
                        .where(((file.fileCategory.eq(FileCategory.PRODUCT_THUMBNAIL.getCategory()))
                                .or(file.fileCategory.isNull()))
                                .and(inquiry.parentInquiry.isNull())
                                .and(inquiry.inquiryStateCode.inquiryCodeName.notIn(
                                        InquiryState.ANSWER.getName(), InquiryState.ERROR.getName())))
                        .select(Projections.fields(
                                GetInquirySummaryResponseDto.class,
                                inquiry.inquiryNo,
                                inquiry.product.productNo,
                                inquiry.member.memberNo,
                                inquiry.inquiryStateCode.inquiryCodeName.as("inquiryStateCodeName"),
                                inquiry.member.memberNickname,
                                product.title.as("productTitle"),
                                product.productIsbn,
                                file.filePath.as("productImagePath"),
                                inquiry.inquiryTitle,
                                inquiry.inquiryDisplayed,
                                inquiry.inquiryAnswered,
                                inquiry.createdAt))
                        .orderBy(inquiry.inquiryAnswered.asc())
                        .orderBy(inquiry.createdAt.desc())
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();

        inquiries.stream()
                .map(m -> m.getProductCategories().add(
                        String.valueOf(
                                from(category)
                                        .leftJoin(productCategory)
                                        .on(productCategory.category.categoryNo.eq(category.categoryNo))
                                        .select(category.categoryName)
                                        .where(productCategory.product.productNo.eq(m.getProductNo())).fetch())
                )).collect(Collectors.toList());

        return PageableExecutionUtils.getPage(inquiries, pageable, count::fetchOne);
    }

    @Override
    public Page<GetInquirySummaryMemberResponseDto> findMemberInquiries(Pageable pageable, Long memberNo) {
        JPQLQuery<Long> count = from(inquiry)
                .innerJoin(inquiry.product, product)
                .innerJoin(inquiry.inquiryStateCode, inquiryStateCode)
                .leftJoin(file)
                .on(inquiry.product.productNo.eq(file.product.productNo))
                .where(inquiry.member.memberNo.eq(memberNo)
                        .and((file.fileCategory.eq(FileCategory.PRODUCT_THUMBNAIL.getCategory()))
                                .or(file.fileCategory.isNull()))
                        .and(inquiry.parentInquiry.isNull())
                        .and(inquiry.inquiryStateCode.inquiryCodeName.notIn(
                                InquiryState.ANSWER.getName())))
                .select(inquiry.count());

        List<GetInquirySummaryMemberResponseDto> inquiries =
                from(inquiry)
                        .innerJoin(inquiry.product, product)
                        .innerJoin(inquiry.inquiryStateCode, inquiryStateCode)
                        .leftJoin(file)
                        .on(inquiry.product.productNo.eq(file.product.productNo))
                        .where(inquiry.member.memberNo.eq(memberNo)
                                .and((file.fileCategory.eq(FileCategory.PRODUCT_THUMBNAIL.getCategory()))
                                        .or(file.fileCategory.isNull()))
                                .and(inquiry.parentInquiry.isNull())
                                .and(inquiry.inquiryStateCode.inquiryCodeName.notIn(
                                        InquiryState.ANSWER.getName())))
                        .select(Projections.constructor(
                                GetInquirySummaryMemberResponseDto.class,
                                inquiry.inquiryNo,
                                inquiry.product.productNo,
                                inquiry.member.memberNo,
                                inquiry.inquiryStateCode.inquiryCodeName,
                                product.title.as("productTitle"),
                                file.filePath.as("productImagePath"),
                                inquiry.inquiryTitle,
                                inquiry.inquiryDisplayed,
                                inquiry.inquiryAnswered,
                                inquiry.createdAt))
                        .orderBy(inquiry.createdAt.desc())
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();

        return PageableExecutionUtils.getPage(inquiries, pageable, count::fetchOne);
    }

    @Override
    public Optional<GetInquiryResponseDto> findInquiry(Long inquiryNo) {
        QInquiry childInquiry = QInquiry.inquiry;

        GetInquiryResponseDto parent = from(inquiry)
                .innerJoin(inquiry.member, member)
                .innerJoin(inquiry.product, product)
                .innerJoin(inquiry.inquiryStateCode, inquiryStateCode)
                .where(inquiry.inquiryNo.eq(inquiryNo))
                .select(Projections.fields(
                        GetInquiryResponseDto.class,
                        inquiry.inquiryNo,
                        inquiry.member.memberNo,
                        inquiry.product.productNo,
                        inquiryStateCode.inquiryCodeName.as("inquiryStateCodeName"),
                        member.memberNickname,
                        product.title.as("productTitle"),
                        inquiry.inquiryTitle,
                        inquiry.inquiryContent,
                        inquiry.inquiryDisplayed,
                        inquiry.inquiryAnswered,
                        inquiry.createdAt)
                )
                .fetchOne();

        parent.addChild(from(childInquiry)
                .innerJoin(childInquiry.member, member)
                .innerJoin(childInquiry.product, product)
                .innerJoin(inquiry.inquiryStateCode, inquiryStateCode)
                .where(childInquiry.parentInquiry.inquiryNo.eq(parent.getInquiryNo()))
                .select(Projections.fields(
                        GetInquiryResponseDto.class,
                        inquiry.inquiryNo,
                        inquiry.member.memberNo,
                        inquiry.product.productNo,
                        inquiryStateCode.inquiryCodeName.as("inquiryStateCodeName"),
                        member.memberNickname,
                        product.title.as("productTitle"),
                        inquiry.inquiryTitle,
                        inquiry.inquiryContent,
                        inquiry.inquiryDisplayed,
                        inquiry.inquiryAnswered,
                        inquiry.createdAt)
                ).fetch());

        return Optional.of(parent);
    }
}