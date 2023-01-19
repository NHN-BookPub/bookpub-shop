package com.nhnacademy.bookpubshop.couponpolicy.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.couponpolicy.dto.response.GetCouponPolicyResponseDto;
import com.nhnacademy.bookpubshop.couponpolicy.dummy.CouponPolicyDummy;
import com.nhnacademy.bookpubshop.couponpolicy.entity.CouponPolicy;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * 쿠폰정책 Repo Test.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@DataJpaTest
class CouponPolicyRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    CouponPolicyRepository couponPolicyRepository;

    CouponPolicy couponPolicy;

    @BeforeEach
    void setUp() {
        couponPolicy = CouponPolicyDummy.dummy();
    }

    @Test
    @DisplayName(value = "쿠폰정책 save 테스트")
    void couponPolicySaveTest() {
        entityManager.persist(couponPolicy);
        entityManager.clear();

        Optional<CouponPolicy> result = couponPolicyRepository.findById(couponPolicy.getPolicyNo());

        assertThat(result).isPresent();
        assertThat(result.get().getPolicyNo()).isEqualTo(couponPolicy.getPolicyNo());
        assertThat(result.get().isPolicyFixed()).isEqualTo(couponPolicy.isPolicyFixed());
        assertThat(result.get().getPolicyPrice()).isEqualTo(couponPolicy.getPolicyPrice());
        assertThat(result.get().getPolicyMinimum()).isEqualTo(couponPolicy.getPolicyMinimum());
        assertThat(result.get().getMaxDiscount()).isEqualTo(couponPolicy.getMaxDiscount());
    }

    @Test
    @DisplayName("쿠폰정책 단건 조회 테스트")
    void findByPolicyNoTest() {
        entityManager.persist(couponPolicy);

        Optional<GetCouponPolicyResponseDto> result = couponPolicyRepository.findByPolicyNo(couponPolicy.getPolicyNo());

        assertThat(result).isPresent();
        assertThat(result.get().getPolicyNo()).isEqualTo(couponPolicy.getPolicyNo());
        assertThat(result.get().isPolicyFixed()).isEqualTo(couponPolicy.isPolicyFixed());
        assertThat(result.get().getPolicyPrice()).isEqualTo(couponPolicy.getPolicyPrice());
        assertThat(result.get().getPolicyMinimum()).isEqualTo(couponPolicy.getPolicyMinimum());
        assertThat(result.get().getMaxDiscount()).isEqualTo(couponPolicy.getMaxDiscount());
    }

    @Test
    @DisplayName("쿠폰정책 리스트 조회 테스트")
    void findByAllTest() {
        entityManager.persist(couponPolicy);

        List<GetCouponPolicyResponseDto> result = couponPolicyRepository.findByAll();

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getPolicyNo()).isEqualTo(couponPolicy.getPolicyNo());
        assertThat(result.get(0).isPolicyFixed()).isEqualTo(couponPolicy.isPolicyFixed());
        assertThat(result.get(0).getPolicyPrice()).isEqualTo(couponPolicy.getPolicyPrice());
        assertThat(result.get(0).getPolicyMinimum()).isEqualTo(couponPolicy.getPolicyMinimum());
        assertThat(result.get(0).getMaxDiscount()).isEqualTo(couponPolicy.getMaxDiscount());
    }
}