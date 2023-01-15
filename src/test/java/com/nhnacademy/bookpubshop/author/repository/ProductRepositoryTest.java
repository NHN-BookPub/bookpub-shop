package com.nhnacademy.bookpubshop.author.repository;

import static com.nhnacademy.bookpubshop.state.ProductTypeState.BEST_SELLER;
import static com.nhnacademy.bookpubshop.state.ProductTypeState.NEW;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import com.nhnacademy.bookpubshop.product.repository.ProductRepository;
import java.time.LocalDateTime;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Pageable;

/**
 * Some description here.
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
    Product productPersist;
    ProductPolicy policyPersist;
    ProductTypeStateCode typePersist;
    ProductSaleStateCode salePersist;
    @BeforeEach
    void setUp() {
        ProductPolicy productPolicy = new ProductPolicy(null,"method",true,1);
        ProductTypeStateCode typeStateCode = new ProductTypeStateCode(null,BEST_SELLER.getName(),BEST_SELLER.isUsed(),"info");
        ProductSaleStateCode saleStateCode = new ProductSaleStateCode(null, NEW.getName(),NEW.isUsed(),"info");

        policyPersist = entityManager.persist(productPolicy);
        typePersist = entityManager.persist(typeStateCode);
        salePersist = entityManager.persist(saleStateCode);

        Product product = new Product(null,
                policyPersist,
                typePersist,
                salePersist,
                Collections.EMPTY_LIST,
                "1231231233",
                "test",
                "publisher",
                130,
                "test_description",
                "thumbnail.png",
                "test.txt",
                10000L,
                10000L,
                0,
                0L,
                10,
                false,
                100,
                LocalDateTime.now(),
                false);

        productPersist = entityManager.persist(product);
    }

    @Test
    @DisplayName("전체 상품 조회")
    void getAllProducts() {
        Pageable pageable = Pageable.ofSize(5);

        assertThat(productRepository.getAllProducts(pageable).getPageable().getPageSize())
                .isEqualTo(pageable.getPageSize());
        assertThat(productRepository.getAllProducts(pageable).getContent().get(0).getProductNo())
                .isEqualTo(productPersist.getProductNo());
        assertThat(productRepository.getAllProducts(pageable).getContent().get(0).getProductStock())
                .isEqualTo(productPersist.getProductStock());
        assertThat(productRepository.getAllProducts(pageable).getContent().get(0).getSaleRate())
                .isEqualTo(productPersist.getSalesRate());
        assertThat(productRepository.getAllProducts(pageable).getContent().get(0).getTitle())
                .isEqualTo(productPersist.getTitle());
        assertThat(productRepository.getAllProducts(pageable).getContent().get(0).getSalesPrice())
                .isEqualTo(productPersist.getSalesPrice());
        assertThat(productRepository.getAllProducts(pageable).getContent().get(0).getThumbnailPath())
                .isEqualTo(productPersist.getProductThumbnail());
    }

    @Test
    @DisplayName("제목 검색 조회")
    void getProductListLikeTitle() {
        Pageable pageable = Pageable.ofSize(5);
        String searchWord = "test";

        assertThat(productRepository.getProductListLikeTitle(searchWord, pageable)
                .getContent().get(0).getProductNo())
                .isEqualTo(productPersist.getProductNo());
    }
}