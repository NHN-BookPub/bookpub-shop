package com.nhnacademy.bookpubshop.purchase.repository;

import com.nhnacademy.bookpubshop.purchase.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 매입이력(purchase) 레포지토리.
 *
 * @author : 박경서
 * @since : 1.0
 **/
public interface PurchaseRepository extends JpaRepository<Purchase, Long>,
        PurchaseRepositoryCustom {
}