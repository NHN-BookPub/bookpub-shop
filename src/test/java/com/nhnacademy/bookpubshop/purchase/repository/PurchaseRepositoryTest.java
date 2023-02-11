package com.nhnacademy.bookpubshop.purchase.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductPolicyDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductSaleStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductTypeStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import com.nhnacademy.bookpubshop.product.relationship.repository.ProductPolicyRepository;
import com.nhnacademy.bookpubshop.product.relationship.repository.ProductSaleStateCodeRepository;
import com.nhnacademy.bookpubshop.product.relationship.repository.ProductTypeStateCodeRepository;
import com.nhnacademy.bookpubshop.product.repository.ProductRepository;
import com.nhnacademy.bookpubshop.purchase.dto.GetPurchaseListResponseDto;
import com.nhnacademy.bookpubshop.purchase.dummy.PurchaseDummy;
import com.nhnacademy.bookpubshop.purchase.entity.Purchase;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
    Purchase purchase;

    @BeforeEach
    void setUp() {
        productPolicy = ProductPolicyDummy.dummy();
        productTypeStateCode = ProductTypeStateCodeDummy.dummy();
        productSaleStateCode = ProductSaleStateCodeDummy.dummy();

        productPolicy = entityManager.persist(productPolicy);
        productTypeStateCode = entityManager.persist(productTypeStateCode);
        productSaleStateCode = entityManager.persist(productSaleStateCode);

        product = new Product(null,
                productPolicy,
                productTypeStateCode,
                productSaleStateCode,
                null,
                "1231231233",
                "test",
                "publisher",
                130,
                "test_description",
                10000L,
                10000L,
                0,
                0L,
                10,
                false,
                100,
                LocalDateTime.now(),
                false);

        product = entityManager.persist(product);

        purchase = PurchaseDummy.dummy(product);
    }

    @Test
    @DisplayName(value = "매입이력(product) 레포지토리 save 테스트")
    void purchaseSaveTest() {
        LocalDateTime now = LocalDateTime.now();
        Purchase persist = entityManager.persist(purchase);

        Optional<Purchase> optional = purchaseRepository.findById(persist.getPurchaseNo());
        assertThat(optional).isPresent();
        assertThat(optional.get().getPurchaseNo()).isEqualTo(persist.getPurchaseNo());
        assertThat(optional.get().getPurchasePrice()).isEqualTo(persist.getPurchasePrice());
        assertThat(optional.get().getPurchaseAmount()).isEqualTo(persist.getPurchaseAmount());
        assertThat(optional.get().getCreatedAt()).isAfter(now);
    }

    @Test
    @DisplayName("상품 번호로 조회 성공")
    void findByProductNumberWithPageTest() {
        Purchase persist = entityManager.persist(purchase);

        Pageable pageable = Pageable.ofSize(10);

        Page<GetPurchaseListResponseDto> returns =
                purchaseRepository.findByProductNumberWithPage(persist.getProduct().getProductNo(), pageable);

        assertThat(returns.getContent().get(0).getProductNo()).isEqualTo(persist.getProduct().getProductNo());
        assertThat(returns.getContent().get(0).getPurchaseAmount()).isEqualTo(persist.getPurchaseAmount());
        assertThat(returns.getContent().get(0).getPurchasePrice()).isEqualTo(persist.getPurchasePrice());
        assertThat(returns.getContent().get(0).getProductNo()).isEqualTo(persist.getProduct().getProductNo());
    }

    @Test
    @DisplayName("최근순 매입 이력 조회 성공")
    void getPurchaseListDescTest() {
        Purchase persist = entityManager.persist(purchase);

        Pageable pageable = Pageable.ofSize(10);

        Page<GetPurchaseListResponseDto> returns =
                purchaseRepository.getPurchaseListDesc(pageable);

        assertThat(returns.getContent().get(0).getProductNo())
                .isEqualTo(persist.getProduct().getProductNo());
        assertThat(returns.getContent().get(0).getPurchaseNo())
                .isEqualTo(persist.getPurchaseNo());
        assertThat(returns.getContent().get(0).getPurchaseAmount())
                .isEqualTo(persist.getPurchaseAmount());
        assertThat(returns.getContent().get(0).getPurchasePrice())
                .isEqualTo(persist.getPurchasePrice());
    }


}