package com.nhnacademy.bookpubshop.coupontemplate.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.category.entity.Category;
import com.nhnacademy.bookpubshop.couponpolicy.dummy.CouponPolicyDummy;
import com.nhnacademy.bookpubshop.couponpolicy.entity.CouponPolicy;
import com.nhnacademy.bookpubshop.couponstatecode.dummy.CouponStateCodeDummy;
import com.nhnacademy.bookpubshop.couponstatecode.entity.CouponStateCode;
import com.nhnacademy.bookpubshop.coupontemplate.dummy.CouponTemplateDummy;
import com.nhnacademy.bookpubshop.coupontemplate.entity.CouponTemplate;
import com.nhnacademy.bookpubshop.coupontype.dummy.CouponTypeDummy;
import com.nhnacademy.bookpubshop.coupontype.entity.CouponType;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import java.time.LocalDateTime;
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

    @BeforeEach
    void setUp() {
        couponPolicy = CouponPolicyDummy.dummy();
        couponType = CouponTypeDummy.dummy();
        couponStateCode = CouponStateCodeDummy.dummy();
        couponTemplate = CouponTemplateDummy.dummy(couponPolicy, couponType,
                productDummy(), categoryDummy(), couponStateCode);
    }

    @Test
    @DisplayName(value = "쿠폰 템플릿 save 테스트")
    void CouponTemplateSaveTest() {
        entityManager.persist(couponPolicy);
        entityManager.persist(couponType);
        entityManager.persist(couponStateCode);
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

    private Product productDummy() {
        Product product = new Product(null, productPolicyDummy(), productTypeStateCodeDummy(),
                productSaleStateCodeDummy(), "isbn",
                "title", 10, "description",
                "test", "file_path", 10L, 1,
                1L, 1, false,
                1, LocalDateTime.now(), LocalDateTime.now(), false);
        return entityManager.persist(product);
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