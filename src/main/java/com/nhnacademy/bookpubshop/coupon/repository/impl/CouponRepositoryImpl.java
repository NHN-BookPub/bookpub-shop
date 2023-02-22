package com.nhnacademy.bookpubshop.coupon.repository.impl;

import com.nhnacademy.bookpubshop.category.entity.QCategory;
import com.nhnacademy.bookpubshop.coupon.dto.response.GetCouponResponseDto;
import com.nhnacademy.bookpubshop.coupon.dto.response.GetOrderCouponResponseDto;
import com.nhnacademy.bookpubshop.coupon.entity.Coupon;
import com.nhnacademy.bookpubshop.coupon.entity.QCoupon;
import com.nhnacademy.bookpubshop.coupon.repository.CouponRepositoryCustom;
import com.nhnacademy.bookpubshop.couponpolicy.entity.QCouponPolicy;
import com.nhnacademy.bookpubshop.coupontemplate.entity.QCouponTemplate;
import com.nhnacademy.bookpubshop.coupontype.entity.QCouponType;
import com.nhnacademy.bookpubshop.file.entity.QFile;
import com.nhnacademy.bookpubshop.member.entity.QMember;
import com.nhnacademy.bookpubshop.order.entity.QBookpubOrder;
import com.nhnacademy.bookpubshop.order.relationship.entity.QOrderProduct;
import com.nhnacademy.bookpubshop.product.entity.QProduct;
import com.nhnacademy.bookpubshop.product.relationship.entity.QProductCategory;
import com.nhnacademy.bookpubshop.state.CouponState;
import com.nhnacademy.bookpubshop.state.CouponType;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;

/**
 * CouponRepositoryCustom 구현체.
 *
 * @author : 정유진, 김서현
 * @since : 1.0
 **/
public class CouponRepositoryImpl extends QuerydslRepositorySupport
        implements CouponRepositoryCustom {
    public CouponRepositoryImpl() {
        super(Coupon.class);
    }

    QCoupon coupon = QCoupon.coupon;
    QCouponTemplate couponTemplate = QCouponTemplate.couponTemplate;
    QCouponPolicy couponPolicy = QCouponPolicy.couponPolicy;
    QMember member = QMember.member;
    QFile file = QFile.file;
    QProduct product = QProduct.product;
    QCategory category = QCategory.category;
    QProductCategory productCategory = QProductCategory.productCategory;
    QCouponType couponType = QCouponType.couponType;
    QBookpubOrder order = QBookpubOrder.bookpubOrder;
    QOrderProduct orderProduct = QOrderProduct.orderProduct;

    private static final String TEMPLATE_IMAGE  = "templateImage";

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<GetCouponResponseDto> findByCouponNo(Long couponNo) {

        return Optional.of(from(coupon)
                .where(coupon.couponNo.eq(couponNo))
                .leftJoin(file).on(coupon.couponTemplate.eq(file.couponTemplate))
                .innerJoin(coupon.couponTemplate, couponTemplate)
                .innerJoin(couponPolicy)
                .on(coupon.couponTemplate.couponPolicy.policyNo
                        .eq(couponTemplate.couponPolicy.policyNo))
                .innerJoin(coupon.member, member)
                .innerJoin(couponType)
                .on(coupon.couponTemplate.couponType.typeNo.eq(
                        couponType.typeNo))
                .select(Projections.constructor(GetCouponResponseDto.class,
                        coupon.couponNo,
                        member.memberId,
                        couponTemplate.templateName,
                        file.filePath.as(TEMPLATE_IMAGE),
                        couponType.typeName,
                        couponPolicy.policyFixed,
                        couponPolicy.policyPrice,
                        couponPolicy.policyMinimum,
                        couponPolicy.maxDiscount,
                        couponTemplate.finishedAt,
                        coupon.couponUsed))
                .fetchOne());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetCouponResponseDto> findAllBy(
            Pageable pageable, String searchKey, String search) {

        JPQLQuery<Long> count = from(coupon).select(coupon.count());

        List<GetCouponResponseDto> content = from(coupon)
                .where(searchEq(searchKey, search))
                .leftJoin(file).on(coupon.couponTemplate.eq(file.couponTemplate))
                .join(coupon.couponTemplate, couponTemplate)
                .join(couponPolicy)
                .on(coupon.couponTemplate.couponPolicy.eq(couponPolicy))
                .join(coupon.member, member)
                .innerJoin(couponType)
                .on(coupon.couponTemplate.couponType.typeNo.eq(
                        couponType.typeNo))
                .select(Projections.constructor(GetCouponResponseDto.class,
                        coupon.couponNo,
                        member.memberId,
                        couponTemplate.templateName,
                        file.filePath.as(TEMPLATE_IMAGE),
                        couponType.typeName,
                        couponPolicy.policyFixed,
                        couponPolicy.policyPrice,
                        couponPolicy.policyMinimum,
                        couponPolicy.maxDiscount,
                        couponTemplate.finishedAt,
                        coupon.couponUsed))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetOrderCouponResponseDto> findByProductNo(Long memberNo, Long productNo) {

        return from(coupon)
                .join(coupon.couponTemplate, couponTemplate)
                .leftJoin(product)
                .on(coupon.couponTemplate.product.eq(product))
                .join(couponType)
                .on(coupon.couponTemplate.couponType.eq(couponType))
                .join(couponPolicy)
                .on(coupon.couponTemplate.couponPolicy.eq(couponPolicy))
                .leftJoin(category)
                .on(coupon.couponTemplate.category.eq(category))
                .where(coupon.member.memberNo.eq(memberNo)
                        .and(coupon.couponUsed.isFalse())
                        .and(couponType.typeName
                                .in(CouponType.COMMON.getName(), CouponType.DUPLICATE.getName()))
                        .and(coupon.couponTemplate.finishedAt
                                .coalesce(LocalDateTime.now().plusDays(1))
                                .after(LocalDateTime.now()))
                        .and((product.productNo.eq(productNo))
                                .or(coupon.couponTemplate.category.categoryNo
                                        .in(JPAExpressions.select(category.categoryNo)
                                                .from(category)
                                                .join(productCategory)
                                                .on(category.categoryNo
                                                        .eq(productCategory.category.categoryNo))
                                                .where(productCategory.product.productNo
                                                        .eq(productNo))))
                                .or(coupon.couponTemplate.couponStateCode.codeTarget
                                        .eq(CouponState.COUPON_ALL.getName()))
                        )
                )
                .select(Projections.constructor(GetOrderCouponResponseDto.class,
                        coupon.couponNo,
                        coupon.couponTemplate.templateName,
                        coupon.couponTemplate.product.productNo,
                        coupon.couponTemplate.category.categoryNo,
                        couponPolicy.policyFixed,
                        couponPolicy.policyPrice,
                        couponPolicy.policyMinimum,
                        couponPolicy.maxDiscount.coalesce((long) Integer.MAX_VALUE),
                        coupon.couponTemplate.templateBundled))
                .fetch();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsMonthCoupon(Long memberNo, Long templateNo) {
        Long monthCoupon = from(coupon)
                .select(coupon.couponNo)
                .where(coupon.member.memberNo.eq(memberNo)
                        .and(coupon.couponTemplate.templateNo.eq(templateNo))).fetchOne();

        return !Objects.isNull(monthCoupon);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Long> existsMonthCouponList(Long memberNo, List<Long> couponList) {

        return from(coupon)
                .select(coupon.couponTemplate.templateNo)
                .where(coupon.member.memberNo.eq(memberNo)
                        .and(coupon.couponTemplate.templateNo.in(couponList)))
                .fetch();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Coupon> findByCouponByOrderNo(Long orderNo) {
        return from(coupon)
                .innerJoin(coupon.order, order)
                .select(coupon)
                .where(coupon.couponUsed.isTrue()
                        .and(coupon.order.orderNo.eq(orderNo)))
                .fetch();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Coupon> findByCouponByOrderProductNo(Long orderProductNo) {
        return from(coupon)
                .innerJoin(coupon.orderProduct, orderProduct)
                .select(coupon)
                .where(coupon.couponUsed.isTrue()
                        .and(coupon.orderProduct.orderProductNo.eq(orderProductNo)))
                .fetch();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetCouponResponseDto> findPositiveCouponByMemberNo(Pageable pageable,
                                                                   Long memberNo) {
        JPQLQuery<Long> count = from(coupon).select(coupon.count())
                .where(coupon.member.memberNo.eq(memberNo)
                        .and(coupon.couponUsed.isFalse())
                        .and((coupon.couponTemplate.finishedAt.after(LocalDateTime.now()))
                                .or(coupon.couponTemplate.finishedAt.isNull())))
                .leftJoin(file).on(coupon.couponTemplate.eq(file.couponTemplate))
                .innerJoin(coupon.couponTemplate, couponTemplate)
                .innerJoin(coupon.couponTemplate.couponPolicy, couponPolicy)
                .on(coupon.couponTemplate.couponPolicy.policyNo
                        .eq(couponTemplate.couponPolicy.policyNo))
                .innerJoin(coupon.member, member)
                .innerJoin(couponType)
                .on(coupon.couponTemplate.couponType.typeNo.eq(
                        couponType.typeNo));

        List<GetCouponResponseDto> positiveCouponList = from(coupon)
                .where(coupon.member.memberNo.eq(memberNo)
                        .and(coupon.couponUsed.isFalse())
                        .and((coupon.couponTemplate.finishedAt.after(LocalDateTime.now()))
                                .or(coupon.couponTemplate.finishedAt.isNull())))
                .leftJoin(file).on(coupon.couponTemplate.eq(file.couponTemplate))
                .innerJoin(coupon.couponTemplate, couponTemplate)
                .innerJoin(coupon.couponTemplate.couponPolicy, couponPolicy)
                .on(coupon.couponTemplate.couponPolicy.policyNo
                        .eq(couponTemplate.couponPolicy.policyNo))
                .innerJoin(coupon.member, member)
                .innerJoin(couponType)
                .on(coupon.couponTemplate.couponType.typeNo.eq(
                        couponType.typeNo))
                .select(Projections.constructor(GetCouponResponseDto.class,
                        coupon.couponNo,
                        member.memberId,
                        couponTemplate.templateName,
                        file.filePath.as(TEMPLATE_IMAGE),
                        couponType.typeName,
                        couponPolicy.policyFixed,
                        couponPolicy.policyPrice,
                        couponPolicy.policyMinimum,
                        couponPolicy.maxDiscount,
                        couponTemplate.finishedAt,
                        coupon.couponUsed))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(positiveCouponList, pageable, count::fetchOne);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetCouponResponseDto> findNegativeCouponByMemberNo(Pageable pageable,
            Long memberNo) {

        JPQLQuery<Long> count = from(coupon).select(coupon.count())
                .where(coupon.member.memberNo.eq(memberNo)
                        .and(coupon.couponUsed.isTrue()
                                .or(coupon.couponTemplate.finishedAt.before(LocalDateTime.now()))))
                .leftJoin(file).on(coupon.couponTemplate.eq(file.couponTemplate))
                .innerJoin(coupon.couponTemplate, couponTemplate)
                .innerJoin(coupon.couponTemplate.couponPolicy, couponPolicy)
                .on(coupon.couponTemplate.couponPolicy.policyNo
                        .eq(couponTemplate.couponPolicy.policyNo))
                .innerJoin(coupon.member, member)
                .innerJoin(couponType)
                .on(coupon.couponTemplate.couponType.typeNo.eq(couponType.typeNo));

        List<GetCouponResponseDto> negativeCoupon = from(coupon)
                .where(coupon.member.memberNo.eq(memberNo)
                        .and(coupon.couponUsed.isTrue()
                                .or(coupon.couponTemplate.finishedAt.before(LocalDateTime.now()))))
                .leftJoin(file).on(coupon.couponTemplate.eq(file.couponTemplate))
                .innerJoin(coupon.couponTemplate, couponTemplate)
                .innerJoin(coupon.couponTemplate.couponPolicy, couponPolicy)
                .on(coupon.couponTemplate.couponPolicy.policyNo
                        .eq(couponTemplate.couponPolicy.policyNo))
                .innerJoin(coupon.member, member)
                .innerJoin(couponType)
                .on(coupon.couponTemplate.couponType.typeNo.eq(couponType.typeNo))
                .select(Projections.constructor(GetCouponResponseDto.class,
                        coupon.couponNo,
                        member.memberId,
                        couponTemplate.templateName,
                        file.filePath.as(TEMPLATE_IMAGE),
                        couponType.typeName,
                        couponPolicy.policyFixed,
                        couponPolicy.policyPrice,
                        couponPolicy.policyMinimum,
                        couponPolicy.maxDiscount,
                        couponTemplate.finishedAt,
                        coupon.couponUsed))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(negativeCoupon, pageable, count::fetchOne);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsTierCouponsByMemberNo(Long memberNo, List<Long> tierCoupons) {
        List<Long> couponList = from(coupon)
                .select(coupon.couponNo)
                .where(coupon.member.memberNo.eq(memberNo)
                        .and(coupon.couponTemplate.templateNo.in(tierCoupons))).fetch();

        return !couponList.isEmpty();
    }


    /**
     * 검색 조건에 따라 쿼리문을 다르게 주기위한 메소드.
     *
     * @param searchKey 검색 조건
     * @param search    검색어
     * @return 조건에 맞는 querydsl
     */
    private BooleanExpression searchEq(String searchKey, String search) {
        if (Objects.isNull(searchKey) || Objects.isNull(search)) {
            return null;
        }

        if (searchKey.equals("memberId")) {
            return coupon.member.memberId.eq(search);
        }
        if (searchKey.equals("templateName")) {
            return coupon.couponTemplate.templateName.eq(search);
        }
        if (searchKey.equals("couponNo")) {
            return coupon.couponNo.eq(Long.parseLong(search));
        } else {
            return null;
        }
    }
}
