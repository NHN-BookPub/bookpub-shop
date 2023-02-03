package com.nhnacademy.bookpubshop.pricepolicy.service.impl;

import com.nhnacademy.bookpubshop.pricepolicy.dto.request.CreatePricePolicyRequestDto;
import com.nhnacademy.bookpubshop.pricepolicy.dto.response.GetOrderPolicyResponseDto;
import com.nhnacademy.bookpubshop.pricepolicy.dto.response.GetPricePolicyResponseDto;
import com.nhnacademy.bookpubshop.pricepolicy.entity.PricePolicy;
import com.nhnacademy.bookpubshop.pricepolicy.exception.NotFoundPricePolicyException;
import com.nhnacademy.bookpubshop.pricepolicy.repository.PricePolicyRepository;
import com.nhnacademy.bookpubshop.pricepolicy.service.PricePolicyService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 가격정책 서비스의 구현체입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PricePolicyServiceImpl implements PricePolicyService {
    private final PricePolicyRepository pricePolicyRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void createPricePolicy(CreatePricePolicyRequestDto request) {
        pricePolicyRepository.save(
                new PricePolicy(null, request.getPolicyName(),
                        request.getPolicyFee()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void modifyPricePolicyFee(Integer pricePolicyNo, Long fee) {
        PricePolicy policy = pricePolicyRepository.findById(pricePolicyNo)
                .orElseThrow(NotFoundPricePolicyException::new);

        policy.modifyFee(fee);

        pricePolicyRepository.save(policy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetPricePolicyResponseDto> getPricePoliciesByName(String pricePolicyName) {
        return pricePolicyRepository.getPricePolicyByName(pricePolicyName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetPricePolicyResponseDto> getPricePolicies() {
        return pricePolicyRepository.findAllPolicies();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PricePolicy getLatestPricePolicyByName(String name) {
        return pricePolicyRepository.getLatestPricePolicyByName(name)
                .orElseThrow(NotFoundPricePolicyException::new);
    }

    @Override
    public List<GetOrderPolicyResponseDto> getOrderRequestPolicy() {
        return pricePolicyRepository.getShipAndPackagePolicy();
    }


}
