package com.nhnacademy.bookpubshop.coupontemplate.repository.impl;

import com.nhnacademy.bookpubshop.category.entity.QCategory;
import com.nhnacademy.bookpubshop.couponpolicy.dto.response.GetCouponPolicyResponseDto;
import com.nhnacademy.bookpubshop.couponpolicy.entity.QCouponPolicy;
import com.nhnacademy.bookpubshop.couponstatecode.entity.QCouponStateCode;
import com.nhnacademy.bookpubshop.coupontemplate.dto.response.GetCouponTemplateResponseDto;
import com.nhnacademy.bookpubshop.coupontemplate.dto.response.GetDetailCouponTemplateResponseDto;
import com.nhnacademy.bookpubshop.coupontemplate.entity.CouponTemplate;
import com.nhnacademy.bookpubshop.coupontemplate.entity.QCouponTemplate;
import com.nhnacademy.bookpubshop.coupontemplate.repository.CouponTemplateRepositoryCustom;
import com.nhnacademy.bookpubshop.coupontype.entity.QCouponType;
import com.nhnacademy.bookpubshop.product.entity.QProduct;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

/**
 * CouponTemplate 레포 구현체입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public class CouponTemplateRepositoryImpl extends QuerydslRepositorySupport
        implements CouponTemplateRepositoryCustom {

    public CouponTemplateRepositoryImpl() {
        super(CouponTemplate.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<GetDetailCouponTemplateResponseDto> findDetailByTemplateNo(Long templateNo) {
        return Optional.of(detailQuery().fetchOne());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetDetailCouponTemplateResponseDto> findDetailAllBy(Pageable pageable) {
        QCouponTemplate couponTemplate = QCouponTemplate.couponTemplate;

        Long count = from(couponTemplate)
                .select(couponTemplate.count())
                .fetchOne();

        List<GetDetailCouponTemplateResponseDto> content = detailQuery()
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        return new PageImpl<>(content, pageable, count);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetCouponTemplateResponseDto> findAllBy(Pageable pageable) {
        QCouponTemplate couponTemplate = QCouponTemplate.couponTemplate;

        Long count = from(couponTemplate)
                .select(couponTemplate.count())
                .fetchOne();

        List<GetCouponTemplateResponseDto> content = from(couponTemplate)
                .select(Projections.constructor(GetCouponTemplateResponseDto.class,
                        couponTemplate.templateName,
                        couponTemplate.templateImage,
                        couponTemplate.issuedAt,
                        couponTemplate.finishedAt))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        return new PageImpl<>(content, pageable, count);
    }

    /**
     * 쿠폰템플릿 상세조회를 위한 쿼리문.
     *
     * @return 쿠폰템플릿 상세 조회 쿼리문
     */
    private JPQLQuery<GetDetailCouponTemplateResponseDto> detailQuery() {
        QCouponTemplate couponTemplate = QCouponTemplate.couponTemplate;
        QProduct product = QProduct.product;
        QCouponPolicy couponPolicy = QCouponPolicy.couponPolicy;
        QCouponType couponType = QCouponType.couponType;
        QCategory category = QCategory.category;
        QCouponStateCode couponStateCode = QCouponStateCode.couponStateCode;

        return from(couponTemplate)
                .leftJoin(couponTemplate.couponPolicy, couponPolicy)
                .leftJoin(couponTemplate.couponType, couponType)
                .leftJoin(couponTemplate.product, product)
                .leftJoin(couponTemplate.category, category)
                .leftJoin(couponTemplate.couponStateCode, couponStateCode)
                .select(Projections.constructor(GetDetailCouponTemplateResponseDto.class,
                        couponTemplate.templateNo,
                        Projections.constructor(GetCouponPolicyResponseDto.class,
                                couponPolicy.policyNo,
                                couponPolicy.policyFixed,
                                couponPolicy.discountRate,
                                couponPolicy.policyMinimum,
                                couponPolicy.maxDiscount),
                        couponType.typeName,
                        product.title.as("productTitle"),
                        category.categoryName,
                        couponStateCode.codeTarget,
                        couponTemplate.templateName,
                        couponTemplate.templateImage,
                        couponTemplate.finishedAt,
                        couponTemplate.issuedAt,
                        couponTemplate.templateOverlapped,
                        couponTemplate.templateBundled
                ));
    }
}
