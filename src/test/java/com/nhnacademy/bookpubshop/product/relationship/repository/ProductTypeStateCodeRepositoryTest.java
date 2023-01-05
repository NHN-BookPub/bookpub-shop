package com.nhnacademy.bookpubshop.product.relationship.repository;

import static com.nhnacademy.bookpubshop.state.ProductTypeState.BEST_SELLER;
import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * 상품유형코드(product_type_state_code) 레포지토리 테스트.
 *
 * @author : 박경서
 * @since : 1.0
 **/
@DataJpaTest
class ProductTypeStateCodeRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    ProductTypeStateCodeRepository productTypeStateCodeRepository;

    @Test
    @DisplayName(value = "상품유형코드(product_type_state_code) 레포지토리 save 테스트")
    void productTypeStateCodeSaveTest() {
        ProductTypeStateCode testProductTypeStateCode = new ProductTypeStateCode(null,
                BEST_SELLER.getName(), BEST_SELLER.isUsed(), "이 책은 베스트셀러입니다.");
        productTypeStateCodeRepository.save(testProductTypeStateCode);

        Optional<ProductTypeStateCode> optional = productTypeStateCodeRepository.findById(testProductTypeStateCode.getCodeNo());
        assertThat(optional).isPresent();
        assertThat(optional.get().getCodeNo()).isEqualTo(testProductTypeStateCode.getCodeNo());
        assertThat(optional.get().getCodeName()).isEqualTo(BEST_SELLER.getName());
        assertThat(optional.get().isCodeUsed()).isEqualTo(BEST_SELLER.isUsed());

        entityManager.clear();
    }

}