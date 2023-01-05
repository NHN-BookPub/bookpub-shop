package com.nhnacademy.bookpubshop.product.relationship.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * 상품카테고리관계(product_and_category) 레포지토리 테스트
 *
 * @author : 박경서
 * @since : 1.0
 **/
@DataJpaTest
class ProductPolicyRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    ProductPolicyRepository productPolicyRepository;

    @Test
    @DisplayName(value = "상품카테고리관계(product_and_category) 레포지토리 save 테스트")
    void productPolicySaveTest() {
        ProductPolicy testProductPolicy = new ProductPolicy(null, "실구매가가기준", true, 5);
        productPolicyRepository.save(testProductPolicy);

        Optional<ProductPolicy> optional = productPolicyRepository.findById(testProductPolicy.getPolicyNo());
        assertThat(optional).isPresent();
        assertThat(optional.get().getPolicyNo()).isEqualTo(testProductPolicy.getPolicyNo());
        assertThat(optional.get().getPolicyMethod()).isEqualTo(testProductPolicy.getPolicyMethod());
        assertThat(optional.get().isPolicySaved()).isEqualTo(testProductPolicy.isPolicySaved());
        assertThat(optional.get().getSaveRate()).isEqualTo(testProductPolicy.getSaveRate());

        entityManager.clear();
    }
}