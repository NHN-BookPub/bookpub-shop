package com.nhnacademy.bookpubshop.product.relationship.repository;

import static com.nhnacademy.bookpubshop.state.ProductSaleState.SALE;
import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * 상품판매여부코드(product_sale_state_code) 레포지토리 테스트.
 *
 * @author : 박경서
 * @since : 1.0
 **/
@DataJpaTest
class ProductSaleStateCodeRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    ProductSaleStateCodeRepository productSaleStateCodeRepository;

    @Test
    @DisplayName(value = "상품판매여부코드(product_sale_state_code) 레포지토리 save 테스트")
    void productSaleStateCodeSaveTest() {
        ProductSaleStateCode testProductSaleStateCode = new ProductSaleStateCode(null,
                SALE.getName(), SALE.isUsed(), "이 상품은 판매중입니다.");
        ProductSaleStateCode persist = productSaleStateCodeRepository.save(testProductSaleStateCode);

        Optional<ProductSaleStateCode> optional = productSaleStateCodeRepository.findById(persist.getCodeNumber());
        assertThat(optional).isPresent();
        assertThat(optional.get().getCodeNumber()).isEqualTo(persist.getCodeNumber());
        assertThat(optional.get().getCodeCategory()).isEqualTo(persist.getCodeCategory());
        assertThat(optional.get().isCodeUsed()).isEqualTo(persist.isCodeUsed());
        assertThat(optional.get().getCodeInfo()).isEqualTo(persist.getCodeInfo());

        entityManager.clear();
    }

}