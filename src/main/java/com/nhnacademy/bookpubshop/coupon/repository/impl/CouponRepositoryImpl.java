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
import com.nhnacademy.bookpubshop.product.entity.QProduct;
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
 * CouponRepositoryCustom 구현체.
 *
 * @author : 정유진
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
                .select(Projections.constructor(GetCouponResponseDto.class,
                        coupon.couponNo,
                        member.memberId,
                        couponTemplate.templateName,
                        file.filePath.as("templateImage"),
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
    public Page<GetCouponResponseDto> findAllBy(Pageable pageable, String searchKey, String search) {

        JPQLQuery<Long> count = from(coupon).select(coupon.count());

        List<GetCouponResponseDto> content = from(coupon)
                .where(searchEq(searchKey, search))
                .leftJoin(file).on(coupon.couponTemplate.eq(file.couponTemplate))
                .join(coupon.couponTemplate, couponTemplate)
                .join(couponPolicy)
                .on(coupon.couponTemplate.couponPolicy.eq(couponPolicy))
                .join(coupon.member, member)
                .select(Projections.constructor(GetCouponResponseDto.class,
                        coupon.couponNo,
                        member.memberId,
                        couponTemplate.templateName,
                        file.filePath.as("templateImage"),
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

    @Override
    public List<GetOrderCouponResponseDto> findByProductNo(String memberId, List<Long> productNoList) {
        QProduct product = QProduct.product;
        QCategory category = QCategory.category;
        QCouponType couponType = QCouponType.couponType;

//        List<GetOrderCouponResponseDto> result =
//                from(coupon)
//                        .join(coupon.couponTemplate.product, product)
//                        .on(coupon.couponTemplate.product.productNo.eq(product.productNo))
//                        .join(coupon.member, member)
//                        .join(coupon.couponTemplate.couponType, couponType)
//                        .on(coupon.couponTemplate.couponType.typeNo.eq(couponType.typeNo))
//                        .join(coupon.couponTemplate.category, category)
//                        .on(coupon.couponTemplate.category.in(
//                                JPAExpressions.select(category)
//                                                .from(product)
//                                        .in()
//                                        .fetch())
//                        ))
//                        .where(member.memberId.eq(memberId))
//                //수정, enum타입
//                        //.where(couponType.typeNo.in(1, 2))
//                        .where(couponTemplate.finishedAt.before(LocalDateTime.now()))
//                        .where(product.productNo.in(productNoList))
//                        .select(Projections.constructor(GetOrderCouponResponseDto.class,
//                                coupon.couponNo,
//                                couponTemplate.templateName,
//                                couponPolicy.policyFixed,
//                                couponPolicy.policyPrice,
//                                couponPolicy.policyMinimum,
//                                couponPolicy.maxDiscount,
//                                coupon.couponTemplate.templateBundled))
//                        .fetch();

        //return result;

        return null;
    }

    /**
     * 검색 조건에 따라 쿼리문을 다르게 주기위한 메소드.
     *
     * @param searchKey 검색 조건
     * @param search    검색어
     * @return 조건에 맞는 querydsl
     */
    private BooleanExpression searchEq(String searchKey, String search) {
        if (Objects.isNull(searchKey) || Objects.isNull(search)) return null;

        if (searchKey.equals("memberId"))
            return coupon.member.memberId.eq(search);
        if (searchKey.equals("templateName"))
            return coupon.couponTemplate.templateName.eq(search);
        if (searchKey.equals("couponNo"))
            return coupon.couponNo.eq(Long.parseLong(search));
        else return null;
    }
}
