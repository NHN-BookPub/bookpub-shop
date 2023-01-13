package com.nhnacademy.bookpubshop.purchase.repository.impl;

import static com.querydsl.jpa.JPAExpressions.select;
import com.nhnacademy.bookpubshop.product.entity.QProduct;
import com.nhnacademy.bookpubshop.purchase.dto.GetPurchaseListResponseDto;
import com.nhnacademy.bookpubshop.purchase.entity.Purchase;
import com.nhnacademy.bookpubshop.purchase.entity.QPurchase;
import com.nhnacademy.bookpubshop.purchase.repository.PurchaseRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;


/**
 * 매입 레포지토리의 구현체입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
public class PurchaseRepositoryImpl extends QuerydslRepositorySupport
        implements PurchaseRepositoryCustom {

    public PurchaseRepositoryImpl() {
        super(Purchase.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetPurchaseListResponseDto> findByProductNumberWithPage(
            Long productNo, Pageable pageable) {
        QPurchase purchase = QPurchase.purchase;
        QProduct product = QProduct.product;

        JPQLQuery<GetPurchaseListResponseDto> query =
                from(purchase)
                        .select(Projections.constructor(
                        GetPurchaseListResponseDto.class,
                        product.productNo,
                        purchase.purchaseNo,
                        purchase.purchaseAmount,
                        purchase.purchasePrice))
                        .innerJoin(product).on(product.productNo.eq(productNo))
                        .orderBy(purchase.createdAt.desc())
                        .limit(pageable.getPageSize())
                        .offset(pageable.getOffset());

        JPQLQuery<Long> count = select(purchase.count())
                .join(product).on(product.productNo.eq(purchase.product.productNo));

        return PageableExecutionUtils.getPage(query.fetch(), pageable, count::fetchOne);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetPurchaseListResponseDto> getPurchaseListDesc(Pageable pageable) {
        QPurchase purchase = QPurchase.purchase;
        QProduct product = QProduct.product;

        JPQLQuery<GetPurchaseListResponseDto> query = from(purchase)
                .join(product).on(product.productNo.eq(purchase.product.productNo))
                .select(Projections.constructor(
                        GetPurchaseListResponseDto.class,
                        product.productNo,
                        purchase.purchaseNo,
                        purchase.purchaseAmount,
                        purchase.purchasePrice))
                .orderBy(purchase.createdAt.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset());

        JPQLQuery<Long> count = select(purchase.count())
                .from(purchase);

        return PageableExecutionUtils.getPage(query.fetch(), pageable, count::fetchOne);
    }
}
