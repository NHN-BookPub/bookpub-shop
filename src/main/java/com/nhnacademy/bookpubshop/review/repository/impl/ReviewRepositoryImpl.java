package com.nhnacademy.bookpubshop.review.repository.impl;

import com.nhnacademy.bookpubshop.author.entity.QAuthor;
import com.nhnacademy.bookpubshop.file.entity.QFile;
import com.nhnacademy.bookpubshop.member.entity.QMember;
import com.nhnacademy.bookpubshop.order.entity.QBookpubOrder;
import com.nhnacademy.bookpubshop.order.relationship.entity.QOrderProduct;
import com.nhnacademy.bookpubshop.order.relationship.entity.QOrderProductStateCode;
import com.nhnacademy.bookpubshop.product.dto.response.GetProductSimpleResponseDto;
import com.nhnacademy.bookpubshop.product.entity.QProduct;
import com.nhnacademy.bookpubshop.product.relationship.entity.QProductAuthor;
import com.nhnacademy.bookpubshop.review.dto.response.GetMemberReviewResponseDto;
import com.nhnacademy.bookpubshop.review.dto.response.GetProductReviewInfoResponseDto;
import com.nhnacademy.bookpubshop.review.dto.response.GetProductReviewResponseDto;
import com.nhnacademy.bookpubshop.review.entity.QReview;
import com.nhnacademy.bookpubshop.review.entity.Review;
import com.nhnacademy.bookpubshop.review.repository.ReviewRepositoryCustom;
import com.nhnacademy.bookpubshop.state.FileCategory;
import com.nhnacademy.bookpubshop.state.OrderProductState;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;

/**
 * 상품평 레포지토리 구현체.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public class ReviewRepositoryImpl extends QuerydslRepositorySupport
        implements ReviewRepositoryCustom {
    public ReviewRepositoryImpl() {
        super(Review.class);
    }

    QReview review = QReview.review;
    QProduct product = QProduct.product;
    QMember member = QMember.member;
    QAuthor author = QAuthor.author;
    QProductAuthor productAuthor = QProductAuthor.productAuthor;
    QFile reviewFile = new QFile("reviewFile");
    QFile productFile = new QFile("productFile");

    private static final String PRODUCT_IMAGE_PATH = "productImagePath";

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetProductReviewResponseDto> findProductReviews(Pageable pageable, Long productNo) {
        JPQLQuery<Long> count = from(review)
                .join(review.member, member)
                .leftJoin(reviewFile)
                .on(review.reviewNo.eq(reviewFile.review.reviewNo))
                .where(review.product.productNo.eq(productNo)
                        .and(review.reviewDeleted.isFalse()))
                .select(review.count());

        List<GetProductReviewResponseDto> content = from(review)
                .join(review.member, member)
                .leftJoin(reviewFile)
                .on(review.reviewNo.eq(reviewFile.review.reviewNo))
                .where(review.product.productNo.eq(productNo)
                        .and(review.reviewDeleted.isFalse()))
                .select(Projections.constructor(GetProductReviewResponseDto.class,
                        review.reviewNo,
                        member.memberNickname,
                        review.reviewStar,
                        review.reviewContent,
                        reviewFile.filePath.as("imagePath"),
                        review.createdAt))
                .orderBy(review.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetMemberReviewResponseDto> findMemberReviews(Pageable pageable, Long memberNo) {
        JPQLQuery<Long> count = from(review)
                .select(review.count())
                .leftJoin(reviewFile)
                .on(review.reviewNo.eq(reviewFile.review.reviewNo))
                .leftJoin(productFile)
                .on(review.product.productNo.eq(productFile.product.productNo))
                .innerJoin(review.product, product)
                .where(review.member.memberNo.eq(memberNo)
                        .and(review.reviewDeleted.isFalse())
                        .and((productFile.fileCategory
                                .eq(FileCategory.PRODUCT_THUMBNAIL.getCategory()))
                                .or(productFile.fileCategory.isNull())));

        List<GetMemberReviewResponseDto> content = from(review)
                .leftJoin(reviewFile)
                .on(review.reviewNo.eq(reviewFile.review.reviewNo))
                .leftJoin(productFile)
                .on(review.product.productNo.eq(productFile.product.productNo))
                .innerJoin(review.product, product)
                .where(review.member.memberNo.eq(memberNo)
                        .and(review.reviewDeleted.isFalse())
                        .and((productFile.fileCategory
                                .eq(FileCategory.PRODUCT_THUMBNAIL.getCategory()))
                                .or(productFile.fileCategory.isNull())))
                .select(Projections.fields(GetMemberReviewResponseDto.class,
                        review.reviewNo,
                        product.productNo,
                        product.title.as("productTitle"),
                        product.productPublisher,
                        productFile.filePath.as(PRODUCT_IMAGE_PATH),
                        review.reviewStar,
                        review.reviewContent,
                        reviewFile.filePath.as("reviewImagePath"),
                        review.createdAt))
                .orderBy(review.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        for (GetMemberReviewResponseDto dto : content) {
            dto.setAuthorNames(
                    from(author)
                            .innerJoin(productAuthor)
                            .on(productAuthor.author.authorNo.eq(author.authorNo))
                            .select(author.authorName)
                            .where(productAuthor.product.productNo.eq(dto.getProductNo()))
                            .fetch());
        }

        return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetProductSimpleResponseDto> findWritableMemberReviews(
            Pageable pageable, Long memberNo) {
        QBookpubOrder order = QBookpubOrder.bookpubOrder;
        QOrderProduct orderProduct = QOrderProduct.orderProduct;
        QOrderProductStateCode orderProductStateCode = QOrderProductStateCode.orderProductStateCode;

        JPQLQuery<Long> count = from(orderProduct)
                .leftJoin(productFile)
                .on(orderProduct.product.productNo.eq(productFile.product.productNo))
                .join(orderProduct.order, order)
                .join(orderProduct.orderProductStateCode, orderProductStateCode)
                .join(orderProduct.product, product)
                .leftJoin(review)
                .on((orderProduct.product.productNo.eq(review.product.productNo)).and(
                        orderProduct.order.member.memberNo.eq(review.member.memberNo)))
                .where(order.member.memberNo.eq(memberNo)
                        .and(orderProduct.orderProductStateCode.codeName
                                .eq(OrderProductState.CONFIRMED.getName()))
                        .and((review.product.isNull()).or(product.productNo.notIn(
                                JPAExpressions.select(product.productNo)
                                        .from(review)
                                        .join(review.product, product)
                                        .join(review.member, member)
                                        .where(member.memberNo.eq(memberNo)
                                                .and(review.reviewDeleted.isFalse()))
                        )))
                        .and((productFile.fileCategory.eq(FileCategory
                                .PRODUCT_THUMBNAIL.getCategory()))
                                .or(productFile.fileCategory.isNull())))
                .distinct()
                .select(orderProduct.count());

        List<GetProductSimpleResponseDto> content = from(orderProduct)
                .leftJoin(productFile)
                .on(orderProduct.product.productNo.eq(productFile.product.productNo))
                .join(orderProduct.order, order)
                .join(orderProduct.orderProductStateCode, orderProductStateCode)
                .join(orderProduct.product, product)
                .leftJoin(review)
                .on((orderProduct.product.productNo.eq(review.product.productNo)).and(
                        orderProduct.order.member.memberNo.eq(review.member.memberNo)))
                .where(order.member.memberNo.eq(memberNo)
                        .and(orderProduct.orderProductStateCode.codeName
                                .eq(OrderProductState.CONFIRMED.getName()))
                        .and((review.product.isNull()).or(product.productNo.notIn(
                                JPAExpressions.select(product.productNo)
                                        .from(review)
                                        .join(review.product, product)
                                        .join(review.member, member)
                                        .where(member.memberNo.eq(memberNo)
                                                .and(review.reviewDeleted.isFalse()))
                        )))
                        .and((productFile.fileCategory
                                .eq(FileCategory.PRODUCT_THUMBNAIL.getCategory()))
                                .or(productFile.fileCategory.isNull())))
                .select(Projections.fields(GetProductSimpleResponseDto.class,
                        product.productNo,
                        product.title,
                        product.productIsbn,
                        product.productPublisher,
                        productFile.filePath.as(PRODUCT_IMAGE_PATH)))
                .distinct()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        for (GetProductSimpleResponseDto dto : content) {
            dto.setAuthorNames(
                    from(author)
                            .innerJoin(productAuthor)
                            .on(productAuthor.author.authorNo.eq(author.authorNo))
                            .select(author.authorName)
                            .where(productAuthor.product.productNo.eq(dto.getProductNo()))
                            .fetch());
        }

        return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<GetMemberReviewResponseDto> findReview(Long reviewNo) {
        GetMemberReviewResponseDto dto = from(review)
                .leftJoin(reviewFile)
                .on(review.reviewNo.eq(reviewFile.review.reviewNo))
                .leftJoin(productFile)
                .on(review.product.productNo.eq(productFile.product.productNo))
                .innerJoin(review.product, product)
                .where(review.reviewNo.eq(reviewNo)
                        .and((productFile.fileCategory
                                .eq(FileCategory.PRODUCT_THUMBNAIL.getCategory()))
                        .or(productFile.fileCategory.isNull())))
                .select(Projections.fields(GetMemberReviewResponseDto.class,
                        review.reviewNo,
                        product.productNo,
                        product.title.as("productTitle"),
                        product.productPublisher,
                        productFile.filePath.as(PRODUCT_IMAGE_PATH),
                        review.reviewStar,
                        review.reviewContent,
                        reviewFile.filePath.as("reviewImagePath"),
                        review.createdAt))
                .fetchOne();

        dto.setAuthorNames(
                from(author)
                        .innerJoin(productAuthor)
                        .on(productAuthor.author.authorNo.eq(author.authorNo))
                        .select(author.authorName)
                        .where(productAuthor.product.productNo.eq(dto.getProductNo()))
                        .fetch());

        return Optional.of(dto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<GetProductReviewInfoResponseDto> findReviewInfoByProductNo(Long productNo) {
        return Optional.of(from(review)
                .select(Projections.constructor(
                        GetProductReviewInfoResponseDto.class,
                        review.count().coalesce(0L).as("reviewCount"),
                        review.reviewStar.avg().intValue().coalesce(0).as("productStar")
                ))
                .where(review.product.productNo.eq(productNo)
                        .and(review.reviewDeleted.isFalse()))
                .fetchOne());
    }

    @Override
    public boolean checkDeletedReview(Long memberNo, Long productNo) {
        return from(review)
                .innerJoin(review.member, member)
                .innerJoin(review.product, product)
                .where(member.memberNo.eq(memberNo)
                        .and(product.productNo.eq(productNo))
                        .and(review.reviewDeleted.isTrue()))
                .fetchOne() == null;
    }
}
