package com.nhnacademy.bookpubshop.product.relationship.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.author.entity.Author;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductAuthor;
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
 * 상품저자 레포지토리 테스트
 *
 * @author : 여운석
 * @since : 1.0
 **/
@DataJpaTest
class ProductAuthorRepositoryTest {
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    ProductAuthorRepository productAuthorRepository;

    Author author;
    ProductPolicy productPolicy;
    ProductTypeStateCode productTypeStateCode;
    ProductSaleStateCode productSaleStateCode;
    Product product;
    ProductAuthor productAuthor;

    @BeforeEach
    void setUp() {
        productPolicy = new ProductPolicy(null, "실구매가", true, 5);
        productTypeStateCode = new ProductTypeStateCode(null, "기본", true, "기본입니다.");
        productSaleStateCode = new ProductSaleStateCode(null, "판타지", true, "판타지 소설");
        product = new Product(null, productPolicy, productTypeStateCode, productSaleStateCode, "1231231231", "인어공주",
                100, "인어공주 이야기", "mermaid.png", "mermaid_ebook.pdf", 1000L,
                10, 300L, 3, false, 30, LocalDateTime.now(), LocalDateTime.now(), false);
        author = new Author(null, "사람");

        entityManager.persist(productPolicy);
        entityManager.persist(productTypeStateCode);
        entityManager.persist(productSaleStateCode);
        entityManager.persist(product);
        entityManager.persist(author);

        productAuthor = new ProductAuthor(new ProductAuthor.Pk(1, 1L), author, product);
    }

    @Test
    @DisplayName("상품저자 save 테스트")
    void memberSaveTest() {

        ProductAuthor persist = entityManager.persist(productAuthor);

        Optional<ProductAuthor> result = productAuthorRepository.findById(persist.getPk());

        assertThat(result).isPresent();
        assertThat(result.get().getProduct().getTitle()).isEqualTo(persist.getProduct().getTitle());
        assertThat(result.get().getAuthor().getAuthorName()).isEqualTo(persist.getAuthor().getAuthorName());

    }
}