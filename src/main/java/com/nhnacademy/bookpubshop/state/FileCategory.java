package com.nhnacademy.bookpubshop.state;

import lombok.Getter;

/**
 * 파일 분류를 관리하기 위한 Enum 입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Getter
public enum FileCategory {
    COUPON("coupon", "coupon_template"),
    PRODUCT_THUMBNAIL("thumbnail", "product/thumbnail"),
    PRODUCT_DETAIL("detail", "product/detail"),
    PRODUCT_EBOOK("ebook", "ebook"),
    REVIEW("review", "review"),
    INQUIRY("inquiry", "inquiry"),
    SERVICE("service", "customer_service"),
    BANNER("banner", "banner");

    private final String category;
    private final String path;

    FileCategory(String category, String path) {
        this.category = category;
        this.path = path;
    }
}
