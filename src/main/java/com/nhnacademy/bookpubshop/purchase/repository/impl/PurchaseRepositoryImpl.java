package com.nhnacademy.bookpubshop.purchase.repository.impl;

import com.nhnacademy.bookpubshop.product.entity.QProduct;
import com.nhnacademy.bookpubshop.purchase.dto.GetPurchaseResponseDto;
import com.nhnacademy.bookpubshop.purchase.entity.Purchase;
import com.nhnacademy.bookpubshop.purchase.entity.QPurchase;
import com.nhnacademy.bookpubshop.purchase.repository.PurchaseRepositoryCustom;
import com.querydsl.core.types.Projections;
import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

/**
 * 매입 레포지토리의 구현체입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
public class PurchaseRepositoryImpl extends QuerydslRepositorySupport implements PurchaseRepositoryCustom {
    public PurchaseRepositoryImpl() {
        super(Purchase.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetPurchaseResponseDto> findByProductNumber(Long number) {
        QPurchase purchase = QPurchase.purchase;
        QProduct product = QProduct.product;


        return from(purchase)
                .innerJoin(product, purchase.product)
                .where(product.productNo.eq(number))
                .select(Projections.constructor(
                        GetPurchaseResponseDto.class,
                        purchase.purchaseNo,
                        purchase.product.productNo,
                        purchase.purchasePrice,
                        purchase.createdAt,
                        purchase.purchaseAmount))
                .fetch();
    }
}
