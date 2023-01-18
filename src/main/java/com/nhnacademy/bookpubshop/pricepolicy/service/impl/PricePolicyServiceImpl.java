package com.nhnacademy.bookpubshop.pricepolicy.service.impl;

import com.nhnacademy.bookpubshop.pricepolicy.dto.CreatePricePolicyRequestDto;
import com.nhnacademy.bookpubshop.pricepolicy.dto.GetPricePolicyResponseDto;
import com.nhnacademy.bookpubshop.pricepolicy.entity.PricePolicy;
import com.nhnacademy.bookpubshop.pricepolicy.exception.NotFoundPricePolicyException;
import com.nhnacademy.bookpubshop.pricepolicy.repository.PricePolicyRepository;
import com.nhnacademy.bookpubshop.pricepolicy.service.PricePolicyService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 가격정책 서비스의 구현체입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Service
@RequiredArgsConstructor
public class PricePolicyServiceImpl implements PricePolicyService {
    private final PricePolicyRepository pricePolicyRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public void createPricePolicy(CreatePricePolicyRequestDto request) {
        pricePolicyRepository.save(
                new PricePolicy(null, request.getPolicyName(),
                        request.getPolicyFee()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
    public GetPricePolicyResponseDto getPricePolicyById(Integer pricePolicyNo) {
        return pricePolicyRepository.findPolicyByNo(pricePolicyNo)
                        .orElseThrow(NotFoundPricePolicyException::new);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetPricePolicyResponseDto> getPricePolicies() {
        return pricePolicyRepository.findAllPolicies();
    }
}
