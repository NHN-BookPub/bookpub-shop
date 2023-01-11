package com.nhnacademy.bookpubshop.product.relationship.repository;

import static com.nhnacademy.bookpubshop.state.ProductSaleState.SALE;
import static com.nhnacademy.bookpubshop.state.ProductTypeState.BEST_SELLER;
import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.category.entity.Category;
import com.nhnacademy.bookpubshop.category.repository.CategoryRepository;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductCategory;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import com.nhnacademy.bookpubshop.product.repository.ProductRepository;
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

        productPolicy = new ProductPolicy(null, "실구매가가기준", true, 5);
        productPolicyRepository.save(productPolicy);

        productTypeStateCode = new ProductTypeStateCode(null,
                BEST_SELLER.getName(), BEST_SELLER.isUsed(), "이 책은 베스트셀러입니다.");
        productTypeStateCodeRepository.save(productTypeStateCode);

        productSaleStateCode = new ProductSaleStateCode(null,
                SALE.getName(), SALE.isUsed(), "이 상품은 판매중입니다.");
        productSaleStateCodeRepository.save(productSaleStateCode);

        product = new Product(null, productPolicy, productTypeStateCode, productSaleStateCode,
                Collections.EMPTY_LIST, "1231231111", "title", "publisher",100, "설명",
                "썸네일.png", "eBook path", 20000L,30000L,
                10, 1L, 5, false, 100,
                LocalDateTime.now(), LocalDateTime.now(), false);
        productRepository.save(product);
    }

    @Test
    @DisplayName(value = "상품카테고리관계(product_and_category) 테이블 save 테스트")
    void productCategorySaveTest() {
        ProductCategory testProductCategory = new ProductCategory(
                new ProductCategory.Pk(category.getCategoryNo(), product.getProductNo()), category, product);
        productCategoryRepository.save(testProductCategory);

        Optional<ProductCategory> optional = productCategoryRepository.findById(
                new ProductCategory.Pk(category.getCategoryNo(), product.getProductNo()));
        assertThat(optional).isPresent();
        assertThat(optional.get().getPk().getCategoryNo()).isEqualTo(category.getCategoryNo());
        assertThat(optional.get().getPk().getProductNo()).isEqualTo(product.getProductNo());

        entityManager.clear();
    }
}