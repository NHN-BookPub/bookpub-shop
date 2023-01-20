package com.nhnacademy.bookpubshop.product.repository.impl;

import com.nhnacademy.bookpubshop.category.entity.QCategory;
import com.nhnacademy.bookpubshop.product.dto.response.GetProductByTypeResponseDto;
import com.nhnacademy.bookpubshop.product.dto.response.GetProductDetailResponseDto;
import com.nhnacademy.bookpubshop.product.dto.response.GetProductListResponseDto;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.entity.QProduct;
import com.nhnacademy.bookpubshop.product.relationship.entity.QProductCategory;
import com.nhnacademy.bookpubshop.product.repository.ProductRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
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
        QProduct product = QProduct.product;

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
        QProduct product = QProduct.product;

        Optional<Product> content = Optional.ofNullable(from(product)
                .where(product.productNo.eq(id))
                .leftJoin(product.productPolicy)
                .leftJoin(product.productSaleStateCode)
                .leftJoin(product.productTypeStateCode)
                .leftJoin(product.productAuthors)
                .leftJoin(product.productTags)
                .select(product)
                .fetchJoin()
                .fetchOne());

        return content.map(GetProductDetailResponseDto::new);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetProductByTypeResponseDto> findProductListByType(Integer typeNo, Integer limit) {
        QProduct product = QProduct.product;
        QProductCategory productCategory = QProductCategory.productCategory;
        QCategory category = QCategory.category;

        List<GetProductByTypeResponseDto> result = from(product)
                .leftJoin(productCategory)
                .on(productCategory.product.productNo.eq(product.productNo))
                .leftJoin(category)
                .on(category.categoryNo.eq(productCategory.category.categoryNo))
                .select(Projections.fields(GetProductByTypeResponseDto.class,
                        product.productNo,
                        product.title,
                        product.salesPrice,
                        product.productPrice,
                        product.salesRate))
                .where(product.productTypeStateCode.codeNo.eq(typeNo))
                .where(product.productSaleStateCode.codeCategory.eq("판매중"))
                .distinct()
                .orderBy(NumberExpression.random().asc())
                .limit(limit)
                .fetch();

        result.stream()
                .map(m -> m.getProductCategories().add(
                        String.valueOf(
                                from(category)
                                        .leftJoin(productCategory)
                                        .on(productCategory.category.categoryNo.eq(category.categoryNo))
                                        .select(category.categoryName)
                                        .where(productCategory.product.productNo.eq(m.getProductNo())).fetch())
                )).collect(Collectors.toList());

        return result;
    }
}
