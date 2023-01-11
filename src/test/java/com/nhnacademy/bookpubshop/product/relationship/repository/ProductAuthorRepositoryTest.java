package com.nhnacademy.bookpubshop.product.relationship.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.author.dummy.AuthorDummy;
import com.nhnacademy.bookpubshop.author.entity.Author;
import com.nhnacademy.bookpubshop.product.dummy.ProductDummy;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductPolicyDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductSaleStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductTypeStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductAuthor;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
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
        author = AuthorDummy.dummy();
        productPolicy = ProductPolicyDummy.dummy();
        productTypeStateCode = ProductTypeStateCodeDummy.dummy();
        productSaleStateCode = ProductSaleStateCodeDummy.dummy();
        product = ProductDummy.dummy(productPolicy, productTypeStateCode, productSaleStateCode);

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
        ProductAuthor persist = productAuthorRepository.save(productAuthor);

        Optional<ProductAuthor> result = productAuthorRepository.findById(persist.getPk());

        assertThat(result).isPresent();
        assertThat(result.get().getProduct().getTitle()).isEqualTo(persist.getProduct().getTitle());
        assertThat(result.get().getAuthor().getAuthorName()).isEqualTo(persist.getAuthor().getAuthorName());
        assertThat(result.get().getPk().getAuthorNo()).isEqualTo(persist.getAuthor().getAuthorNo());
        assertThat(result.get().getPk().getProductNo()).isEqualTo(persist.getProduct().getProductNo());
        assertThat(result.get().getPk()).isEqualTo(persist.getPk());
    }
}