package com.nhnacademy.bookpubshop.reviewpolicy.service.impl;

import com.nhnacademy.bookpubshop.reviewpolicy.dto.request.CreateReviewPolicyRequestDto;
import com.nhnacademy.bookpubshop.reviewpolicy.dto.request.ModifyPointReviewPolicyRequestDto;
import com.nhnacademy.bookpubshop.reviewpolicy.dto.response.GetReviewPolicyResponseDto;
import com.nhnacademy.bookpubshop.reviewpolicy.entity.ReviewPolicy;
import com.nhnacademy.bookpubshop.reviewpolicy.exception.ReviewPolicyNotFoundException;
import com.nhnacademy.bookpubshop.reviewpolicy.repository.ReviewPolicyRepository;
import com.nhnacademy.bookpubshop.reviewpolicy.service.ReviewPolicyService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 상품평정책을 다루기 위한 서비스 구현체입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewPolicyServiceImpl implements ReviewPolicyService {
    private final ReviewPolicyRepository reviewPolicyRepository;

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void createReviewPolicy(CreateReviewPolicyRequestDto createRequestDto) {
        reviewPolicyRepository.save(ReviewPolicy.builder()
                .sendPoint(createRequestDto.getSendPoint())
                .build());
    }

    /**
     * {@inheritDoc}
     *
     * @throws ReviewPolicyNotFoundException 상품평 정책이 존재하지 않을 때 발생하는 예외
     */
    @Transactional
    @Override
    public void modifyReviewPolicy(ModifyPointReviewPolicyRequestDto modifyRequestDto) {
        ReviewPolicy reviewPolicy = reviewPolicyRepository.findById(modifyRequestDto.getPolicyNo())
                .orElseThrow(() -> new ReviewPolicyNotFoundException(modifyRequestDto.getPolicyNo()));

        reviewPolicy.modifySendPoint(modifyRequestDto.getSendPoint());
    }

    /**
     * {@inheritDoc}
     *
     * @throws ReviewPolicyNotFoundException 상품평 정책이 존재하지 않을 때 발생하는 에러
     */
    @Override
    @Transactional
    public void modifyUsedReviewPolicy(Integer policyNo) {
        if (reviewPolicyRepository.existsByPolicyUsedIsTrue()) {
            ReviewPolicy usedReviewPolicy = reviewPolicyRepository.findByPolicyUsedIsTrue()
                    .orElseThrow(() -> new ReviewPolicyNotFoundException(policyNo));
            usedReviewPolicy.modifyUsed(false);
        }

        ReviewPolicy reviewPolicy = reviewPolicyRepository.findById(policyNo)
                .orElseThrow(() -> new ReviewPolicyNotFoundException(policyNo));

        reviewPolicy.modifyUsed(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetReviewPolicyResponseDto> getReviewPolicies() {
        return reviewPolicyRepository.findReviewPolicies();
    }
}
