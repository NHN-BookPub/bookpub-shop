package com.nhnacademy.bookpubshop.tier.relationship.service.impl;

import com.nhnacademy.bookpubshop.coupontemplate.entity.CouponTemplate;
import com.nhnacademy.bookpubshop.coupontemplate.exception.CouponTemplateNotFoundException;
import com.nhnacademy.bookpubshop.coupontemplate.repository.CouponTemplateRepository;
import com.nhnacademy.bookpubshop.tier.entity.BookPubTier;
import com.nhnacademy.bookpubshop.tier.exception.TierNotFoundException;
import com.nhnacademy.bookpubshop.tier.relationship.dto.request.CreateTierCouponRequestDto;
import com.nhnacademy.bookpubshop.tier.relationship.dto.response.GetTierCouponResponseDto;
import com.nhnacademy.bookpubshop.tier.relationship.entity.TierCoupon;
import com.nhnacademy.bookpubshop.tier.relationship.entity.TierCoupon.Pk;
import com.nhnacademy.bookpubshop.tier.relationship.exception.NotFoundTierCouponException;
import com.nhnacademy.bookpubshop.tier.relationship.repository.TierCouponRepository;
import com.nhnacademy.bookpubshop.tier.relationship.service.TierCouponService;
import com.nhnacademy.bookpubshop.tier.repository.TierRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * TierCouponService 구현체.
 *
 * @author : 김서현
 * @since : 1.0
 **/
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TierCouponServiceImpl implements TierCouponService {

    private final TierCouponRepository tierCouponRepository;
    private final TierRepository tierRepository;
    private final CouponTemplateRepository couponTemplateRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetTierCouponResponseDto> getTierCoupons(Pageable pageable) {
        return tierCouponRepository.findAllBy(pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void createTierCoupon(CreateTierCouponRequestDto request) {
        BookPubTier bookPubTier = tierRepository.findById(request.getTierNo())
                .orElseThrow(TierNotFoundException::new);
        CouponTemplate couponTemplate = couponTemplateRepository.findById(request.getTemplateNo())
                .orElseThrow(() -> new CouponTemplateNotFoundException(request.getTemplateNo()));
        Pk pk = new Pk(request.getTemplateNo(), request.getTierNo());

        tierCouponRepository.save(new TierCoupon(pk, couponTemplate, bookPubTier));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteTierCoupon(Long templateNo, Integer tierNo) {
        Pk pk = new Pk(templateNo, tierNo);
        if (!tierCouponRepository.existsById(pk)) {
            throw new NotFoundTierCouponException();
        }

        tierCouponRepository.deleteById(pk);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Long> getTierCouponsByTierNo(Integer tierNo) {
        return tierCouponRepository.findAllByTierNo(tierNo);
    }
}
