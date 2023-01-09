package com.nhnacademy.bookpubshop.purchase.repository;

import static com.nhnacademy.bookpubshop.state.ProductSaleState.SALE;
import static com.nhnacademy.bookpubshop.state.ProductTypeState.BEST_SELLER;
import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import com.nhnacademy.bookpubshop.product.relationship.repository.ProductPolicyRepository;
import com.nhnacademy.bookpubshop.product.relationship.repository.ProductSaleStateCodeRepository;
import com.nhnacademy.bookpubshop.product.relationship.repository.ProductTypeStateCodeRepository;
import com.nhnacademy.bookpubshop.product.repository.ProductRepository;
import com.nhnacademy.bookpubshop.purchase.entity.Purchase;
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
 * 매입이력(purchase) 레포지토리 테스트.
 *
 * @author : 박경서
 * @since : 1.0
 **/
@DataJpaTest
class PurchaseRepositoryTest {

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
    PurchaseRepository purchaseRepository;

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
                Collections.EMPTY_LIST, "Isbn:123-1111", "title", 100, "설명",
                "썸네일.png", "eBook path", 20000L,5L,
                10, 1L, 3, false, 100,
                LocalDateTime.now(), LocalDateTime.now(), false);
        productRepository.save(product);
    }

    @Test
    @DisplayName(value = "매입이력(product) 레포지토리 save 테스트")
    void purchaseSaveTest() {
        LocalDateTime time = LocalDateTime.now();
        Purchase purchase = new Purchase(null, product, 1000L, time, 500L);
        purchaseRepository.save(purchase);

        Optional<Purchase> optional = purchaseRepository.findById(purchase.getPurchaseNo());
        assertThat(optional).isPresent();
        assertThat(optional.get().getPurchaseNo()).isEqualTo(purchase.getPurchaseNo());
        assertThat(optional.get().getPurchasePrice()).isEqualTo(purchase.getPurchasePrice());
        assertThat(optional.get().getCreatedAt()).isEqualTo(time);
        assertThat(optional.get().getPurchaseAmount()).isEqualTo(purchase.getPurchaseAmount());

        entityManager.clear();
    }
}