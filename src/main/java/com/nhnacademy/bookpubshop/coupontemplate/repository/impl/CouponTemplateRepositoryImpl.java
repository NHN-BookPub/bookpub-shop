package com.nhnacademy.bookpubshop.coupontemplate.repository.impl;

import com.nhnacademy.bookpubshop.category.entity.QCategory;
import com.nhnacademy.bookpubshop.couponpolicy.entity.QCouponPolicy;
import com.nhnacademy.bookpubshop.couponstatecode.entity.QCouponStateCode;
import com.nhnacademy.bookpubshop.coupontemplate.dto.response.GetCouponTemplateResponseDto;
import com.nhnacademy.bookpubshop.coupontemplate.dto.response.GetDetailCouponTemplateResponseDto;
import com.nhnacademy.bookpubshop.coupontemplate.entity.CouponTemplate;
import com.nhnacademy.bookpubshop.coupontemplate.entity.QCouponTemplate;
import com.nhnacademy.bookpubshop.coupontemplate.repository.CouponTemplateRepositoryCustom;
import com.nhnacademy.bookpubshop.coupontype.entity.QCouponType;
import com.nhnacademy.bookpubshop.file.entity.QFile;
import com.nhnacademy.bookpubshop.product.entity.QProduct;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;

/**
 * CouponTemplate 레포 구현체입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public class CouponTemplateRepositoryImpl extends QuerydslRepositorySupport
        implements CouponTemplateRepositoryCustom {

    private static final String TEMPLATE_IMAGE = "templateImage";
    QCouponTemplate couponTemplate = QCouponTemplate.couponTemplate;
    QProduct product = QProduct.product;
    QCouponPolicy couponPolicy = QCouponPolicy.couponPolicy;
    QCouponType couponType = QCouponType.couponType;
    QCategory category = QCategory.category;
    QCouponStateCode couponStateCode = QCouponStateCode.couponStateCode;
    QFile file = QFile.file;

    public CouponTemplateRepositoryImpl() {
        super(CouponTemplate.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<GetDetailCouponTemplateResponseDto> findDetailByTemplateNo(Long templateNo) {
        return Optional.of(from(couponTemplate)
                .where(couponTemplate.templateNo.eq(templateNo))
                .leftJoin(file).on(couponTemplate.templateNo.eq(file.couponTemplate.templateNo))
                .join(couponTemplate.couponPolicy, couponPolicy)
                .join(couponTemplate.couponType, couponType)
                .leftJoin(couponTemplate.product, product)
                .leftJoin(couponTemplate.category, category)
                .join(couponTemplate.couponStateCode, couponStateCode)
                .select(Projections.constructor(GetDetailCouponTemplateResponseDto.class,
                        couponTemplate.templateNo,
                        couponPolicy.policyFixed,
                        couponPolicy.policyPrice,
                        couponPolicy.policyMinimum,
                        couponPolicy.maxDiscount,
                        couponType.typeName,
                        product.title.as("productTitle"),
                        category.categoryName,
                        couponStateCode.codeTarget,
                        couponTemplate.templateName,
                        file.filePath.as(TEMPLATE_IMAGE),
                        couponTemplate.finishedAt,
                        couponTemplate.templateBundled
                ))
                .fetchOne());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<CouponTemplate> findDetailByTemplateName(String templateName) {
        return Optional.of(from(couponTemplate)
                .where(couponTemplate.templateName.eq(templateName))
                .select(couponTemplate)
                .fetchOne());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetDetailCouponTemplateResponseDto> findDetailAllBy(Pageable pageable) {
        JPQLQuery<Long> count = from(couponTemplate)
                .select(couponTemplate.count());

        List<GetDetailCouponTemplateResponseDto> content = from(couponTemplate)
                .leftJoin(file).on(couponTemplate.templateNo.eq(file.couponTemplate.templateNo))
                .join(couponTemplate.couponPolicy, couponPolicy)
                .join(couponTemplate.couponType, couponType)
                .leftJoin(couponTemplate.product, product)
                .leftJoin(couponTemplate.category, category)
                .join(couponTemplate.couponStateCode, couponStateCode)
                .select(Projections.constructor(GetDetailCouponTemplateResponseDto.class,
                        couponTemplate.templateNo,
                        couponPolicy.policyFixed,
                        couponPolicy.policyPrice,
                        couponPolicy.policyMinimum,
                        couponPolicy.maxDiscount,
                        couponType.typeName,
                        product.title.as("productTitle"),
                        category.categoryName,
                        couponStateCode.codeTarget,
                        couponTemplate.templateName,
                        file.filePath.as(TEMPLATE_IMAGE),
                        couponTemplate.finishedAt,
                        couponTemplate.templateBundled
                ))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetCouponTemplateResponseDto> findAllBy(Pageable pageable) {
        JPQLQuery<Long> count = from(couponTemplate)
                .select(couponTemplate.count());

        List<GetCouponTemplateResponseDto> content = from(couponTemplate)
                .leftJoin(file).on(couponTemplate.templateNo.eq(file.couponTemplate.templateNo))
                .select(Projections.constructor(GetCouponTemplateResponseDto.class,
                        couponTemplate.templateNo,
                        couponTemplate.templateName,
                        file.filePath.as(TEMPLATE_IMAGE),
                        couponTemplate.finishedAt))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
    }


}
