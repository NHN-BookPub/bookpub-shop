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
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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

    private static final String PRODUCT_TITLE = "productTitle";
    private static final String INQUIRY_STATE_CODE_NAME = "inquiryStateCodeName";
    private static final String PRODUCT_IMAGE_PATH = "productImagePath";


    public InquiryRepositoryImpl() {
        super(Inquiry.class);
    }


    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetInquirySummaryProductResponseDto> findSummaryInquiriesByProduct(
            Pageable pageable, Long productNo) {
        JPQLQuery<Long> count = from(inquiry)
                .innerJoin(inquiry.member, member)
                .on(inquiry.member.memberNo.eq(member.memberNo))
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
                        .on(inquiry.member.memberNo.eq(member.memberNo))
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
    public Page<GetInquirySummaryResponseDto> findSummaryErrorInquiries(Pageable pageable) {
        JPQLQuery<Long> count = from(inquiry)
                .innerJoin(inquiry.product, product)
                .innerJoin(inquiry.inquiryStateCode, inquiryStateCode)
                .innerJoin(inquiry.member, member)
                .on(inquiry.member.memberNo.eq(member.memberNo))
                .where(inquiry.parentInquiry.isNull()
                        .and(inquiry.inquiryStateCode.inquiryCodeName
                                .eq(InquiryState.ERROR.getName())))
                .select(inquiry.count());

        List<GetInquirySummaryResponseDto> inquiries =
                from(inquiry)
                        .innerJoin(inquiry.product, product)
                        .innerJoin(inquiry.inquiryStateCode, inquiryStateCode)
                        .innerJoin(inquiry.member, member)
                        .on(inquiry.member.memberNo.eq(member.memberNo))
                        .where(inquiry.parentInquiry.isNull()
                                .and(inquiry.inquiryStateCode.inquiryCodeName
                                        .eq(InquiryState.ERROR.getName())))
                        .select(Projections.fields(
                                GetInquirySummaryResponseDto.class,
                                inquiry.inquiryNo,
                                inquiry.product.productNo,
                                member.memberNo,
                                inquiry.inquiryStateCode.inquiryCodeName
                                        .as(INQUIRY_STATE_CODE_NAME),
                                member.memberNickname,
                                product.title.as(PRODUCT_TITLE),
                                inquiry.inquiryTitle,
                                inquiry.inquiryDisplayed,
                                inquiry.inquiryAnswered,
                                inquiry.createdAt))
                        .orderBy(inquiry.inquiryAnswered.asc())
                        .orderBy(inquiry.createdAt.desc())
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();

        return PageableExecutionUtils.getPage(inquiries, pageable, count::fetchOne);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetInquirySummaryResponseDto> findSummaryInquiries(
            Pageable pageable,
            String searchKeyFir, String searchValueFir,
            String searchKeySec, String searchValueSec) {
        JPQLQuery<Long> count = from(inquiry)
                .innerJoin(inquiry.product, product)
                .innerJoin(inquiry.inquiryStateCode, inquiryStateCode)
                .innerJoin(inquiry.member, member)
                .on(inquiry.member.memberNo.eq(member.memberNo))
                .leftJoin(productCategory)
                .on(inquiry.product.productNo.eq(productCategory.product.productNo))
                .innerJoin(category)
                .on(productCategory.category.categoryNo.eq(category.categoryNo))
                .where((inquiry.parentInquiry.isNull())
                        .and(inquiry.inquiryStateCode.inquiryCodeName.notIn(
                                InquiryState.ANSWER.getName(), InquiryState.ERROR.getName()))
                        .and(searchFirEq(searchKeyFir, searchValueFir))
                        .and(searchSecEq(searchKeySec, searchValueSec)))
                .select(inquiry.count());

        List<GetInquirySummaryResponseDto> inquiries =
                from(inquiry)
                        .innerJoin(inquiry.product, product)
                        .innerJoin(inquiry.inquiryStateCode, inquiryStateCode)
                        .innerJoin(inquiry.member, member)
                        .on(inquiry.member.memberNo.eq(member.memberNo))
                        .leftJoin(productCategory)
                        .on(inquiry.product.productNo.eq(productCategory.product.productNo))
                        .innerJoin(category)
                        .on(productCategory.category.categoryNo.eq(category.categoryNo))
                        .where((inquiry.parentInquiry.isNull())
                                .and(inquiry.inquiryStateCode.inquiryCodeName.notIn(
                                        InquiryState.ANSWER.getName(),
                                        InquiryState.ERROR.getName()))
                                .and(searchFirEq(searchKeyFir, searchValueFir))
                                .and(searchSecEq(searchKeySec, searchValueSec)))
                        .select(Projections.fields(
                                GetInquirySummaryResponseDto.class,
                                inquiry.inquiryNo,
                                inquiry.product.productNo,
                                inquiry.member.memberNo,
                                inquiry.inquiryStateCode.inquiryCodeName
                                        .as(INQUIRY_STATE_CODE_NAME),
                                member.memberNickname,
                                product.title.as(PRODUCT_TITLE),
                                inquiry.inquiryTitle,
                                inquiry.inquiryDisplayed,
                                inquiry.inquiryAnswered,
                                inquiry.createdAt))
                        .orderBy(inquiry.inquiryAnswered.asc())
                        .orderBy(inquiry.createdAt.desc())
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .distinct()
                        .fetch();

        return PageableExecutionUtils.getPage(inquiries, pageable, count::fetchOne);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetInquirySummaryMemberResponseDto> findMemberInquiries(
            Pageable pageable, Long memberNo) {
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
                                .and((file.fileCategory
                                        .eq(FileCategory.PRODUCT_THUMBNAIL.getCategory()))
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
                                product.title.as(PRODUCT_TITLE),
                                file.filePath.as(PRODUCT_IMAGE_PATH),
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

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<GetInquiryResponseDto> findInquiry(Long inquiryNo) {
        QInquiry childInquiry = QInquiry.inquiry;

        GetInquiryResponseDto parent = from(inquiry)
                .innerJoin(inquiry.member, member)
                .on(inquiry.member.memberNo.eq(member.memberNo))
                .innerJoin(inquiry.product, product)
                .innerJoin(inquiry.inquiryStateCode, inquiryStateCode)
                .where(inquiry.inquiryNo.eq(inquiryNo))
                .select(Projections.fields(
                        GetInquiryResponseDto.class,
                        inquiry.inquiryNo,
                        inquiry.member.memberNo,
                        inquiry.product.productNo,
                        inquiryStateCode.inquiryCodeName.as(INQUIRY_STATE_CODE_NAME),
                        member.memberNickname,
                        product.title.as(PRODUCT_TITLE),
                        inquiry.inquiryTitle,
                        inquiry.inquiryContent,
                        inquiry.inquiryDisplayed,
                        inquiry.inquiryAnswered,
                        inquiry.createdAt)
                )
                .fetchOne();

        parent.addChild(from(childInquiry)
                .innerJoin(childInquiry.member, member)
                .on(inquiry.member.memberNo.eq(member.memberNo))
                .innerJoin(childInquiry.product, product)
                .innerJoin(inquiry.inquiryStateCode, inquiryStateCode)
                .where(childInquiry.parentInquiry.inquiryNo.eq(parent.getInquiryNo()))
                .select(Projections.fields(
                        GetInquiryResponseDto.class,
                        inquiry.inquiryNo,
                        inquiry.member.memberNo,
                        inquiry.product.productNo,
                        inquiryStateCode.inquiryCodeName.as(INQUIRY_STATE_CODE_NAME),
                        member.memberNickname,
                        product.title.as(PRODUCT_TITLE),
                        inquiry.inquiryTitle,
                        inquiry.inquiryContent,
                        inquiry.inquiryDisplayed,
                        inquiry.inquiryAnswered,
                        inquiry.createdAt)
                ).fetch());

        return Optional.of(parent);
    }

    /**
     * 상품문의 검색 시 키, 값으로 검색해오기 위한 메서드입니다.
     *
     * @param searchKeyFir   검색 조건
     * @param searchValueFir 검색 값
     * @return querydsl 조건절
     */
    private BooleanExpression searchFirEq(String searchKeyFir, String searchValueFir) {
        if (Objects.isNull(searchKeyFir) || Objects.isNull(searchValueFir)
                || (searchKeyFir.isBlank()) || (searchValueFir.isBlank())) {
            return null;
        }
        if (searchKeyFir.equals("category")) {
            return category.categoryName.contains(searchValueFir);
        } else {
            return null;
        }
    }

    /**
     * 상품문의 검색 시 키, 값으로 검색해오기 위한 메서드입니다.
     *
     * @param searchKeySec   검색 조건 두번째
     * @param searchValueSec 검색 값 두번째
     * @return querydsl 조건절
     */
    private BooleanExpression searchSecEq(String searchKeySec, String searchValueSec) {
        if (Objects.isNull(searchKeySec) || Objects.isNull(searchValueSec)
                || (searchKeySec.isBlank()) || (searchValueSec.isBlank())) {
            return null;
        }
        switch (searchKeySec) {
            case PRODUCT_TITLE:
                return inquiry.product.title.contains(searchValueSec);
            case "productIsbn":
                return inquiry.product.productIsbn.eq(searchValueSec);
            case "inquiryTitle":
                return inquiry.inquiryTitle.contains(searchValueSec);
            default:
                return null;
        }
    }
}
