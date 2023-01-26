package com.nhnacademy.bookpubshop.coupontemplate.dummy;

import com.nhnacademy.bookpubshop.category.entity.Category;
import com.nhnacademy.bookpubshop.couponpolicy.entity.CouponPolicy;
import com.nhnacademy.bookpubshop.couponstatecode.entity.CouponStateCode;
import com.nhnacademy.bookpubshop.coupontemplate.entity.CouponTemplate;
import com.nhnacademy.bookpubshop.coupontype.entity.CouponType;
import com.nhnacademy.bookpubshop.product.entity.Product;
import java.time.LocalDateTime;

/**
 * 쿠폰 템플릿 더미클래스입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public class CouponTemplateDummy {

    public static CouponTemplate dummy(CouponPolicy couponPolicy, CouponType couponType, Product product,
                                       Category category, CouponStateCode couponStateCode) {

        return new CouponTemplate(
                null,
                couponPolicy,
                couponType,
                product,
                category,
                couponStateCode,
                "test_templateName",
                LocalDateTime.of(1, 1, 1, 1, 1),
                LocalDateTime.of(1, 1, 1, 1, 1),
                false,
                false,
                null
        );
    }
}
