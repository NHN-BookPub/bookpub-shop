package com.nhnacademy.bookpubshop.product.relationship.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.category.entity.Category;
import com.nhnacademy.bookpubshop.category.repository.CategoryRepository;
import com.nhnacademy.bookpubshop.product.dummy.ProductDummy;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductPolicyDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductSaleStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductTypeStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductCategory;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import com.nhnacademy.bookpubshop.product.repository.ProductRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * 상품카테고리관계(product_and_category) 레포지토리 테스트.
 *
 * @author : 박경서
 * @since : 1.0
 **/
@DataJpaTest
class ProductCategoryRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    ProductCategoryRepository productCategoryRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductPolicyRepository productPolicyRepository;

    @Autowired
    ProductTypeStateCodeRepository productTypeStateCodeRepository;

    @Autowired
    ProductSaleStateCodeRepository productSaleStateCodeRepository;

    Category category;
    ProductPolicy productPolicy;
    Product product;
    ProductTypeStateCode productTypeStateCode;
    ProductSaleStateCode productSaleStateCode;

    @BeforeEach
    void setUp() {
        category = new Category(null, null, "소설", 0, true);
        categoryRepository.save(category);

        productPolicy = ProductPolicyDummy.dummy();
        productTypeStateCode = ProductTypeStateCodeDummy.dummy();
        productSaleStateCode = ProductSaleStateCodeDummy.dummy();
        product = ProductDummy.dummy(productPolicy, productTypeStateCode, productSaleStateCode);

        entityManager.persist(productPolicy);
        entityManager.persist(productTypeStateCode);
        entityManager.persist(productSaleStateCode);
        entityManager.persist(product);
    }

    @Test
    @DisplayName(value = "상품카테고리관계(product_and_category) 테이블 save 테스트")
    void productCategorySaveTest() {
        ProductCategory testProductCategory = new ProductCategory(
                new ProductCategory.Pk(category.getCategoryNo(), product.getProductNo()), category, product);
        ProductCategory persist = productCategoryRepository.save(testProductCategory);

        Optional<ProductCategory> optional = productCategoryRepository.findById(persist.getPk());
        assertThat(optional).isPresent();
        assertThat(optional.get().getPk()).isEqualTo(persist.getPk());
        assertThat(optional.get().getPk().getCategoryNo()).isEqualTo(persist.getPk().getCategoryNo());
        assertThat(optional.get().getPk().getProductNo()).isEqualTo(persist.getPk().getProductNo());
        assertThat(optional.get().getCategory().getCategoryNo()).isEqualTo(persist.getCategory().getCategoryNo());
        assertThat(optional.get().getProduct().getProductNo()).isEqualTo(persist.getProduct().getProductNo());

        entityManager.clear();
    }
}