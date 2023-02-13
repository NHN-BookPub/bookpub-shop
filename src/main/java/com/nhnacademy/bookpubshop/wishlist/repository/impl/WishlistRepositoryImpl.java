package com.nhnacademy.bookpubshop.wishlist.repository.impl;

import com.nhnacademy.bookpubshop.file.entity.QFile;
import com.nhnacademy.bookpubshop.member.entity.QMember;
import com.nhnacademy.bookpubshop.product.entity.QProduct;
import com.nhnacademy.bookpubshop.product.relationship.entity.QProductSaleStateCode;
import com.nhnacademy.bookpubshop.state.FileCategory;
import com.nhnacademy.bookpubshop.state.ProductSaleState;
import com.nhnacademy.bookpubshop.wishlist.dto.response.GetAppliedMemberResponseDto;
import com.nhnacademy.bookpubshop.wishlist.dto.response.GetWishlistResponseDto;
import com.nhnacademy.bookpubshop.wishlist.entity.QWishlist;
import com.nhnacademy.bookpubshop.wishlist.entity.Wishlist;
import com.nhnacademy.bookpubshop.wishlist.repository.WishlistRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;

/**
 * 커스텀 위시리스트 레포지토리 구현체.
 *
 * @author : 박경서
 * @since : 1.0
 **/
@Slf4j
public class WishlistRepositoryImpl extends QuerydslRepositorySupport
        implements WishlistRepositoryCustom {

    QWishlist wishlist = QWishlist.wishlist;
    QProduct product = QProduct.product;
    QFile file = QFile.file;
    QMember member = QMember.member;
    QProductSaleStateCode productSaleStateCode = QProductSaleStateCode.productSaleStateCode;


    public WishlistRepositoryImpl() {
        super(Wishlist.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetWishlistResponseDto> findWishlistByMember(Pageable pageable, Long memberNo) {
        List<GetWishlistResponseDto> result = from(wishlist)
                .join(wishlist.member, member)
                .join(wishlist.product, product)
                .leftJoin(file)
                .on(product.productNo.eq(file.product.productNo))
                .leftJoin(productSaleStateCode)
                .on(product.productSaleStateCode.codeNo.eq(productSaleStateCode.codeNo))
                .where(member.memberNo.eq(memberNo)
                        .and((file.fileCategory.eq(FileCategory.PRODUCT_THUMBNAIL.getCategory()))
                                .or(file.fileCategory.isNull())))
                .select(Projections.fields(GetWishlistResponseDto.class,
                        product.productNo,
                        product.title,
                        product.productPublisher,
                        file.filePath.as("thumbnail"),
                        productSaleStateCode.codeCategory,
                        wishlist.wishlistApplied))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPQLQuery<Long> count = from(wishlist)
                .join(wishlist.member, member)
                .join(wishlist.product, product)
                .leftJoin(file)
                .on(product.productNo.eq(file.product.productNo))
                .where(member.memberNo.eq(memberNo)
                        .and((file.fileCategory.eq(FileCategory.PRODUCT_THUMBNAIL.getCategory()))
                                .or(file.fileCategory.isNull())))
                .select(wishlist.count());

        return PageableExecutionUtils.getPage(result, pageable, count::fetchOne);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetAppliedMemberResponseDto> findWishlistAppliedMembers(Long productNo) {
        return from(wishlist)
                .innerJoin(wishlist.member, member)
                .innerJoin(wishlist.product, product)
                .select(Projections.constructor(GetAppliedMemberResponseDto.class,
                        member.memberNo,
                        member.memberNickname,
                        member.memberPhone,
                        product.productNo,
                        product.title))
                .where(wishlist.member.memberBlocked.isFalse()
                        .and(wishlist.product.productSaleStateCode.codeCategory.eq(ProductSaleState.SOLD_OUT.getName()))
                        .and(wishlist.wishlistApplied.isTrue())
                        .and(wishlist.product.productNo.eq(productNo)))
                .fetch();
    }
}
