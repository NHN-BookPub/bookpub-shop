package com.nhnacademy.bookpubshop.coupon.dummy;

import com.nhnacademy.bookpubshop.coupon.dto.response.GetCouponResponseDto;
import com.nhnacademy.bookpubshop.coupon.entity.Coupon;
import com.nhnacademy.bookpubshop.coupontemplate.entity.CouponTemplate;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.order.entity.BookpubOrder;
import com.nhnacademy.bookpubshop.order.relationship.entity.OrderProduct;
import java.time.LocalDateTime;

/**
 * 쿠폰 더미 클래스
 * 테스트시 해당 클래스를 쉽게 생성하기 위한 클래스 입니다.
 * 동료들과 협력하여 코딩할 때 유용합니다.
 *
 * @author : 임태원
 * @since : 1.0
 **/
public class CouponDummy {
    public static Coupon dummy(CouponTemplate couponTemplate,
                               BookpubOrder order,
                               OrderProduct orderProduct,
                               Member member) {
        return new Coupon(
                null,
                couponTemplate,
                order,
                orderProduct,
                member,
                false,
                LocalDateTime.now()
        );
    }

    public static GetCouponResponseDto getCouponResponseDtoDummy(){
        return new GetCouponResponseDto(1L, "id", "name",
                "image", "name", true, 1L, 1L, 10L, LocalDateTime.now(), false
        );
    }
}
