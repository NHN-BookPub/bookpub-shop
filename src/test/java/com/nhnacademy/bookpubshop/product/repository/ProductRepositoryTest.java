package com.nhnacademy.bookpubshop.product.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.product.dummy.ProductDummy;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductPolicyDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductSaleStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductTypeStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * 상품 레포지토리 테스트.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@DataJpaTest
class ProductRepositoryTest {
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    ProductRepository productRepository;

    Product product;
    ProductPolicy productPolicy;
    ProductTypeStateCode productTypeStateCode;
    ProductSaleStateCode productSaleStateCode;

    @BeforeEach
    void setUp() {
        productPolicy = ProductPolicyDummy.dummy();
        productTypeStateCode = ProductTypeStateCodeDummy.dummy();
        productSaleStateCode = ProductSaleStateCodeDummy.dummy();

        entityManager.persist(productPolicy);
        entityManager.persist(productTypeStateCode);
        entityManager.persist(productSaleStateCode);

        product = ProductDummy.dummy(productPolicy, productTypeStateCode, productSaleStateCode);

    }

    @Test
    @DisplayName("상품 save 테스트")
    void productSaveTest() {
        LocalDateTime now = LocalDateTime.now();

        Product persist = entityManager.persist(product);

        Optional<Product> product = productRepository.findById(persist.getProductNo());
        assertThat(product).isPresent();
        assertThat(product.get().getProductPolicy().getPolicyNo()).isEqualTo(persist.getProductPolicy().getPolicyNo());
        assertThat(product.get().getRelationProduct()).isEqualTo(persist.getRelationProduct());
        assertThat(product.get().getSalesRate()).isEqualTo(persist.getSalesRate());
        assertThat(product.get().getViewCount()).isEqualTo(persist.getViewCount());
        assertThat(product.get().isProductDeleted()).isFalse();
        assertThat(product.get().isProductSubscribed()).isTrue();
        assertThat(product.get().getProductNo()).isEqualTo(persist.getProductNo());
        assertThat(product.get().getProductIsbn()).isEqualTo(persist.getProductIsbn());
        assertThat(product.get().getProductDescription()).isEqualTo(persist.getProductDescription());
        assertThat(product.get().getProductPriority()).isEqualTo(persist.getProductPriority());
        assertThat(product.get().getProductStock()).isEqualTo(persist.getProductStock());
        assertThat(product.get().getSalesPrice()).isEqualTo(persist.getSalesPrice());
        assertThat(product.get().getPageCount()).isEqualTo(persist.getPageCount());
        assertThat(product.get().getCreatedAt()).isAfter(now);
    }
}