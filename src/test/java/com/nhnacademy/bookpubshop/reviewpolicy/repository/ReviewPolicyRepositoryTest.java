package com.nhnacademy.bookpubshop.reviewpolicy.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.reviewpolicy.dto.response.GetReviewPolicyResponseDto;
import com.nhnacademy.bookpubshop.reviewpolicy.dummy.ReviewPolicyDummy;
import com.nhnacademy.bookpubshop.reviewpolicy.entity.ReviewPolicy;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * 상품평정책 레포지토리 테스트.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@DataJpaTest
class ReviewPolicyRepositoryTest {

    @Autowired
    ReviewPolicyRepository reviewPolicyRepository;

    @Autowired
    TestEntityManager entityManager;

    ReviewPolicy reviewPolicy;

    @BeforeEach
    void setUp() {
        reviewPolicy = ReviewPolicyDummy.dummy();
    }

    @Test
    @DisplayName("상품평 정책 저장 테스트")
    void reviewPolicySaveTest() {
        ReviewPolicy persist = entityManager.persist(reviewPolicy);

        Optional<ReviewPolicy> findReviewPolicy = reviewPolicyRepository.findById(persist.getPolicyNo());

        assertThat(findReviewPolicy).isPresent();
        assertThat(findReviewPolicy.get().getSendPoint()).isEqualTo(100);
    }

    @Test
    @DisplayName("상품평 정책 리스트 조회 테스트")
    void findReviewPoliciesTest() {
        ReviewPolicy save = reviewPolicyRepository.save(reviewPolicy);

        List<GetReviewPolicyResponseDto> result = reviewPolicyRepository.findReviewPolicies();

        assertThat(result.get(0).getPolicyNo()).isEqualTo(save.getPolicyNo());
        assertThat(result.get(0).getSendPoint()).isEqualTo(save.getSendPoint());
    }
}