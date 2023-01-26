package com.nhnacademy.bookpubshop.coupontemplate.repository;

import com.nhnacademy.bookpubshop.coupontemplate.entity.CouponTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 쿠폰 템플릿을 데이터베이스에서 다루기위한 Repo 클래스입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public interface CouponTemplateRepository extends JpaRepository<CouponTemplate, Long>,
        CouponTemplateRepositoryCustom {

}
