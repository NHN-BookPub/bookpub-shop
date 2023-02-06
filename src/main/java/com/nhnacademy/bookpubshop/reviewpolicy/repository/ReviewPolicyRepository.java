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
public interface ReviewPolicyRepository extends JpaRepository<ReviewPolicy, Integer>, ReviewPolicyRepositoryCustom {
    Optional<ReviewPolicy> findByPolicyUsedIsTrue();

    boolean existsByPolicyUsedIsTrue();
}
