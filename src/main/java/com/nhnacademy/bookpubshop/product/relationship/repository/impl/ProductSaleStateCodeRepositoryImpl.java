package com.nhnacademy.bookpubshop.product.relationship.repository.impl;

import com.nhnacademy.bookpubshop.product.relationship.dto.GetProductSaleStateCodeResponseDto;
import com.nhnacademy.bookpubshop.product.relationship.entity.QProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.repository.ProductSaleStateCodeRepository;
import com.nhnacademy.bookpubshop.product.relationship.repository.ProductSaleStateCodeRepositoryCustom;
import com.querydsl.core.types.Projections;
import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

/**
 * 상품유형코드 커스텀 레포지토리 구현체.
 *
 * @author : 박경서
 * @since : 1.0
 **/
public class ProductSaleStateCodeRepositoryImpl
        extends QuerydslRepositorySupport implements ProductSaleStateCodeRepositoryCustom {

    public ProductSaleStateCodeRepositoryImpl() {
        super(ProductSaleStateCodeRepository.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetProductSaleStateCodeResponseDto> findByAllUsed() {
        QProductSaleStateCode productSaleStateCode = QProductSaleStateCode.productSaleStateCode;

        return from(productSaleStateCode)
                .where(productSaleStateCode.codeUsed.isTrue())
                .select(Projections.constructor(GetProductSaleStateCodeResponseDto.class,
                        productSaleStateCode.codeNo,
                        productSaleStateCode.codeCategory,
                        productSaleStateCode.codeUsed,
                        productSaleStateCode.codeInfo))
                .fetch();
    }
}
