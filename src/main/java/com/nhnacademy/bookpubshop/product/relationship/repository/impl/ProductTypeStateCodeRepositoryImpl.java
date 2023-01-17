package com.nhnacademy.bookpubshop.product.relationship.repository.impl;

import com.nhnacademy.bookpubshop.product.relationship.dto.GetProductTypeStateCodeResponseDto;
import com.nhnacademy.bookpubshop.product.relationship.entity.QProductTypeStateCode;
import com.nhnacademy.bookpubshop.product.relationship.repository.ProductTypeStateCodeRepository;
import com.nhnacademy.bookpubshop.product.relationship.repository.ProductTypeStateCodeRepositoryCustom;
import com.querydsl.core.types.Projections;
import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

/**
 * 상품 유형 코드 커스텀 레포지토리 구현체.
 *
 * @author : 박경서
 * @since : 1.0
 **/
public class ProductTypeStateCodeRepositoryImpl
        extends QuerydslRepositorySupport implements ProductTypeStateCodeRepositoryCustom {

    public ProductTypeStateCodeRepositoryImpl() {
        super(ProductTypeStateCodeRepository.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetProductTypeStateCodeResponseDto> findByAllUsed() {
        QProductTypeStateCode productTypeStateCode = QProductTypeStateCode.productTypeStateCode;

        return from(productTypeStateCode)
                .where(productTypeStateCode.codeUsed.isTrue())
                .select(Projections.constructor(GetProductTypeStateCodeResponseDto.class,
                        productTypeStateCode.codeNo,
                        productTypeStateCode.codeName,
                        productTypeStateCode.codeUsed,
                        productTypeStateCode.codeInfo))
                .fetch();
    }
}
