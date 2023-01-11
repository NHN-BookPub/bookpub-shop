package com.nhnacademy.bookpubshop.product.repository.impl;

import com.nhnacademy.bookpubshop.product.dto.GetProductListResponseDto;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.entity.QProduct;
import com.nhnacademy.bookpubshop.product.repository.ProductRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
    private final EntityManager entityManager;

    public ProductRepositoryImpl(EntityManager entityManager) {
        super(Product.class);
        this.entityManager = entityManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetProductListResponseDto> getAllProducts(Pageable pageable) {
        QProduct product = QProduct.product;

        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        JPAQuery<GetProductListResponseDto> query = queryFactory
                .from(product)
                .orderBy(product.publishDate.desc())
                .select(Projections.constructor(GetProductListResponseDto.class,
                        product.productNo,
                        product.productThumbnail,
                        product.title,
                        product.productStock,
                        product.salesPrice,
                        product.salesRate,
                        product.productDeleted,
                        product.publishDate))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        Long count = queryFactory.select(product.count()).from(product).fetchOne();

        return new PageImpl<>(query.fetch(), pageable, count);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetProductListResponseDto> getProductListLikeTitle(
            String title, Pageable pageable) {
        QProduct product = QProduct.product;

        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        JPAQuery<GetProductListResponseDto> query = queryFactory
                .from(product)
                .select(Projections.constructor(GetProductListResponseDto.class,
                        product.productNo,
                        product.productThumbnail,
                        product.title,
                        product.productStock,
                        product.salesPrice,
                        product.salesRate,
                        product.productDeleted,
                        product.publishDate))
                .where(product.title.like(title))
                .orderBy(product.publishDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        Long count = queryFactory.select(product.count())
                .where(product.title.like(title))
                .from(product)
                .fetchOne();

        return new PageImpl<>(query.fetch(), pageable, count);
    }
}
