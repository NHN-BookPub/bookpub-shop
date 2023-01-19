package com.nhnacademy.bookpubshop.pricepolicy.repository;

import com.nhnacademy.bookpubshop.pricepolicy.entity.PricePolicy;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 가격정책을 데이터베이스에서 다루기위한 Repo 클래스입니다.
 *
 * @author : 김서현
 * @since : 1.0
 **/
public interface PricePolicyRepository extends JpaRepository<PricePolicy, Integer>,
        PricePolicyRepositoryCustom {
    Optional<PricePolicy> getByPolicyName(String name);
}
