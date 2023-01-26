package com.nhnacademy.bookpubshop.pricepolicy.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.bookpubshop.pricepolicy.dto.GetPricePolicyResponseDto;
import com.nhnacademy.bookpubshop.pricepolicy.dummy.PricePolicyDummy;
import com.nhnacademy.bookpubshop.pricepolicy.entity.PricePolicy;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * 가격정책 Repo Test 입니다.
 *
 * @author : 김서현
 * @since : 1.0
 **/
@DataJpaTest
class PricePolicyRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    PricePolicyRepository pricePolicyRepository;

    PricePolicy pricePolicy;

    @BeforeEach
    void setUp() {
        pricePolicy = PricePolicyDummy.dummy();

    }

    @Test
    @DisplayName(value = "가격정책 save 테스트")
    void pricePolicySaveTest() {
        entityManager.persist(pricePolicy);
        entityManager.clear();

        Optional<PricePolicy> result = pricePolicyRepository.findById(pricePolicy.getPolicyNo());

        assertThat(result).isPresent();
        assertThat(result.get().getPolicyNo()).isEqualTo(pricePolicy.getPolicyNo());
        assertThat(result.get().getPolicyName()).isEqualTo(pricePolicy.getPolicyName());
        assertThat(result.get().getPolicyFee()).isEqualTo(pricePolicy.getPolicyFee());
    }

    @Test
    @DisplayName("정책 단건 반환 태스트")
    void findPolicy() {
        // given
        PricePolicy persist = entityManager.persist(pricePolicy);

        // when
        Optional<GetPricePolicyResponseDto> policy = pricePolicyRepository.findPolicyByNo(persist.getPolicyNo());

        // then
        assertThat(policy).isPresent();
        assertThat(policy.get().getPolicyNo()).isEqualTo(persist.getPolicyNo());
        assertThat(policy.get().getPolicyName()).isEqualTo(persist.getPolicyName());
        assertThat(policy.get().getPolicyFee()).isEqualTo(persist.getPolicyFee());
    }

    @Test
    @DisplayName("전체 정책 조회 테스트")
    void findAllPolicies() {
        // given
        PricePolicy persist = entityManager.persist(pricePolicy);

        // when
        List<GetPricePolicyResponseDto> policies = pricePolicyRepository.findAllPolicies();

        // then
        assertThat(policies).isNotEmpty();
        assertThat(policies.get(0).getPolicyNo()).isEqualTo(persist.getPolicyNo());
        assertThat(policies.get(0).getPolicyName()).isEqualTo(persist.getPolicyName());
        assertThat(policies.get(0).getPolicyFee()).isEqualTo(persist.getPolicyFee());
    }

    @Test
    @DisplayName("최근 정책 조회 테스트")
    void getLatestPolicy_Test() {
        // given
        PricePolicy persist = entityManager.persist(pricePolicy);

        // when
        Optional<PricePolicy> latestPricePolicyByName = pricePolicyRepository.getLatestPricePolicyByName(persist.getPolicyName());

        // then
        assertThat(latestPricePolicyByName).isPresent();
        assertThat(latestPricePolicyByName.get().getPolicyName()).isEqualTo(persist.getPolicyName());
        assertThat(latestPricePolicyByName.get().getPolicyNo()).isEqualTo(persist.getPolicyNo());
        assertThat(latestPricePolicyByName.get().getPolicyFee()).isEqualTo(persist.getPolicyFee());
    }

}