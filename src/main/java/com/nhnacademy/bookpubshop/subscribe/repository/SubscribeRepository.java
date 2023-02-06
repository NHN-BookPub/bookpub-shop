package com.nhnacademy.bookpubshop.subscribe.repository;

import com.nhnacademy.bookpubshop.subscribe.entity.Subscribe;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 구독 레포지토리입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
public interface SubscribeRepository extends JpaRepository<Subscribe, Long>,
        SubscribeRepositoryCustom {
}