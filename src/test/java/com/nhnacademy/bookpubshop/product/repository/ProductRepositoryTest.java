package com.nhnacademy.bookpubshop.product.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import java.time.LocalDateTime;
import java.util.Collections;
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
        productPolicy = new ProductPolicy(null, "실구매가", true, 5);
        productTypeStateCode = new ProductTypeStateCode(null, "기본", true, "기본입니다.");
        productSaleStateCode = new ProductSaleStateCode(null, "판타지", true, "판타지 소설");

        entityManager.persist(productPolicy);
        entityManager.persist(productTypeStateCode);
        entityManager.persist(productSaleStateCode);

        product = new Product(null, productPolicy, productTypeStateCode, productSaleStateCode, Collections.EMPTY_LIST, "1231231231", "인어공주",
                "publisher", 100, "인어공주 이야기", "mermaid.png", "mermaid_ebook.pdf", 1000L,
                100L, 10, 3L, 3, false, 30, LocalDateTime.now(), LocalDateTime.now(), false);
    }

    @Test
    @DisplayName("상품 save 테스트")
    void productSaveTest() {
        Product persist = entityManager.persist(product);
        entityManager.clear();

        Optional<Product> product = productRepository.findById(persist.getProductNo());
        assertThat(product).isPresent();
        assertThat(product.get().getProductNo()).isEqualTo(persist.getProductNo());
        assertThat(product.get().getProductIsbn()).isEqualTo(persist.getProductIsbn());
        assertThat(product.get().getProductDescription()).isEqualTo(persist.getProductDescription());
        assertThat(product.get().getProductPriority()).isEqualTo(persist.getProductPriority());
        assertThat(product.get().getProductStock()).isEqualTo(persist.getProductStock());
        assertThat(product.get().getProductThumbnail()).isEqualTo(persist.getProductThumbnail());
        assertThat(product.get().getSalesPrice()).isEqualTo(persist.getSalesPrice());
        assertThat(product.get().getCreatedAt()).isEqualTo(persist.getCreatedAt());
        assertThat(product.get().getEbookFilePath()).isEqualTo(persist.getEbookFilePath());
        assertThat(product.get().getPageCount()).isEqualTo(persist.getPageCount());
    }
}