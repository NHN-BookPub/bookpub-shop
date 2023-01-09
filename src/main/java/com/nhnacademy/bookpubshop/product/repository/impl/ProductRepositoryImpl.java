package com.nhnacademy.bookpubshop.product.repository.impl;

import com.nhnacademy.bookpubshop.product.dto.GetProductListResponseDto;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.entity.QProduct;
import com.nhnacademy.bookpubshop.product.repository.ProductRepositoryCustom;
import com.querydsl.core.types.Projections;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

/**
 * 상품 레포지토리의 구현체입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
public class ProductRepositoryImpl extends QuerydslRepositorySupport
        implements ProductRepositoryCustom {

    public ProductRepositoryImpl() {
        super(Product.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetProductListResponseDto> getAllProducts(Pageable pageable) {
        QProduct product = QProduct.product;

        return from(product)
                .orderBy(product.createdAt.asc())
                .select(Projections.constructor(GetProductListResponseDto.class,
                        product.productNo,
                        product.productThumbnail,
                        product.title,
                        product.productStock,
                        product.salesPrice,
                        product.salesRate,
                        product.createdAt))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetProductListResponseDto> getProductListLikeTitle(
            String title, Pageable pageable) {
        QProduct product = QProduct.product;

        return from(product)
                .select(Projections.constructor(GetProductListResponseDto.class,
                        product.productNo,
                        product.productThumbnail,
                        product.title,
                        product.productStock,
                        product.salesPrice,
                        product.salesRate,
                        product.createdAt))
                .where(product.title.like(title))
                .orderBy(product.createdAt.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
}
