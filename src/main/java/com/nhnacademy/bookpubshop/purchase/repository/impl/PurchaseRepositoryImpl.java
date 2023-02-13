package com.nhnacademy.bookpubshop.purchase.repository.impl;

import static com.querydsl.jpa.JPAExpressions.select;

import com.nhnacademy.bookpubshop.product.entity.QProduct;
import com.nhnacademy.bookpubshop.purchase.dto.GetPurchaseListResponseDto;
import com.nhnacademy.bookpubshop.purchase.entity.Purchase;
import com.nhnacademy.bookpubshop.purchase.entity.QPurchase;
import com.nhnacademy.bookpubshop.purchase.repository.PurchaseRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import java.util.List;
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

    QPurchase purchase = QPurchase.purchase;
    QProduct product = QProduct.product;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetPurchaseListResponseDto> findByProductNumberWithPage(
            Long productNo, Pageable pageable) {

        JPQLQuery<GetPurchaseListResponseDto> query =
                from(purchase)
                        .join(product).on(product.productNo.eq(purchase.product.productNo))
                        .select(Projections.constructor(
                                GetPurchaseListResponseDto.class,
                                product.productNo,
                                product.title,
                                purchase.purchaseNo,
                                purchase.purchaseAmount,
                                purchase.purchasePrice,
                                purchase.createdAt))
                        .where(purchase.product.productNo.eq(productNo))
                        .orderBy(purchase.createdAt.desc())
                        .limit(pageable.getPageSize())
                        .offset(pageable.getOffset());

        JPQLQuery<Long> count = select(purchase.count())
                .join(product).on(product.productNo.eq(purchase.product.productNo));

        return PageableExecutionUtils.getPage(query.fetch(), pageable, count::fetchOne);
    }

    @Override
    public Page<GetPurchaseListResponseDto> getPurchaseListDesc(Pageable pageable) {
        List<GetPurchaseListResponseDto> content = from(purchase)
                .leftJoin(product)
                .on(product.productNo.eq(purchase.product.productNo))
                .select(Projections.fields(GetPurchaseListResponseDto.class,
                        product.productNo,
                        product.title.as("productTitle"),
                        purchase.purchaseNo,
                        purchase.purchaseAmount,
                        purchase.purchasePrice,
                        purchase.createdAt))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .orderBy(purchase.createdAt.desc())
                .fetch();

        JPQLQuery<Long> count = from(purchase)
                .leftJoin(product)
                .on(product.productNo.eq(purchase.product.productNo))
                .select(purchase.count());

        return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
    }
}
