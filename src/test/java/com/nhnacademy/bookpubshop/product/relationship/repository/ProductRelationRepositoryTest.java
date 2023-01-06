package com.nhnacademy.bookpubshop.product.relationship.repository;

import static com.nhnacademy.bookpubshop.state.ProductSaleState.SALE;
import static com.nhnacademy.bookpubshop.state.ProductTypeState.BEST_SELLER;
import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductRelation;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import com.nhnacademy.bookpubshop.product.repository.ProductRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * 연관상품(product_relation) 레포지토리 테스트.
 *
 * @author : 박경서
 * @since : 1.0
 **/
@DataJpaTest
class ProductRelationRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductPolicyRepository productPolicyRepository;

    @Autowired
    ProductTypeStateCodeRepository productTypeStateCodeRepository;

    @Autowired
    ProductSaleStateCodeRepository productSaleStateCodeRepository;

    @Autowired
    ProductRelationRepository productRelationRepository;

    ProductPolicy productPolicy;
    ProductTypeStateCode productTypeStateCode;
    ProductSaleStateCode productSaleStateCode;
    Product product;

    @BeforeEach
    void setUp() {
        productPolicy = new ProductPolicy(null, "실구매가가기준", true, 5);
        productPolicyRepository.save(productPolicy);

        productTypeStateCode = new ProductTypeStateCode(null,
                BEST_SELLER.getName(), BEST_SELLER.isUsed(), "이 책은 베스트셀러입니다.");
        productTypeStateCodeRepository.save(productTypeStateCode);

        productSaleStateCode = new ProductSaleStateCode(null,
                SALE.getName(), SALE.isUsed(), "이 상품은 판매중입니다.");
        productSaleStateCodeRepository.save(productSaleStateCode);

        product = new Product(null, productPolicy, productTypeStateCode, productSaleStateCode,
                "Isbn:123-1111", "title", 100, "설명",
                "썸네일.png", "eBook path", 20000L,5,
                300L, 1, false, 100,
                LocalDateTime.now(), LocalDateTime.now(), false);
        productRepository.save(product);
    }

    @Test
    @DisplayName(value = "연관상품(product_relation) 레포지토리 save 테스트")
    void productRelationSaveTest() {
        ProductRelation testProductRelation = new ProductRelation(null, product, false);
        productRelationRepository.save(testProductRelation);

        Optional<ProductRelation> optional = productRelationRepository.findById(testProductRelation.getProductRelationNo());
        assertThat(optional).isPresent();
        assertThat(optional.get().getProductRelationNo()).isEqualTo(testProductRelation.getProductRelationNo());
        assertThat(optional.get().isRelationDeleted()).isFalse();

        entityManager.clear();
    }

}