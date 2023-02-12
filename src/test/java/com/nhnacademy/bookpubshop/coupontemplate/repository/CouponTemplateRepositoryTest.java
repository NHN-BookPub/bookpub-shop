package com.nhnacademy.bookpubshop.coupontemplate.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.bookpubshop.category.dummy.CategoryDummy;
import com.nhnacademy.bookpubshop.category.entity.Category;
import com.nhnacademy.bookpubshop.couponpolicy.dummy.CouponPolicyDummy;
import com.nhnacademy.bookpubshop.couponpolicy.entity.CouponPolicy;
import com.nhnacademy.bookpubshop.couponstatecode.dummy.CouponStateCodeDummy;
import com.nhnacademy.bookpubshop.couponstatecode.entity.CouponStateCode;
import com.nhnacademy.bookpubshop.coupontemplate.dto.response.GetCouponTemplateResponseDto;
import com.nhnacademy.bookpubshop.coupontemplate.dto.response.GetDetailCouponTemplateResponseDto;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
        couponTemplate = CouponTemplateDummy.dummy(couponPolicy, couponType,
                product, category, couponStateCode);
        file = FileDummy.dummy(null, null, couponTemplate, null, null, null);
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
        assertThat(result.get().getFinishedAt()).isEqualTo(couponTemplate.getFinishedAt());
        assertThat(result.get().isTemplateBundled()).isEqualTo(couponTemplate.isTemplateBundled());
    }

    @Test
    @DisplayName("쿠폰템플릿 상세 단건 조회 성공 테스트")
    void findDetailByTemplateNoTest_Success() {
        entityManager.persist(couponPolicy);
        entityManager.persist(couponType);
        entityManager.persist(couponStateCode);
        entityManager.persist(productPolicy);
        entityManager.persist(productTypeStateCode);
        entityManager.persist(productSaleStateCode);
        entityManager.persist(product);
        entityManager.persist(category);
        CouponTemplate save = entityManager.persist(couponTemplate);
        File saveFile = entityManager.persist(file);

        Optional<GetDetailCouponTemplateResponseDto> result = couponTemplateRepository.findDetailByTemplateNo(save.getTemplateNo());

        assertThat(result).isPresent();
        assertThat(result.get().getTemplateNo()).isEqualTo(save.getTemplateNo());
        assertThat(result.get().isPolicyFixed()).isEqualTo(save.getCouponPolicy().isPolicyFixed());
        assertThat(result.get().getPolicyPrice()).isEqualTo(save.getCouponPolicy().getPolicyMinimum());
        assertThat(result.get().getPolicyMinimum()).isEqualTo(save.getCouponPolicy().getPolicyMinimum());
        assertThat(result.get().getMaxDiscount()).isEqualTo(save.getCouponPolicy().getMaxDiscount());
        assertThat(result.get().getTypeName()).isEqualTo(save.getCouponType().getTypeName());
        assertThat(result.get().getProductTitle()).isEqualTo(save.getProduct().getTitle());
        assertThat(result.get().getCategoryName()).isEqualTo(save.getCategory().getCategoryName());
        assertThat(result.get().getCodeTarget()).isEqualTo(save.getCouponStateCode().getCodeTarget());
        assertThat(result.get().getTemplateName()).isEqualTo(save.getTemplateName());
        assertThat(result.get().getTemplateImage()).isEqualTo(saveFile.getFilePath());
        assertThat(result.get().getFinishedAt()).isEqualTo(save.getFinishedAt());
        assertThat(result.get().isTemplateBundled()).isEqualTo(save.isTemplateBundled());
    }

    @Test
    @DisplayName("쿠폰템플릿 상세 리스트(페이지) 조회 성공 테스트")
    void findDetailAllByTest_Success() {
        entityManager.persist(couponPolicy);
        entityManager.persist(couponType);
        entityManager.persist(couponStateCode);
        entityManager.persist(productPolicy);
        entityManager.persist(productTypeStateCode);
        entityManager.persist(productSaleStateCode);
//        entityManager.persist(product.getRelationProduct().get(0));
        entityManager.persist(product);
        entityManager.persist(category);
        CouponTemplate save = entityManager.persist(couponTemplate);
        File saveFile = entityManager.persist(file);

        Pageable pageable = PageRequest.of(0, 10);

        Page<GetDetailCouponTemplateResponseDto> result = couponTemplateRepository.findDetailAllBy(pageable);

        List<GetDetailCouponTemplateResponseDto> content = result.getContent();

        assertThat(content).isNotEmpty();
        assertThat(content.get(0).getTemplateNo()).isEqualTo(save.getTemplateNo());
        assertThat(content.get(0).isPolicyFixed()).isEqualTo(save.getCouponPolicy().isPolicyFixed());
        assertThat(content.get(0).getPolicyPrice()).isEqualTo(save.getCouponPolicy().getPolicyMinimum());
        assertThat(content.get(0).getPolicyMinimum()).isEqualTo(save.getCouponPolicy().getPolicyMinimum());
        assertThat(content.get(0).getMaxDiscount()).isEqualTo(save.getCouponPolicy().getMaxDiscount());
        assertThat(content.get(0).getTypeName()).isEqualTo(save.getCouponType().getTypeName());
        assertThat(content.get(0).getProductTitle()).isEqualTo(save.getProduct().getTitle());
        assertThat(content.get(0).getCategoryName()).isEqualTo(save.getCategory().getCategoryName());
        assertThat(content.get(0).getCodeTarget()).isEqualTo(save.getCouponStateCode().getCodeTarget());
        assertThat(content.get(0).getTemplateName()).isEqualTo(save.getTemplateName());
        assertThat(content.get(0).getTemplateImage()).isEqualTo(saveFile.getFilePath());
        assertThat(content.get(0).getFinishedAt()).isEqualTo(save.getFinishedAt());
        assertThat(content.get(0).isTemplateBundled()).isEqualTo(save.isTemplateBundled());
    }

    @Test
    @DisplayName("쿠폰템플릿 리스트(페이지) 조회 성공 테스트")
    void findAllBy_Success() {
        entityManager.persist(couponPolicy);
        entityManager.persist(couponType);
        entityManager.persist(couponStateCode);
        entityManager.persist(productPolicy);
        entityManager.persist(productTypeStateCode);
        entityManager.persist(productSaleStateCode);
//        entityManager.persist(product.getRelationProduct().get(0));
        entityManager.persist(product);
        entityManager.persist(category);
        CouponTemplate save = entityManager.persist(couponTemplate);
        File saveFile = entityManager.persist(file);

        Pageable pageable = PageRequest.of(0, 10);

        Page<GetCouponTemplateResponseDto> result = couponTemplateRepository.findAllBy(pageable);

        List<GetCouponTemplateResponseDto> content = result.getContent();

        assertThat(content).isNotEmpty();
        assertThat(content.get(0).getTemplateName()).isEqualTo(save.getTemplateName());
        assertThat(content.get(0).getTemplateImage()).isEqualTo(saveFile.getFilePath());
        assertThat(content.get(0).getFinishedAt()).isEqualTo(save.getFinishedAt());
    }

    @Test
    @DisplayName("쿠폰템플릿 이름을 가지고 쿠폰템플릿을 가져오는 테스트")
    void getTemplateByTemplateName() {
        entityManager.persist(couponPolicy);
        entityManager.persist(couponType);
        entityManager.persist(couponStateCode);
        entityManager.persist(productPolicy);
        entityManager.persist(productTypeStateCode);
        entityManager.persist(productSaleStateCode);
//        entityManager.persist(product.getRelationProduct().get(0));
        entityManager.persist(product);
        entityManager.persist(category);
        CouponTemplate save = entityManager.persist(couponTemplate);

        Optional<CouponTemplate> template
                = couponTemplateRepository.findDetailByTemplateName("test_templateName");

        assertThat(template).isPresent();
        assertThat(template.get().getTemplateNo()).isEqualTo(save.getTemplateNo());
        assertThat(template.get().getTemplateName()).isEqualTo(save.getTemplateName());
        assertThat(template.get().getFile()).isEqualTo(save.getFile());
        assertThat(template.get().getCouponType()).isEqualTo(save.getCouponType());
        assertThat(template.get().getCouponPolicy()).isEqualTo(save.getCouponPolicy());
        assertThat(template.get().getCategory()).isEqualTo(save.getCategory());
        assertThat(template.get().getCouponStateCode()).isEqualTo(save.getCouponStateCode());
        assertThat(template.get().getProduct()).isEqualTo(save.getProduct());
    }

}