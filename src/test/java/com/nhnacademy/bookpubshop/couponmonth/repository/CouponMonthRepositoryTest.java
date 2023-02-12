package com.nhnacademy.bookpubshop.couponmonth.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.bookpubshop.category.dummy.CategoryDummy;
import com.nhnacademy.bookpubshop.category.entity.Category;
import com.nhnacademy.bookpubshop.couponmonth.dto.response.GetCouponMonthResponseDto;
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
import com.nhnacademy.bookpubshop.file.dummy.FileDummy;
import com.nhnacademy.bookpubshop.file.entity.File;
import com.nhnacademy.bookpubshop.product.dummy.ProductDummy;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductPolicyDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductSaleStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductTypeStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import java.util.List;
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
    File file;

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

        file = FileDummy.dummy(null, null, couponTemplate, product, null,null);

        entityManager.persist(category);
        entityManager.persist(couponPolicy);
        entityManager.persist(couponType);
        entityManager.persist(couponStateCode);
        entityManager.persist(couponTemplate);
        entityManager.persist(productPolicy);
        entityManager.persist(productTypeStateCode);
        entityManager.persist(productSaleStateCode);
        entityManager.persist(product);
        entityManager.persist(file);
    }

    @Test
    @DisplayName(value = "이달의쿠폰 save 테스트")
    void couponMonthSaveTest() {
        CouponMonth persist = entityManager.persist(couponMonth);
        Optional<CouponMonth> result = couponMonthRepository.findById(persist.getMonthNo());

        assertThat(result).isPresent();
        assertThat(result.get().getMonthNo()).isEqualTo(persist.getMonthNo());
        assertThat(result.get().getCouponTemplate().getTemplateNo()).isEqualTo(persist.getCouponTemplate().getTemplateNo());
        assertThat(result.get().getOpenedAt()).isEqualTo(persist.getOpenedAt());
        assertThat(result.get().getMonthQuantity()).isEqualTo(persist.getMonthQuantity());
    }

    @DisplayName("이달의 쿠폰 get Test")
    @Test
    void getCouponMonthTest() {
        CouponMonth persist = entityManager.persist(couponMonth);

        Optional<GetCouponMonthResponseDto> result = couponMonthRepository.getCouponMonth(persist.getMonthNo());

        assertThat(result).isPresent();
        assertThat(result.get().getMonthNo()).isEqualTo(persist.getMonthNo());
        assertThat(result.get().getTemplateNo()).isEqualTo(persist.getCouponTemplate().getTemplateNo());
        assertThat(result.get().getTemplateName()).isEqualTo(persist.getCouponTemplate().getTemplateName());
        assertThat(result.get().getTemplateImage()).isEqualTo(file.getFilePath());
        assertThat(result.get().getOpenedAt()).isEqualTo(couponMonth.getOpenedAt());
        assertThat(result.get().getMonthQuantity()).isEqualTo(couponMonth.getMonthQuantity());
    }


    @DisplayName("이달의 쿠폰 리스트 get Test")
    @Test
    void getCouponMonthsTest() {
        // given
        CouponMonth persist = entityManager.persist(couponMonth);

        // when
        List<GetCouponMonthResponseDto> result = couponMonthRepository.getCouponMonths();

        // then
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getMonthNo()).isEqualTo(couponMonth.getMonthNo());
        assertThat(result.get(0).getTemplateNo()).isEqualTo(persist.getCouponTemplate().getTemplateNo());
        assertThat(result.get(0).getTemplateName()).isEqualTo(persist.getCouponTemplate().getTemplateName());
        assertThat(result.get(0).getTemplateImage()).isEqualTo(file.getFilePath());
        assertThat(result.get(0).getOpenedAt()).isEqualTo(couponMonth.getOpenedAt());
        assertThat(result.get(0).getMonthQuantity()).isEqualTo(couponMonth.getMonthQuantity());

    }
}

