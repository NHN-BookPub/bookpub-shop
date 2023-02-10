package com.nhnacademy.bookpubshop.product.relationship.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.bookpubshop.product.relationship.dto.GetProductPolicyResponseDto;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import java.util.List;
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
        ProductPolicy persist = productPolicyRepository.save(testProductPolicy);

        Optional<ProductPolicy> optional = productPolicyRepository.findById(persist.getPolicyNo());
        assertThat(optional).isPresent();
        assertThat(optional.get().getPolicyNo()).isEqualTo(persist.getPolicyNo());
        assertThat(optional.get().getPolicyMethod()).isEqualTo(persist.getPolicyMethod());
        assertThat(optional.get().isPolicySaved()).isEqualTo(persist.isPolicySaved());
        assertThat(optional.get().getSaveRate()).isEqualTo(persist.getSaveRate());

        entityManager.clear();
    }

    @Test
    @DisplayName("모든 정책 조회 테스트")
    void findAllPolicies() {
        // given
        ProductPolicy testProductPolicy = new ProductPolicy(null, "실구매가가기준", true, 5);
        ProductPolicy persist = entityManager.persist(testProductPolicy);
        GetProductPolicyResponseDto dto =
                new GetProductPolicyResponseDto(
                        persist.getPolicyNo(),
                        persist.getPolicyMethod(),
                        persist.isPolicySaved(),
                        persist.getSaveRate());
        List<GetProductPolicyResponseDto> list = List.of(dto);

        // when
        List<GetProductPolicyResponseDto> result = productPolicyRepository.findAllPolicies();

        // then
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getPolicyNo()).isEqualTo(persist.getPolicyNo());
        assertThat(result.get(0).getPolicyMethod()).isEqualTo(persist.getPolicyMethod());
        assertThat(result.get(0).isPolicySaved()).isEqualTo(persist.isPolicySaved());
        assertThat(result.get(0).getSaveRate()).isEqualTo(persist.getSaveRate());
    }
}