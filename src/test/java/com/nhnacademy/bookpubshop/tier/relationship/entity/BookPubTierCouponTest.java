package com.nhnacademy.bookpubshop.tier.relationship.entity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import com.nhnacademy.bookpubshop.category.entity.Category;
import com.nhnacademy.bookpubshop.couponpolicy.dummy.CouponPolicyDummy;
import com.nhnacademy.bookpubshop.couponstatecode.dummy.CouponStateCodeDummy;
import com.nhnacademy.bookpubshop.coupontemplate.dummy.CouponTemplateDummy;
import com.nhnacademy.bookpubshop.coupontemplate.entity.CouponTemplate;
import com.nhnacademy.bookpubshop.coupontype.dummy.CouponTypeDummy;
import com.nhnacademy.bookpubshop.product.dummy.ProductDummy;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductPolicyDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductSaleStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductTypeStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import com.nhnacademy.bookpubshop.tier.entity.BookPubTier;
import com.nhnacademy.bookpubshop.tier.relationship.repository.TierCouponRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * Tier_Coupon 테스트 입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
@DataJpaTest
class BookPubTierCouponTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TierCouponRepository tierCouponRepository;

    CouponTemplate couponTemplate;

    Product product;

    BookPubTier bookPubTier;
    ProductPolicy productPolicy;
    ProductTypeStateCode productTypeStateCode;
    ProductSaleStateCode productSaleStateCode;

    @BeforeEach
    void setUp() {
        productPolicy = ProductPolicyDummy.dummy();
        productTypeStateCode = ProductTypeStateCodeDummy.dummy();
        productSaleStateCode = ProductSaleStateCodeDummy.dummy();
        product = ProductDummy.dummy(productPolicy, productTypeStateCode, productSaleStateCode);

        bookPubTier = new BookPubTier("null", 1,1L,1000L);
        couponTemplate = CouponTemplateDummy.dummy(CouponPolicyDummy.dummy(),
                CouponTypeDummy.dummy(), product,
                new Category(null, null,
                        "name", 1, true),
                CouponStateCodeDummy.dummy());
    }

    @DisplayName("등급과 쿠폰의 연관관계에대한 테이블의 세이브테스트")
    @Test
    void TierCouponSaveTest() {
        entityManager.persist(bookPubTier);
        entityManager.persist(product.getProductSaleStateCode());
        entityManager.persist(product.getProductTypeStateCode());
        entityManager.persist(product.getProductPolicy());
        entityManager.persist(product);

        entityManager.persist(couponTemplate.getCouponType());
        entityManager.persist(couponTemplate.getCouponPolicy());
        entityManager.persist(couponTemplate.getCouponStateCode());
        entityManager.persist(couponTemplate.getCategory());
        entityManager.persist(couponTemplate);

        TierCoupon tierCoupon = new TierCoupon(new TierCoupon.Pk(couponTemplate.getTemplateNo(),
                bookPubTier.getTierNo()),
                couponTemplate,
                bookPubTier);
        TierCoupon persist = tierCouponRepository.save(tierCoupon);

        Optional<TierCoupon> result = tierCouponRepository.findById(persist.getPk());

        assertThat(result).isPresent();
        assertThat(result.get().getBookPubTier()).isEqualTo(persist.getBookPubTier());
        assertThat(result.get().getCouponTemplate()).isEqualTo(persist.getCouponTemplate());
        assertThat(result.get().getPk()).isEqualTo(persist.getPk());
        assertThat(result.get().getPk()).isEqualTo(persist.getPk());
    }

}