package com.nhnacademy.bookpubshop.couponmonth.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.category.dummy.CategoryDummy;
import com.nhnacademy.bookpubshop.category.entity.Category;
import com.nhnacademy.bookpubshop.couponmonth.dummy.CouponMonthDummy;
import com.nhnacademy.bookpubshop.couponmonth.entity.CouponMonth;
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
 * 이달의쿠폰 Repo Test.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@DataJpaTest
class CouponMonthRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    CouponMonthRepository couponMonthRepository;

    CouponPolicy couponPolicy;
    CouponType couponType;
    CouponStateCode couponStateCode;
    CouponTemplate couponTemplate;
    CouponMonth couponMonth;
    ProductPolicy productPolicy;
    Category category;
    ProductTypeStateCode productTypeStateCode;
    ProductSaleStateCode productSaleStateCode;
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
        couponTemplate = CouponTemplateDummy.dummy(couponPolicy, couponType, product, category, couponStateCode);
        couponMonth = CouponMonthDummy.dummy(couponTemplate);

        entityManager.persist(couponPolicy);
        entityManager.persist(couponType);
        entityManager.persist(couponStateCode);
        entityManager.persist(couponTemplate);
        entityManager.persist(productPolicy);
        entityManager.persist(productTypeStateCode);
        entityManager.persist(productSaleStateCode);
        entityManager.persist(product);
    }

    @Test
    @DisplayName(value = "이달의쿠폰 save 테스트")
    void couponMonthSaveTest() {
        CouponMonth persist = entityManager.persist(couponMonth);
        Optional<CouponMonth> result = couponMonthRepository.findById(persist.getMonthNumber());

        assertThat(result).isPresent();
        assertThat(result.get().getMonthNumber()).isEqualTo(persist.getMonthNumber());
        assertThat(result.get().getCouponTemplate().getTemplateNo()).isEqualTo(persist.getCouponTemplate().getTemplateNo());
        assertThat(result.get().getOpenedAt()).isEqualTo(persist.getOpenedAt());
        assertThat(result.get().getMonthQuantity()).isEqualTo(persist.getMonthQuantity());
    }

    private ProductTypeStateCode productTypeStateCodeDummy() {
        return entityManager.persist(new ProductTypeStateCode(null, "code",
                true, "info"));
    }

    private ProductPolicy productPolicyDummy() {
        return entityManager.persist(new ProductPolicy(null, "test_policy",
                false, 1));
    }

    private ProductSaleStateCode productSaleStateCodeDummy() {
        return entityManager.persist(new ProductSaleStateCode(null, "category",
                true, "info"));
    }

    private Category categoryDummy() {
        Category category = new Category(null, null, "test_categoryName",
                0, true);
        return entityManager.persist(new Category(null, category,
                "test_categoryName", 0, true));
    }

}