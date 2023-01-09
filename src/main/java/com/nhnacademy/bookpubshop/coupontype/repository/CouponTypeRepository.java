package com.nhnacademy.bookpubshop.coupontype.repository;

import com.nhnacademy.bookpubshop.coupontype.entity.CouponType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 쿠폰유형을 데이터베이스에서 다루기위한 Repo 클래스입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public interface CouponTypeRepository extends JpaRepository<CouponType, Long>,
        CouponTypeRepositoryCustom {
}
