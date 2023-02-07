package com.nhnacademy.bookpubshop.product.repository.impl;

import com.nhnacademy.bookpubshop.author.entity.QAuthor;
import com.nhnacademy.bookpubshop.category.entity.QCategory;
import com.nhnacademy.bookpubshop.file.entity.QFile;
import com.nhnacademy.bookpubshop.order.entity.QBookpubOrder;
import com.nhnacademy.bookpubshop.order.relationship.entity.QOrderProduct;
import com.nhnacademy.bookpubshop.order.relationship.entity.QOrderProductStateCode;
import com.nhnacademy.bookpubshop.product.dto.response.GetProductByCategoryResponseDto;
import com.nhnacademy.bookpubshop.product.dto.response.GetProductByTypeResponseDto;
import com.nhnacademy.bookpubshop.product.dto.response.GetProductDetailResponseDto;
import com.nhnacademy.bookpubshop.product.dto.response.GetProductListForOrderResponseDto;
import com.nhnacademy.bookpubshop.product.dto.response.GetProductListResponseDto;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.entity.QProduct;
import com.nhnacademy.bookpubshop.product.relationship.entity.QProductAuthor;
import com.nhnacademy.bookpubshop.product.relationship.entity.QProductCategory;
import com.nhnacademy.bookpubshop.product.relationship.entity.QProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.repository.ProductRepositoryCustom;
import com.nhnacademy.bookpubshop.state.FileCategory;
import com.nhnacademy.bookpubshop.state.ProductSaleState;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPQLQuery;
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
import org.springframework.data.support.PageableExecutionUtils;

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
    QOrderProductStateCode orderProductStateCode = QOrderProductStateCode.orderProductStateCode;
    QBookpubOrder order = QBookpubOrder.bookpubOrder;
    QProductCategory productCategory = QProductCategory.productCategory;
    QCategory category = QCategory.category;
    QAuthor author = QAuthor.author;
    QProductAuthor productAuthor = QProductAuthor.productAuthor;
    QProductSaleStateCode productSaleStateCode = QProductSaleStateCode.productSaleStateCode;
    QFile file = QFile.file;


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
                        file.filePath,
                        product.productStock,
                        product.salesPrice,
                        product.salesRate,
                        product.productPrice,
                        product.productDeleted))
                .leftJoin(product.files, file)
                .on(file.fileCategory.eq(FileCategory.PRODUCT_THUMBNAIL.getCategory()))
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

        List<GetProductListResponseDto> content = queryFactory
                .from(product)
                .select(Projections.constructor(GetProductListResponseDto.class,
                        product.productNo,
                        product.title,
                        file.filePath,
                        product.productStock,
                        product.salesPrice,
                        product.salesRate,
                        product.productPrice,
                        product.productDeleted))
                .leftJoin(product.files, file)
                .on(file.fileCategory.eq(FileCategory.PRODUCT_THUMBNAIL.getCategory()))
                .where(product.title.like(title))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPQLQuery<Long> count = from(product)
                .select(product.count())
                .where(product.title.like(title));

        return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<GetProductDetailResponseDto> getProductDetailById(Long id) {

        Optional<Product> content = Optional.ofNullable(from(product)
                .where(product.productNo.eq(id))
                .leftJoin(product.productPolicy)
                .leftJoin(product.productSaleStateCode)
                .leftJoin(product.productTypeStateCode)
                .leftJoin(product.productAuthors)
                .leftJoin(product.productTags)
                .leftJoin(file).on(product.productNo.eq(file.product.productNo))
                .select(product)
                .where(product.productNo.eq(id))
                .fetchOne());

        return content.map(GetProductDetailResponseDto::new);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetProductByTypeResponseDto> findProductListByType(Integer typeNo, Integer limit) {

        List<GetProductByTypeResponseDto> result = from(product)
                .leftJoin(productCategory)
                .on(productCategory.product.productNo.eq(product.productNo))
                .leftJoin(category)
                .on(category.categoryNo.eq(productCategory.category.categoryNo))
                .leftJoin(file).on(product.productNo.eq(file.product.productNo)
                        .and(file.fileCategory.eq(FileCategory.PRODUCT_THUMBNAIL.getCategory())))
                .select(Projections.fields(GetProductByTypeResponseDto.class,
                        product.productNo,
                        product.title,
                        file.filePath.as(FileCategory.PRODUCT_THUMBNAIL.getCategory()),
                        product.salesPrice,
                        product.productPrice,
                        product.salesRate))
                .where(product.productTypeStateCode.codeNo.eq(typeNo))
                .where(product.productSaleStateCode.codeCategory.eq("판매중"))
                .distinct()
                .orderBy(Expressions.numberTemplate(Double.class, "function('rand')").asc())
                .limit(limit)
                .fetch();

        // TODO: 쿼리 수정 (Check)
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

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetProductListForOrderResponseDto> getProductListByOrderNo(Long orderNo) {
        return from(orderProduct)
                .select(Projections.constructor(
                        GetProductListForOrderResponseDto.class,
                        product.productNo,
                        product.title,
                        file.filePath,
                        product.salesPrice,
                        orderProduct.productAmount,
                        orderProductStateCode.codeName))
                .innerJoin(orderProduct.product, product)
                .innerJoin(orderProduct.orderProductStateCode, orderProductStateCode)
                .innerJoin(orderProduct.order, order)
                .leftJoin(file).on(product.productNo.eq(file.product.productNo)
                        .and(file.fileCategory.eq(FileCategory.PRODUCT_THUMBNAIL.getCategory())))
                .on(file.fileCategory.eq(FileCategory.PRODUCT_THUMBNAIL.getCategory()))
                .where(order.orderNo.eq(orderNo))
                .fetch();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetProductDetailResponseDto> getProductsInCart(List<Long> productsNo) {
        return from(product)
                .select(Projections.fields(GetProductDetailResponseDto.class,
                        product.productNo,
                        product.title,
                        file.filePath,
                        product.productPublisher,
                        product.productPrice,
                        product.salesPrice))
                .leftJoin(file).on(product.productNo.eq(file.product.productNo)
                        .and(file.fileCategory.eq(FileCategory.PRODUCT_THUMBNAIL.getCategory())))
                .where(product.productNo.in(productsNo))
                .fetch();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetProductByCategoryResponseDto> getProductsByCategory(Integer categoryNo, Pageable pageable) {

        List<GetProductByCategoryResponseDto> content =
                from(product)
                        .select(Projections.fields(GetProductByCategoryResponseDto.class,
                                product.productNo,
                                product.title,
                                file.filePath.as(FileCategory.PRODUCT_THUMBNAIL.getCategory()),
                                product.salesPrice,
                                product.salesRate))
                        .innerJoin(product.productSaleStateCode, productSaleStateCode)
                        .on(productSaleStateCode.codeCategory.eq(ProductSaleState.SALE.getName()))

                        .innerJoin(product.productCategories, productCategory)
                        .on(productCategory.product.productNo.eq(product.productNo))

                        .leftJoin(file).on(product.productNo.eq(file.product.productNo)
                                .and(file.fileCategory.eq(FileCategory.PRODUCT_THUMBNAIL.getCategory())))

                        .orderBy(product.productPriority.asc())
                        .where(productCategory.category.categoryNo.eq(categoryNo))
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();

        setCategoryAndAuthors(content);

        JPQLQuery<Long> count = from(product)
                .leftJoin(product.productCategories)
                .leftJoin(product.productAuthors)
                .leftJoin(product.productTags)
                .leftJoin(product.productSaleStateCode)
                .select(product.count())
                .where(category.categoryNo.eq(categoryNo))
                .where(product.productSaleStateCode.codeCategory.eq(ProductSaleState.SALE.getName()));

        return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetProductByCategoryResponseDto> getEbooks(Pageable pageable) {
        List<GetProductByCategoryResponseDto> content =
                from(product)
                        .select(Projections.fields(GetProductByCategoryResponseDto.class,
                                product.productNo,
                                product.title,
                                product.salesPrice,
                                product.salesRate))
                        .innerJoin(product.productSaleStateCode, productSaleStateCode)
                        .on(productSaleStateCode.codeCategory.eq(ProductSaleState.SALE.getName()))

                        .innerJoin(product.productCategories, productCategory)
                        .on(productCategory.product.productNo.eq(product.productNo))

                        .innerJoin(file).on(product.productNo.eq(file.product.productNo)
                                .and(file.fileCategory.eq(FileCategory.PRODUCT_EBOOK.getCategory())))

                        .orderBy(product.productPriority.asc())
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();

        setCategoryAndAuthors(content);

        for (GetProductByCategoryResponseDto getProductByCategoryResponseDto : content) {
            getProductByCategoryResponseDto.setThumbnail(
                    from(file)
                            .innerJoin(product)
                            .on(file.product.productNo.eq(product.productNo))
                            .select(file.filePath)
                            .where(file.fileCategory.eq(FileCategory.PRODUCT_THUMBNAIL.getCategory())
                                    .and(file.product.productNo.eq(getProductByCategoryResponseDto.getProductNo())))
                            .fetchOne());
        }

        JPQLQuery<Long> count = from(product)
                .leftJoin(product.productCategories)
                .leftJoin(product.productAuthors)
                .leftJoin(product.productTags)
                .leftJoin(product.productSaleStateCode)
                .innerJoin(file).on(product.productNo.eq(file.product.productNo))
                .select(product.count())
                .where(file.fileCategory.eq(FileCategory.PRODUCT_EBOOK.getCategory()))
                .where(product.productSaleStateCode.codeCategory.eq(ProductSaleState.SALE.getName()));

        return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
    }

    /**
     * 상품별 카테고리와 저자들을 넣어주는 메소드입니다.
     *
     * @param content 상품리스트.
     */
    private void setCategoryAndAuthors(List<GetProductByCategoryResponseDto> content) {
        for (GetProductByCategoryResponseDto getProductByCategoryResponseDto : content) {
            getProductByCategoryResponseDto.setCategories(
                    from(category)
                            .leftJoin(productCategory)
                            .on(productCategory.category.categoryNo.eq(category.categoryNo))
                            .select(category.categoryName)
                            .where(productCategory.product.productNo.eq(getProductByCategoryResponseDto.getProductNo())).fetch());

            getProductByCategoryResponseDto.setAuthors(
                    from(author)
                            .innerJoin(productAuthor)
                            .on(productAuthor.author.authorNo.eq(author.authorNo))
                            .select(author.authorName)
                            .where(productAuthor.product.productNo.eq(getProductByCategoryResponseDto.getProductNo())).fetch());
        }
    }
}
