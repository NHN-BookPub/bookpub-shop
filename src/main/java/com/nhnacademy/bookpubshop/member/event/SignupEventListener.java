package com.nhnacademy.bookpubshop.member.event;

import com.nhnacademy.bookpubshop.coupon.entity.Coupon;
import com.nhnacademy.bookpubshop.coupon.repository.CouponRepository;
import com.nhnacademy.bookpubshop.coupontemplate.entity.CouponTemplate;
import com.nhnacademy.bookpubshop.coupontemplate.essential.EssentialCoupon;
import com.nhnacademy.bookpubshop.coupontemplate.service.CouponTemplateService;
import com.nhnacademy.bookpubshop.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 회원가입 이벤트 리스너.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@Component
@RequiredArgsConstructor
@Slf4j
public class SignupEventListener {
    private final CouponRepository couponRepository;
    private final CouponTemplateService couponTemplateService;

    /**
     * 회원가입 시 가입한 회원에게 회원가입 쿠폰을 발행해주는 이벤트입니다.
     *
     * @param signupEvent 회원가입 이벤트.
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, classes = SignupEvent.class)
    public void handler(SignupEvent signupEvent) {
        Member member = signupEvent.getMember();
        CouponTemplate couponTemplate = couponTemplateService
                .getCouponTemplateByName(EssentialCoupon.SIGN_UP.getCouponName());

        Coupon coupon = Coupon.builder().couponTemplate(couponTemplate).member(member).build();

        couponRepository.save(coupon);
    }
}
