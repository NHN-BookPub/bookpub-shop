package com.nhnacademy.bookpubshop.coupontemplate.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.category.dummy.CategoryDummy;
import com.nhnacademy.bookpubshop.category.entity.Category;
import com.nhnacademy.bookpubshop.couponpolicy.dummy.CouponPolicyDummy;
import com.nhnacademy.bookpubshop.couponpolicy.entity.CouponPolicy;
import com.nhnacademy.bookpubshop.couponstatecode.dummy.CouponStateCodeDummy;
import com.nhnacademy.bookpubshop.couponstatecode.entity.CouponStateCode;
import com.nhnacademy.bookpubshop.coupontemplate.dummy.CouponTemplateDummy;
import com.nhnacademy.bookpubshop.coupontemplate.entity.CouponTemplate;
import com.nhnacademy.bookpubshop.coupontype.dummy.CouponTypeDummy;
import com.nhnacademy.bookpubshop.coupontype.entity.CouponType;
import com.nhnacademy.bookpubshop.product.dummy.ProductDummy;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductPolicyDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductSaleStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductTypeStateCodeDummy;
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
 * 쿠폰 템플릿 Repo Test.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@DataJpaTest
class CouponTemplateRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    CouponTemplateRepository couponTemplateRepository;

    CouponPolicy couponPolicy;
    CouponType couponType;
    CouponStateCode couponStateCode;
    CouponTemplate couponTemplate;
    ProductPolicy productPolicy;
    ProductTypeStateCode productTypeStateCode;
    ProductSaleStateCode productSaleStateCode;
    Category category;
    Product product;

    @BeforeEach
    void setUp() {
        couponPolicy = CouponPolicyDummy.dummy();
        couponType = CouponTypeDummy.dummy();
        couponStateCode = CouponStateCodeDummy.dummy();
        productPolicy = ProductPolicyDummy.dummy();
        productTypeStateCode = ProductTypeStateCodeDummy.dummy();
        productSaleStateCode = ProductSaleStateCodeDummy.dummy();
        product = ProductDummy.dummy(productPolicy, productTypeStateCode, productSaleStateCode);
        category = CategoryDummy.dummy();
        couponTemplate = CouponTemplateDummy.dummy(couponPolicy, couponType,
                ProductDummy.dummy(productPolicy, productTypeStateCode, productSaleStateCode), category, couponStateCode);
    }

    @Test
    @DisplayName(value = "쿠폰 템플릿 save 테스트")
    void CouponTemplateSaveTest() {
        entityManager.persist(couponPolicy);
        entityManager.persist(couponType);
        entityManager.persist(couponStateCode);
        entityManager.persist(productPolicy);
        entityManager.persist(productTypeStateCode);
        entityManager.persist(productSaleStateCode);
        entityManager.persist(product);
        entityManager.persist(category);
        entityManager.persist(couponTemplate);

        Optional<CouponTemplate> result = couponTemplateRepository.findById(couponTemplate.getTemplateNo());

        assertThat(result).isPresent();
        assertThat(result.get().getTemplateNo()).isEqualTo(couponTemplate.getTemplateNo());
        assertThat(result.get().getCouponPolicy().getPolicyNo()).isEqualTo(couponTemplate.getCouponPolicy().getPolicyNo());
        assertThat(result.get().getCouponType().getTypeNo()).isEqualTo(couponTemplate.getCouponType().getTypeNo());
        assertThat(result.get().getProduct().getProductNo()).isEqualTo(couponTemplate.getProduct().getProductNo());
        assertThat(result.get().getCategory().getCategoryNo()).isEqualTo(couponTemplate.getCategory().getCategoryNo());
        assertThat(result.get().getCouponStateCode().getCodeNo()).isEqualTo(couponTemplate.getCouponStateCode().getCodeNo());
        assertThat(result.get().getTemplateName()).isEqualTo(couponTemplate.getTemplateName());
        assertThat(result.get().getTemplateImage()).isEqualTo(couponTemplate.getTemplateImage());
        assertThat(result.get().getFinishedAt()).isEqualTo(couponTemplate.getFinishedAt());
        assertThat(result.get().getIssuedAt()).isEqualTo(couponTemplate.getIssuedAt());
        assertThat(result.get().isTemplateOverlapped()).isEqualTo(couponTemplate.isTemplateOverlapped());
        assertThat(result.get().isTemplateBundled()).isEqualTo(couponTemplate.isTemplateBundled());
    }

}