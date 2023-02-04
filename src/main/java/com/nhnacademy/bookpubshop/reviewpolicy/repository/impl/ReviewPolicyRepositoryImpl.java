package com.nhnacademy.bookpubshop.reviewpolicy.repository.impl;

import com.nhnacademy.bookpubshop.reviewpolicy.dto.response.GetReviewPolicyResponseDto;
import com.nhnacademy.bookpubshop.reviewpolicy.entity.QReviewPolicy;
import com.nhnacademy.bookpubshop.reviewpolicy.entity.ReviewPolicy;
import com.nhnacademy.bookpubshop.reviewpolicy.repository.ReviewPolicyRepositoryCustom;
import com.querydsl.core.types.Projections;
import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

/**
 * 상품평 정책 레포지토리 구현체입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public class ReviewPolicyRepositoryImpl extends QuerydslRepositorySupport implements ReviewPolicyRepositoryCustom {
    public ReviewPolicyRepositoryImpl() {
        super(ReviewPolicy.class);
    }

    QReviewPolicy reviewPolicy = QReviewPolicy.reviewPolicy;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetReviewPolicyResponseDto> findReviewPolicies() {
        return from(reviewPolicy)
                .select(Projections.constructor(GetReviewPolicyResponseDto.class,
                        reviewPolicy.policyNo,
                        reviewPolicy.sendPoint,
                        reviewPolicy.policyUsed))
                .fetch();
    }
}
