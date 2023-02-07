package com.nhnacademy.bookpubshop.review.dummy;

import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.review.entity.Review;
import com.nhnacademy.bookpubshop.reviewpolicy.entity.ReviewPolicy;

/**
 * 상품평 더미 클래스
 * 테스트시 해당 클래스를 쉽게 생성하기 위한 클래스 입니다.
 * 동료들과 협력하여 코딩할 때 유용합니다.
 *
 * @author : 임태원
 * @since : 1.0
 **/
public class ReviewDummy {
    public static Review dummy(Member member, Product product, ReviewPolicy reviewPolicy) {
        return new Review(
                member,
                product,
                reviewPolicy,
                5,
                "좋습니다."
        );
    }
}
