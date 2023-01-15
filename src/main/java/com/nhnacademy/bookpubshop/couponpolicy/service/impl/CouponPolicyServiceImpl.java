package com.nhnacademy.bookpubshop.couponpolicy.service.impl;

import com.nhnacademy.bookpubshop.couponpolicy.dto.request.CreateCouponPolicyRequestDto;
import com.nhnacademy.bookpubshop.couponpolicy.dto.request.ModifyCouponPolicyRequestDto;
import com.nhnacademy.bookpubshop.couponpolicy.dto.response.GetCouponPolicyResponseDto;
import com.nhnacademy.bookpubshop.couponpolicy.entity.CouponPolicy;
import com.nhnacademy.bookpubshop.couponpolicy.exception.CouponPolicyNotFoundException;
import com.nhnacademy.bookpubshop.couponpolicy.repository.CouponPolicyRepository;
import com.nhnacademy.bookpubshop.couponpolicy.service.CouponPolicyService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 쿠폰정책 서비스 구현체입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CouponPolicyServiceImpl implements CouponPolicyService {
    private final CouponPolicyRepository couponPolicyRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void addCouponPolicy(CreateCouponPolicyRequestDto createCouponPolicyRequestDto) {
        couponPolicyRepository.save(new CouponPolicy(
                null,
                createCouponPolicyRequestDto.isPolicyFixed(),
                createCouponPolicyRequestDto.getPolicyPrice(),
                createCouponPolicyRequestDto.getPolicyMinimum(),
                createCouponPolicyRequestDto.getMaxDiscount()
        ));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void modifyCouponPolicy(ModifyCouponPolicyRequestDto request) {
        CouponPolicy couponPolicy = couponPolicyRepository.findById(request.getPolicyNo())
                .orElseThrow(() -> new CouponPolicyNotFoundException(request.getPolicyNo()));

        couponPolicy.modifyCouponPolicy(request.isPolicyFixed(), request.getPolicyPrice(),
                request.getPolicyMinimum(), request.getMaxDiscount());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GetCouponPolicyResponseDto getCouponPolicy(Integer policyNo) {
        return couponPolicyRepository.findByPolicyNo(policyNo)
                .orElseThrow(() -> new CouponPolicyNotFoundException(policyNo));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetCouponPolicyResponseDto> getCouponPolicies() {
        return couponPolicyRepository.findByAll();
    }
}
