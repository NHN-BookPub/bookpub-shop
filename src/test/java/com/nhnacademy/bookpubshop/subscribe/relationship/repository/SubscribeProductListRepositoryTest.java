package com.nhnacademy.bookpubshop.subscribe.relationship.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import com.nhnacademy.bookpubshop.subscribe.entity.Subscribe;
import com.nhnacademy.bookpubshop.subscribe.relationship.entity.SubscribeProductList;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * 구독상품리스트 레포지토리 테스트.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@DataJpaTest
class SubscribeProductListRepositoryTest {
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    SubscribeProductListRepository subscribeProductListRepository;

    SubscribeProductList subscribeProductList;
    Subscribe subscribe;
    Product product;
    ProductPolicy productPolicy;
    ProductTypeStateCode productTypeStateCode;
    ProductSaleStateCode productSaleStateCode;

    LocalDateTime dateTime;

    @BeforeEach
    void setUp() {
        productPolicy = new ProductPolicy(null, "실구매가", true, 5);
        productTypeStateCode = new ProductTypeStateCode(null, "기본", true, "기본입니다.");
        productSaleStateCode = new ProductSaleStateCode(null, "판타지", true, "판타지 소설");
        subscribe =  new Subscribe(null, "좋은생각", 80000L, 100000L,
                20, 100L, false, LocalDateTime.now(), true);
        product = new Product(null, productPolicy, productTypeStateCode, productSaleStateCode, "1231231231", "인어공주",
                100, "인어공주 이야기", "mermaid.png", "mermaid_ebook.pdf", 1000L,
                10, 300L, 3, false, 30, LocalDateTime.now(), LocalDateTime.now(), false);

        entityManager.persist(productPolicy);
        entityManager.persist(productTypeStateCode);
        entityManager.persist(productSaleStateCode);
        entityManager.persist(subscribe);
        entityManager.persist(product);

        dateTime = LocalDateTime.of(1999, 10, 22, 13, 30);

        subscribeProductList = new SubscribeProductList(null, subscribe, product, dateTime);
    }

    @Test
    @DisplayName("멤버 save 테스트")
    void memberSaveTest() {
        SubscribeProductList persist = entityManager.persist(subscribeProductList);

        Optional<SubscribeProductList> subscribeProductList = subscribeProductListRepository.findById(persist.getListNumber());

        assertThat(subscribeProductList).isPresent();
        assertThat(subscribeProductList.get().getListNumber()).isEqualTo(persist.getListNumber());
        assertThat(subscribeProductList.get().getPublishedAt()).isEqualTo(persist.getPublishedAt());

    }
}