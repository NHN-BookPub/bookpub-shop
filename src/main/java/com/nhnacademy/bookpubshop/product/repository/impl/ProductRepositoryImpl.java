package com.nhnacademy.bookpubshop.product.repository.impl;

import com.nhnacademy.bookpubshop.order.relationship.entity.QOrderProduct;
import com.nhnacademy.bookpubshop.product.dto.GetProductListForOrderResponseDto;
import com.nhnacademy.bookpubshop.product.dto.response.GetProductDetailResponseDto;
import com.nhnacademy.bookpubshop.product.dto.response.GetProductListResponseDto;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.entity.QProduct;
import com.nhnacademy.bookpubshop.product.relationship.entity.QProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.QProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.QProductTypeStateCode;
import com.nhnacademy.bookpubshop.product.repository.ProductRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

/**
 * 상품 레포지토리의 구현체입니다.
 *
 * @author : 여운석, 박경서
 * @since : 1.0
 **/

public class ProductRepositoryImpl extends QuerydslRepositorySupport
        implements ProductRepositoryCustom {
    private final EntityManager entityManager;
    QProduct product = QProduct.product;
    QOrderProduct orderProduct = QOrderProduct.orderProduct;


    public ProductRepositoryImpl(EntityManager entityManager) {
        super(Product.class);
        this.entityManager = entityManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetProductListResponseDto> getAllProducts(Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        JPAQuery<GetProductListResponseDto> query = queryFactory
                .from(product)
                .select(Projections.constructor(GetProductListResponseDto.class,
                        product.productNo,
                        product.title,
                        product.productStock,
                        product.salesPrice,
                        product.salesRate,
                        product.productPrice,
                        product.productDeleted))
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
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        JPAQuery<GetProductListResponseDto> query = queryFactory
                .from(product)
                .select(Projections.constructor(GetProductListResponseDto.class,
                        product.productNo,
                        product.title,
                        product.productStock,
                        product.salesPrice,
                        product.salesRate,
                        product.productPrice,
                        product.productDeleted))
                .where(product.title.like(title))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        Long count = queryFactory.select(product.count())
                .where(product.title.like(title))
                .from(product)
                .fetchOne();

        return new PageImpl<>(query.fetch(), pageable, count);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<GetProductDetailResponseDto> getProductDetailById(Long id) {
        QProductPolicy productPolicy = QProductPolicy.productPolicy;
        QProductSaleStateCode productSaleStateCode = QProductSaleStateCode.productSaleStateCode;
        QProductTypeStateCode productTypeStateCode = QProductTypeStateCode.productTypeStateCode;

        Optional<Product> content = Optional.ofNullable(from(product)
                .innerJoin(product.productPolicy, productPolicy)
                .innerJoin(product.productSaleStateCode, productSaleStateCode)
                .innerJoin(product.productTypeStateCode, productTypeStateCode)
                .select(product)
                .where(product.productNo.eq(id))
                .fetchOne());

        return content.map(GetProductDetailResponseDto::new);
    }

    @Override
    public List<GetProductListForOrderResponseDto> getProductListByOrderNo(Long orderNo) {
        return from(product)
                .select(Projections.constructor(
                        GetProductListForOrderResponseDto.class,
                        product.productNo,
                        product.title,
                        product.salesPrice,
                        orderProduct.productAmount))
                .join(orderProduct).on(orderProduct.product.productNo.eq(product.productNo))
                .where(orderProduct.order.orderNo.eq(orderNo))
                .fetch();
    }
}
