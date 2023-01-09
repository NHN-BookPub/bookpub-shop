package com.nhnacademy.bookpubshop.couponpolicy.repository;

import com.nhnacademy.bookpubshop.couponpolicy.entity.CouponPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 쿠폰정책을 데이터베이스에서 다루기위한 Repo 클래스입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public interface CouponPolicyRepository extends JpaRepository<CouponPolicy, Integer>,
        CouponPolicyRepositoryCustom {
}
