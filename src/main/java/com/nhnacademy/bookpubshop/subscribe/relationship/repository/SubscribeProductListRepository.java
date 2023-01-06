package com.nhnacademy.bookpubshop.subscribe.relationship.repository;

import com.nhnacademy.bookpubshop.subscribe.relationship.entity.SubscribeProductList;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 구독상품리스트 레포지토리.
 *
 * @author : 여운석
 * @since : 1.0
 **/
public interface SubscribeProductListRepository extends JpaRepository<SubscribeProductList, Long> {
}