package com.nhnacademy.bookpubshop.subscribe.repository.impl;


import com.nhnacademy.bookpubshop.file.entity.QFile;
import com.nhnacademy.bookpubshop.product.entity.QProduct;
import com.nhnacademy.bookpubshop.state.FileCategory;
import com.nhnacademy.bookpubshop.subscribe.dto.response.GetSubscribeDetailResponseDto;
import com.nhnacademy.bookpubshop.subscribe.dto.response.GetSubscribeProductListDto;
import com.nhnacademy.bookpubshop.subscribe.dto.response.GetSubscribeResponseDto;
import com.nhnacademy.bookpubshop.subscribe.entity.QSubscribe;
import com.nhnacademy.bookpubshop.subscribe.entity.Subscribe;
import com.nhnacademy.bookpubshop.subscribe.relationship.entity.QSubscribeProductList;
import com.nhnacademy.bookpubshop.subscribe.repository.SubscribeRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;

/**
 * QueryDsl 을 사용하기위한 구현 클래스입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
public class SubscribeRepositoryImpl extends QuerydslRepositorySupport
        implements SubscribeRepositoryCustom {
    public SubscribeRepositoryImpl() {
        super(Subscribe.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetSubscribeResponseDto> getSubscribes(Pageable pageable) {
        QSubscribe subscribe = QSubscribe.subscribe;
        QFile file = QFile.file;

        JPQLQuery<Long> count = from(subscribe)
                .select(subscribe.count());

        List<GetSubscribeResponseDto> content = from(subscribe)
                .select(Projections.constructor(
                        GetSubscribeResponseDto.class,
                        subscribe.subscribeNo,
                        subscribe.subscribeName,
                        subscribe.subscribePrice.as("price"),
                        subscribe.salesPrice,
                        subscribe.salesRate.as("salesRate"),
                        subscribe.viewCount.as("viewCnt"),
                        subscribe.subscribeDeleted.as("deleted"),
                        subscribe.subscribeRenewed.as("renewed"),
                        file.filePath.as("imagePath")
                ))
                .leftJoin(subscribe.file, file)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
    }

    @Override
    public Optional<GetSubscribeDetailResponseDto> getSubscribeDetail(Long subscribeNo) {
        QSubscribe subscribe = QSubscribe.subscribe;
        QSubscribeProductList productList = QSubscribeProductList.subscribeProductList;
        QProduct product = QProduct.product;
        QFile file = QFile.file;

        GetSubscribeDetailResponseDto result = from(subscribe)
                .select(Projections.constructor(
                        GetSubscribeDetailResponseDto.class,
                        subscribe.subscribeNo,
                        subscribe.subscribeName,
                        subscribe.subscribePrice,
                        subscribe.salesPrice,
                        subscribe.salesRate.as("salesRate"),
                        subscribe.viewCount.as("viewCnt"),
                        subscribe.subscribeDeleted.as("deleted"),
                        subscribe.subscribeRenewed.as("renewed"),
                        file.filePath.as("imagePath")))
                .leftJoin(subscribe.file, file)
                .where(subscribe.subscribeNo.eq(subscribeNo))
                .fetchOne();

        List<GetSubscribeProductListDto> products = from(productList)
                .select(Projections.constructor(GetSubscribeProductListDto.class,
                        product.productNo,
                        product.title,
                        file.filePath))
                .innerJoin(productList.product, product)
                .innerJoin(productList.subscribe, subscribe)
                .leftJoin(product.files, file)
                .where(file.fileCategory.eq(FileCategory.PRODUCT_THUMBNAIL.getCategory())
                        .or(file.fileCategory.isNull())
                        .and(subscribe.subscribeNo.eq(subscribeNo))
                        .and(product.productDeleted.isFalse()))
                .fetch();

        result.inputProductLists(products);
        return Optional.of(result);
    }
}
