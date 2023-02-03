package com.nhnacademy.bookpubshop.tier.relationship.repository;

import com.nhnacademy.bookpubshop.tier.relationship.entity.TierCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 회원등급과 쿠폰을 위한 인터페이스입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/

public interface TierCouponRepository extends JpaRepository<TierCoupon, TierCoupon.Pk>,
        TierCouponRepositoryCustom {

}
