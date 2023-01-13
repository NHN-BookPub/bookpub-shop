package com.nhnacademy.bookpubshop.purchase.repository.impl;

import com.nhnacademy.bookpubshop.product.entity.QProduct;
import com.nhnacademy.bookpubshop.purchase.dto.GetPurchaseListResponseDto;
import com.nhnacademy.bookpubshop.purchase.entity.Purchase;
import com.nhnacademy.bookpubshop.purchase.entity.QPurchase;
import com.nhnacademy.bookpubshop.purchase.repository.PurchaseRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;


/**
 * 매입 레포지토리의 구현체입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
public class PurchaseRepositoryImpl extends QuerydslRepositorySupport
        implements PurchaseRepositoryCustom {
    private final EntityManager entityManager;

    public PurchaseRepositoryImpl(EntityManager entityManager) {
        super(Purchase.class);
        this.entityManager = entityManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetPurchaseListResponseDto> findByProductNumberWithPage(
            Long productNo, Pageable pageable) {
        QPurchase purchase = QPurchase.purchase;
        QProduct product = QProduct.product;

        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        JPAQuery<GetPurchaseListResponseDto> query = queryFactory
                .from(purchase)
                .join(purchase).on(product.productNo.eq(purchase.product.productNo))
                .where(product.productNo.eq(productNo))
                .select(Projections.constructor(
                        GetPurchaseListResponseDto.class,
                        product.productNo,
                        purchase.purchaseNo,
                        purchase.purchaseAmount,
                        purchase.purchasePrice))
                .orderBy(purchase.createdAt.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset());

        Long count = queryFactory
                .from(purchase)
                .join(product).on(product.productNo.eq(purchase.product.productNo))
                .select(purchase.count())
                .fetchOne();

        return new PageImpl<>(query.fetch(), pageable, count);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetPurchaseListResponseDto> getPurchaseListDesc(Pageable pageable) {
        QPurchase purchase = QPurchase.purchase;
        QProduct product = QProduct.product;

        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        JPAQuery<GetPurchaseListResponseDto> query = queryFactory
                .from(purchase)
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

        Long count = queryFactory
                .select(purchase.count())
                .from(purchase)
                .fetchOne();

        return new PageImpl<>(query.fetch(), pageable, count);
    }
}
