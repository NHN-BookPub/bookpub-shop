package com.nhnacademy.bookpubshop.pricepolicy.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.pricepolicy.dummy.PricePolicyDummy;
import com.nhnacademy.bookpubshop.pricepolicy.entity.PricePolicy;
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

}