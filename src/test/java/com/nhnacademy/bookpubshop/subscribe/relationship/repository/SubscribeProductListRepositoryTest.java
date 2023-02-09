package com.nhnacademy.bookpubshop.subscribe.relationship.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.product.dummy.ProductDummy;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductPolicyDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductSaleStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductTypeStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import com.nhnacademy.bookpubshop.subscribe.dummy.SubscribeDummy;
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
        productPolicy = ProductPolicyDummy.dummy();
        productTypeStateCode = ProductTypeStateCodeDummy.dummy();
        productSaleStateCode = ProductSaleStateCodeDummy.dummy();
        product = ProductDummy.dummy(productPolicy, productTypeStateCode, productSaleStateCode);
        subscribe = SubscribeDummy.dummy();

        entityManager.persist(productPolicy);
        entityManager.persist(productTypeStateCode);
        entityManager.persist(productSaleStateCode);
        entityManager.persist(subscribe);
        entityManager.persist(product);

        dateTime = LocalDateTime.of(1999, 10, 22, 13, 30);

        subscribeProductList = new SubscribeProductList(null, subscribe, product);
    }

    @Test
    @DisplayName("멤버 save 테스트")
    void memberSaveTest() {
        SubscribeProductList persist = entityManager.persist(subscribeProductList);

        Optional<SubscribeProductList> subscribeProductList = subscribeProductListRepository.findById(persist.getListNumber());

        assertThat(subscribeProductList).isPresent();
        assertThat(subscribeProductList.get().getListNumber()).isEqualTo(persist.getListNumber());
        assertThat(subscribeProductList.get().getProduct().getProductNo()).isEqualTo(persist.getProduct().getProductNo());
        assertThat(subscribeProductList.get().getSubscribe().getSubscribeNo()).isEqualTo(persist.getSubscribe().getSubscribeNo());

    }
}