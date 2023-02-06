package com.nhnacademy.bookpubshop.reviewpolicy.repository;

import com.nhnacademy.bookpubshop.reviewpolicy.entity.ReviewPolicy;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 리뷰정책 레포지토리.
 *
 * @author : 임태원
 * @since : 1.0
 **/
public interface ReviewPolicyRepository extends JpaRepository<ReviewPolicy, Integer>,
        ReviewPolicyRepositoryCustom {
    /**
     * 상품평정책 사용여부가 참인 상품평 정책을 찾는 메서드입니다.
     *
     * @return the optional
     */
    Optional<ReviewPolicy> findByPolicyUsedIsTrue();

    /**
     * 상품평정책 사용여부가 참인 상품평 정책이 있는지 확인하는 메서드입니다.
     *
     * @return the boolean
     */
    boolean existsByPolicyUsedIsTrue();
}
