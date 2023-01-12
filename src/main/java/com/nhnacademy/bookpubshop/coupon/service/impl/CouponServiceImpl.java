package com.nhnacademy.bookpubshop.coupon.service.impl;

import com.nhnacademy.bookpubshop.coupon.dto.request.CreateCouponRequestDto;
import com.nhnacademy.bookpubshop.coupon.dto.request.ModifyCouponRequestDto;
import com.nhnacademy.bookpubshop.coupon.dto.response.GetCouponResponseDto;
import com.nhnacademy.bookpubshop.coupon.entity.Coupon;
import com.nhnacademy.bookpubshop.coupon.exception.CouponNotFoundException;
import com.nhnacademy.bookpubshop.coupon.repository.CouponRepository;
import com.nhnacademy.bookpubshop.coupon.service.CouponService;
import com.nhnacademy.bookpubshop.coupontemplate.entity.CouponTemplate;
import com.nhnacademy.bookpubshop.coupontemplate.exception.CouponTemplateNotFoundException;
import com.nhnacademy.bookpubshop.coupontemplate.repository.CouponTemplateRepository;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.member.repository.MemberRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * CouponService 구현체.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponServiceImpl implements CouponService {
    private final CouponRepository couponRepository;
    private final MemberRepository memberRepository;
    private final CouponTemplateRepository couponTemplateRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void createCoupon(CreateCouponRequestDto createRequestDto) {
        // 수정해야함
        Member member = memberRepository.findById(createRequestDto.getMemberNo())
                .orElseThrow(RuntimeException::new);

        CouponTemplate couponTemplate =
                couponTemplateRepository.findById(createRequestDto.getTemplateNo())
                .orElseThrow(() ->
                        new CouponTemplateNotFoundException(createRequestDto.getTemplateNo()));

        couponRepository.save(new Coupon(null, couponTemplate, null, null,
                member, false, null));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void modifyCouponUsed(ModifyCouponRequestDto modifyRequestDto) {
        Coupon coupon = couponRepository.findById(modifyRequestDto.getCouponNo())
                .orElseThrow(() -> new CouponNotFoundException(modifyRequestDto.getCouponNo()));

        coupon.modifyCouponUsed(modifyRequestDto.isCouponUsed());
        if (coupon.isCouponUsed()) {
            coupon.modifyCouponUsedAt(LocalDateTime.now());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GetCouponResponseDto getCoupon(Long couponNo) {
        return couponRepository.getCoupon(couponNo)
                .orElseThrow(() -> new CouponNotFoundException(couponNo));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetCouponResponseDto> getCoupons(Pageable pageable) {

        return couponRepository.getCoupons(pageable);
    }
}
