package com.nhnacademy.bookpubshop.product.repository.impl;

import com.nhnacademy.bookpubshop.author.entity.QAuthor;
import com.nhnacademy.bookpubshop.category.entity.QCategory;
import com.nhnacademy.bookpubshop.file.entity.QFile;
import com.nhnacademy.bookpubshop.member.entity.QMember;
import com.nhnacademy.bookpubshop.order.entity.QBookpubOrder;
import com.nhnacademy.bookpubshop.order.relationship.entity.QOrderProduct;
import com.nhnacademy.bookpubshop.order.relationship.entity.QOrderProductStateCode;
import com.nhnacademy.bookpubshop.product.dto.response.GetProductByCategoryResponseDto;
import com.nhnacademy.bookpubshop.product.dto.response.GetProductByTypeResponseDto;
import com.nhnacademy.bookpubshop.product.dto.response.GetProductDetailResponseDto;
import com.nhnacademy.bookpubshop.product.dto.response.GetProductListForOrderResponseDto;
import com.nhnacademy.bookpubshop.product.dto.response.GetProductListResponseDto;
import com.nhnacademy.bookpubshop.product.dto.response.GetRelationProductInfoResponseDto;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.entity.QProduct;
import com.nhnacademy.bookpubshop.product.relationship.entity.QProductAuthor;
import com.nhnacademy.bookpubshop.product.relationship.entity.QProductCategory;
import com.nhnacademy.bookpubshop.product.relationship.entity.QProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.QProductTypeStateCode;
import com.nhnacademy.bookpubshop.product.repository.ProductRepositoryCustom;
import com.nhnacademy.bookpubshop.state.FileCategory;
import com.nhnacademy.bookpubshop.state.ProductSaleState;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
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
    QProductTypeStateCode productTypeStateCode = QProductTypeStateCode.productTypeStateCode;
    QMember member = QMember.member;


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

        List<GetProductListResponseDto> query = queryFactory
                .from(product)
                .select(Projections.constructor(GetProductListResponseDto.class,
                        product.productNo,
                        product.title,
                        file.filePath,
                        product.productStock,
                        product.salesPrice,
                        product.salesRate,
                        product.productSubscribed,
                        product.productPrice,
                        product.productDeleted))
                .leftJoin(product.files, file)
                .on(file.fileCategory.eq(FileCategory.PRODUCT_THUMBNAIL.getCategory()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPQLQuery<Long> count = from(product)
                .select(product.count());

        return new PageImpl<>(query, pageable, count.fetchOne());
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
                        product.productSubscribed,
                        product.productPrice,
                        product.productDeleted))
                .leftJoin(product.files, file)
                .on(file.fileCategory.eq(FileCategory.PRODUCT_THUMBNAIL.getCategory()))
                .where(product.title.like(title)
                        .and(product.productDeleted.isFalse()))
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
    public Optional<GetProductDetailResponseDto> getProductDetailById(Long productNo) {
        QProduct childProduct = new QProduct("child");

        Optional<Product> content = Optional.ofNullable(from(product)
                .where(product.productNo.eq(productNo))
                .leftJoin(product.productPolicy)
                .leftJoin(product.productSaleStateCode)
                .leftJoin(product.productTypeStateCode)
                .leftJoin(product.productAuthors)
                .leftJoin(product.productTags)
                .leftJoin(file).on(product.productNo.eq(file.product.productNo))
                .select(product)
                .where(product.productNo.eq(productNo))
                .fetchOne());

        Optional<GetProductDetailResponseDto> getProductDetailResponseDto =
                content.map(GetProductDetailResponseDto::new);

        List<GetRelationProductInfoResponseDto> relationInfo = from(childProduct)
                .leftJoin(childProduct.files, file)
                .innerJoin(childProduct.parentProduct, product)
                .select(Projections.fields(GetRelationProductInfoResponseDto.class,
                        childProduct.productNo,
                        childProduct.title,
                        file.filePath,
                        childProduct.salesPrice))
                .where(childProduct.parentProduct.productNo.eq(productNo)
                        .and(file.fileCategory.eq(FileCategory.PRODUCT_THUMBNAIL.getCategory())
                                .or(file.fileCategory.isNull())))
                .fetch();

        assert getProductDetailResponseDto.isPresent();
        getProductDetailResponseDto.get().addRelationInfo(relationInfo);


        return getProductDetailResponseDto;
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
                .leftJoin(productSaleStateCode)
                .on(product.productSaleStateCode.codeNo.eq(productSaleStateCode.codeNo))
                .leftJoin(file).on(product.productNo.eq(file.product.productNo)
                        .and(file.fileCategory.eq(FileCategory.PRODUCT_THUMBNAIL.getCategory())))
                .select(Projections.fields(GetProductByTypeResponseDto.class,
                        product.productNo,
                        product.title,
                        file.filePath.as(FileCategory.PRODUCT_THUMBNAIL.getCategory()),
                        product.salesPrice,
                        product.productPrice,
                        product.salesRate))
                .where(product.productTypeStateCode.codeNo.eq(typeNo)
                        .and(product.productDeleted.isFalse())
                        .and(productSaleStateCode.codeCategory.eq(ProductSaleState.SALE.getName())))
                .distinct()
                .orderBy(Expressions.numberTemplate(Double.class, "function('rand')").asc())
                .limit(limit)
                .fetch();

        result.forEach(m -> m.getProductCategories().add(
                        String.valueOf(
                                from(category)
                                        .leftJoin(productCategory)
                                        .on(productCategory.category.categoryNo
                                                .eq(category.categoryNo))
                                        .select(category.categoryName)
                                        .where(productCategory.product.productNo
                                                .eq(m.getProductNo())).fetch())
                ));

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
                        orderProduct.orderProductNo,
                        product.productNo,
                        product.title,
                        file.filePath,
                        product.salesPrice,
                        orderProduct.productAmount,
                        orderProductStateCode.codeName))
                .innerJoin(orderProduct.product, product)
                .innerJoin(orderProduct.orderProductStateCode, orderProductStateCode)
                .innerJoin(orderProduct.order, order)
                .leftJoin(file)
                .on(product.productNo.eq(file.product.productNo)
                        .and(file.fileCategory.eq(FileCategory.PRODUCT_THUMBNAIL.getCategory())))
                .where(order.orderNo.eq(orderNo))
                .fetch();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetProductDetailResponseDto> getProductsInCart(List<Long> productsNo) {
        List<GetProductDetailResponseDto> policySaveRate = from(product)
                .select(Projections.fields(GetProductDetailResponseDto.class,
                        product.productNo,
                        product.title,
                        file.filePath.as(FileCategory.PRODUCT_THUMBNAIL.getCategory()),
                        product.productPublisher,
                        product.productPrice,
                        product.salesPrice,
                        product.productStock,
                        product.productPolicy.policyMethod,
                        product.productPolicy.policySaved,
                        product.productPolicy.saveRate.as("policySaveRate")
                        ))
                .leftJoin(file).on(product.productNo.eq(file.product.productNo)
                        .and(file.fileCategory.eq(FileCategory.PRODUCT_THUMBNAIL.getCategory())))
                .where(product.productNo.in(productsNo))
                .fetch();

        policySaveRate.forEach(p -> p.addEbookPath(getEbookPath(p.getProductNo())));

        return policySaveRate;
    }

    private String getEbookPath(Long productNo) {
        return from(product)
                .select(Projections.fields(GetProductDetailResponseDto.class,
                        file.filePath.as(FileCategory.PRODUCT_EBOOK.getCategory())
                ))
                .leftJoin(file).on(product.productNo.eq(file.product.productNo)
                        .and(file.fileCategory.eq(FileCategory.PRODUCT_EBOOK.getCategory())))
                .where(product.productNo.eq(productNo))
                .fetchOne().getEbook();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetProductByCategoryResponseDto> getProductsByCategory(
            Integer categoryNo, Pageable pageable) {
        List<GetProductByCategoryResponseDto> content =
                from(product)
                        .select(Projections.fields(GetProductByCategoryResponseDto.class,
                                product.productNo,
                                product.title,
                                file.filePath.as(FileCategory.PRODUCT_THUMBNAIL.getCategory()),
                                product.salesPrice,
                                product.salesRate))
                        .innerJoin(product.productSaleStateCode, productSaleStateCode)
                        .on(productSaleStateCode.codeCategory
                                .notIn(ProductSaleState.STOP.getName()))
                        .innerJoin(product.productCategories, productCategory)
                        .on(productCategory.product.productNo.eq(product.productNo)
                                .and(productCategory.category.categoryNo.eq(categoryNo)))
                        .leftJoin(file).on(product.productNo.eq(file.product.productNo)
                                .and((file.fileCategory
                                        .eq(FileCategory.PRODUCT_THUMBNAIL.getCategory()))
                                        .or(file.fileCategory.isNull())))
                        .orderBy(product.productPriority.asc())
                        .where(product.productDeleted.isFalse())
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();

        setCategoryAndAuthors(content);

        JPQLQuery<Long> count = from(product)
                .innerJoin(product.productSaleStateCode, productSaleStateCode)
                .on(productSaleStateCode.codeCategory.notIn(ProductSaleState.STOP.getName()))
                .innerJoin(product.productCategories, productCategory)
                .on(productCategory.product.productNo.eq(product.productNo)
                        .and(productCategory.category.categoryNo.eq(categoryNo)))
                .leftJoin(file).on(product.productNo.eq(file.product.productNo)
                        .and((file.fileCategory.eq(FileCategory.PRODUCT_THUMBNAIL.getCategory()))
                                .or(file.fileCategory.isNull())))
                .orderBy(product.productPriority.asc())
                .where(product.productDeleted.isFalse())
                .select(product.count());

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
                        .innerJoin(file).on(product.productNo.eq(file.product.productNo)
                                .and(file.fileCategory
                                        .eq(FileCategory.PRODUCT_EBOOK.getCategory())))
                        .orderBy(product.productPriority.asc())
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();

        setCategoryAndAuthors(content);
        setProductThumbnails(content);

        JPQLQuery<Long> count = from(product)
                .innerJoin(product.productSaleStateCode, productSaleStateCode)
                .on(productSaleStateCode.codeCategory.eq(ProductSaleState.SALE.getName()))
                .innerJoin(file).on(product.productNo.eq(file.product.productNo)
                        .and(file.fileCategory.eq(FileCategory.PRODUCT_EBOOK.getCategory())))
                .select(product.count())
                .where(product.productSaleStateCode.codeCategory
                        .eq(ProductSaleState.SALE.getName()));

        return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetProductByCategoryResponseDto> getEbooksByMember(
            Pageable pageable, Long memberNo) {
        List<GetProductByCategoryResponseDto> content =
                from(orderProduct)
                        .select(Projections.fields(GetProductByCategoryResponseDto.class,
                                product.productNo,
                                product.title,
                                file.filePath.as(FileCategory.PRODUCT_EBOOK.getCategory()),
                                product.salesPrice,
                                product.salesRate))
                        .innerJoin(order).on(orderProduct.order.orderNo.eq(order.orderNo))
                        .innerJoin(product).on(product.productNo.eq(orderProduct.product.productNo))
                        .innerJoin(member).on(order.member.memberNo.eq(member.memberNo)
                                .and(member.memberNo.eq(memberNo)))
                        .innerJoin(file).on(file.product.productNo.eq(product.productNo))
                        .where(file.fileCategory.eq(FileCategory.PRODUCT_EBOOK.getCategory()))
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();

        setProductThumbnails(content);
        setCategoryAndAuthors(content);

        JPQLQuery<Long> count = from(orderProduct)
                .innerJoin(order).on(orderProduct.order.orderNo.eq(order.orderNo))
                .innerJoin(product).on(product.productNo.eq(orderProduct.product.productNo))
                .innerJoin(member).on(order.member.memberNo.eq(member.memberNo)
                        .and(member.memberNo.eq(memberNo)))
                .innerJoin(file).on(file.product.productNo.eq(product.productNo)
                        .and(file.fileCategory.eq(FileCategory.PRODUCT_EBOOK.getCategory())))
                .select(product.count())
                .where(member.memberNo.eq(memberNo));

        return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
    }


    /**
     * 상품리스트에 썸네일을 각각 꽂아줍니다.
     *
     * @param content 상품 Dto 리스트
     */
    private void setProductThumbnails(List<GetProductByCategoryResponseDto> content) {
        for (GetProductByCategoryResponseDto getProductByCategoryResponseDto : content) {
            getProductByCategoryResponseDto.setThumbnail(
                    from(file)
                            .innerJoin(product)
                            .on(file.product.productNo.eq(product.productNo))
                            .select(file.filePath)
                            .where(file.fileCategory
                                    .eq(FileCategory.PRODUCT_THUMBNAIL.getCategory())
                                    .and(file.product.productNo
                                            .eq(getProductByCategoryResponseDto.getProductNo())))
                            .fetchOne());
        }
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
                            .where(productCategory.product.productNo
                                    .eq(getProductByCategoryResponseDto.getProductNo())).fetch());

            getProductByCategoryResponseDto.setAuthors(
                    from(author)
                            .innerJoin(productAuthor)
                            .on(productAuthor.author.authorNo.eq(author.authorNo))
                            .select(author.authorName)
                            .where(productAuthor.product.productNo
                                    .eq(getProductByCategoryResponseDto.getProductNo())).fetch());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFilePath(Long productNo) {
        return from(product)
                .leftJoin(file)
                .on(file.product.productNo.eq(productNo))
                .where(product.productNo.eq(productNo)
                        .and(file.fileCategory.eq(FileCategory.PRODUCT_EBOOK.getCategory())))
                .select(file.filePath)
                .fetchOne();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetProductByCategoryResponseDto> getProductsByTypes(
            Integer typeNo, Pageable pageable) {
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
                        .innerJoin(productTypeStateCode).on(productTypeStateCode.codeNo
                                .eq(product.productTypeStateCode.codeNo))
                        .leftJoin(file).on(product.productNo.eq(file.product.productNo)
                                .and(file.fileCategory
                                        .eq(FileCategory.PRODUCT_THUMBNAIL.getCategory())))
                        .orderBy(product.productPriority.asc())
                        .where(productTypeStateCode.codeNo.eq(typeNo))
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();

        setCategoryAndAuthors(content);

        JPQLQuery<Long> count = from(product)
                .innerJoin(product.productSaleStateCode, productSaleStateCode)
                .on(productSaleStateCode.codeCategory.eq(ProductSaleState.SALE.getName()))
                .innerJoin(productTypeStateCode).on(productTypeStateCode.codeNo
                        .eq(product.productTypeStateCode.codeNo))
                .leftJoin(file).on(product.productNo.eq(file.product.productNo)
                        .and(file.fileCategory
                                .eq(FileCategory.PRODUCT_THUMBNAIL.getCategory())))
                .orderBy(product.productPriority.asc())
                .where(productTypeStateCode.codeNo.eq(typeNo))
                .select(product.count());

        return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
    }

    @Override
    public boolean isPurchaseUser(String memberNo, String productNo) {
        return from(orderProduct)
                .innerJoin(orderProduct.order, order)
                .innerJoin(order.member, member)
                .innerJoin(orderProduct.product, product)
                .innerJoin(product.productCategories, productCategory)
                .innerJoin(productCategory.category, category)
                .where(member.memberNo.eq(Long.parseLong(memberNo))
                        .and(productCategory.product.productNo.eq(Long.parseLong(productNo))
                                .and(productCategory.category.categoryName.eq("eBook"))))
                .select(orderProduct.orderProductNo)
                .fetchOne() != null;
    }
}
