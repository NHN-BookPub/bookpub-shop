package com.nhnacademy.bookpubshop.subscribe.repository.impl;

import com.nhnacademy.bookpubshop.subscribe.dto.response.GetSubscribeResponseDto;
import com.nhnacademy.bookpubshop.subscribe.entity.QSubscribe;
import com.nhnacademy.bookpubshop.subscribe.entity.Subscribe;
import com.nhnacademy.bookpubshop.subscribe.repository.SubscribeRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import java.util.List;
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
                        subscribe.subscribeRenewed.as("isRenewed")
                ))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
    }
}
