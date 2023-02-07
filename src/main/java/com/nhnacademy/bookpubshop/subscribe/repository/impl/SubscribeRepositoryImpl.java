package com.nhnacademy.bookpubshop.subscribe.repository.impl;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.set;

import com.nhnacademy.bookpubshop.file.entity.QFile;
import com.nhnacademy.bookpubshop.product.entity.QProduct;
import com.nhnacademy.bookpubshop.subscribe.dto.response.GetSubscribeDetailResponseDto;
import com.nhnacademy.bookpubshop.subscribe.dto.response.GetSubscribeResponseDto;
import com.nhnacademy.bookpubshop.subscribe.dto.response.QGetSubscribeDetailResponseDto;
import com.nhnacademy.bookpubshop.subscribe.dto.response.QGetSubscribeDetailResponseDto_SubscribeProduct;
import com.nhnacademy.bookpubshop.subscribe.entity.QSubscribe;
import com.nhnacademy.bookpubshop.subscribe.entity.Subscribe;
import com.nhnacademy.bookpubshop.subscribe.relationship.entity.QSubscribeProductList;
import com.nhnacademy.bookpubshop.subscribe.repository.SubscribeRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import java.util.List;
import java.util.Map;
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
                        subscribe.salesPrice.as("salePrice"),
                        subscribe.subscribePrice.as("price"),
                        subscribe.salesRate.as("salePrice"),
                        subscribe.viewCount.as("viewCnt"),
                        subscribe.subscribeDeleted.as("isDeleted"),
                        subscribe.subscribeRenewed.as("isRenewed"),
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
//        QSubscribe subscribe = QSubscribe.subscribe;
//        QProduct product = QProduct.product;
//        QFile file = QFile.file;
//        QFile checkFile = new QFile("check");
//        QSubscribeProductList subscribeProductList = QSubscribeProductList.subscribeProductList;
//
//        Map<Long, GetSubscribeDetailResponseDto> transform = from(subscribeProductList)
//                .join(subscribeProductList.subscribe, subscribe)
//                .join(subscribeProductList.product, product)
//                .join(subscribe, file.subscribe)
//                .where(subscribe.subscribeNo.eq(subscribeNo))
//                .transform(groupBy(subscribe.subscribeNo)
//                        .as(new QGetSubscribeDetailResponseDto(
//                                subscribe.subscribeNo,
//                                subscribe.subscribeName,
//                                subscribe.subscribePrice,
//                                subscribe.salesPrice,
//                                subscribe.salesRate,
//                                subscribe.viewCount,
//                                subscribe.subscribeDeleted,
//                                subscribe.subscribeRenewed,
//                                subscribe.file.filePath,
//                                set(new QGetSubscribeDetailResponseDto_SubscribeProduct(
//                                        product.productNo,
//                                        product.title,
//                                        file.filePath))
//                        )));
//
//        return transform.keySet().stream()
//                .map(transform::get)
//                .findFirst();
        return Optional.empty();
    }
}
