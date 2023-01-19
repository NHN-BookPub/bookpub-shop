package com.nhnacademy.bookpubshop.product.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.author.dummy.AuthorDummy;
import com.nhnacademy.bookpubshop.author.entity.Author;
import com.nhnacademy.bookpubshop.category.dummy.CategoryDummy;
import com.nhnacademy.bookpubshop.category.entity.Category;
import com.nhnacademy.bookpubshop.product.dto.response.GetProductDetailResponseDto;
import com.nhnacademy.bookpubshop.product.dto.response.GetProductListResponseDto;
import com.nhnacademy.bookpubshop.product.dummy.ProductDummy;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductPolicyDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductSaleStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductTypeStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductAuthor;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductCategory;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTag;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import com.nhnacademy.bookpubshop.tag.dummy.TagDummy;
import com.nhnacademy.bookpubshop.tag.entity.Tag;
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
 * 상품 레포지토리 테스트.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@DataJpaTest
class ProductRepositoryTest {
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    ProductRepository productRepository;

    Product product;
    ProductPolicy productPolicy;
    ProductTypeStateCode productTypeStateCode;
    ProductSaleStateCode productSaleStateCode;

    @BeforeEach
    void setUp() {
        productPolicy = ProductPolicyDummy.dummy();
        productTypeStateCode = ProductTypeStateCodeDummy.dummy();
        productSaleStateCode = ProductSaleStateCodeDummy.dummy();

        entityManager.persist(productPolicy);
        entityManager.persist(productTypeStateCode);
        entityManager.persist(productSaleStateCode);

        product = ProductDummy.dummy(productPolicy, productTypeStateCode, productSaleStateCode);
        entityManager.persist(product.getRelationProduct().get(0));

    }

    @Test
    @DisplayName("상품 save 테스트")
    void productSaveTest() {
        LocalDateTime now = LocalDateTime.now();

        Product persist = entityManager.persist(product);

        Optional<Product> product = productRepository.findById(persist.getProductNo());
        assertThat(product).isPresent();
        assertThat(product.get().getProductPolicy().getPolicyNo()).isEqualTo(persist.getProductPolicy().getPolicyNo());
        assertThat(product.get().getRelationProduct()).isEqualTo(persist.getRelationProduct());
        assertThat(product.get().getSalesRate()).isEqualTo(persist.getSalesRate());
        assertThat(product.get().getViewCount()).isEqualTo(persist.getViewCount());
        assertThat(product.get().isProductDeleted()).isFalse();
        assertThat(product.get().isProductSubscribed()).isTrue();
        assertThat(product.get().getProductNo()).isEqualTo(persist.getProductNo());
        assertThat(product.get().getProductIsbn()).isEqualTo(persist.getProductIsbn());
        assertThat(product.get().getProductDescription()).isEqualTo(persist.getProductDescription());
        assertThat(product.get().getProductPriority()).isEqualTo(persist.getProductPriority());
        assertThat(product.get().getProductStock()).isEqualTo(persist.getProductStock());
        assertThat(product.get().getSalesPrice()).isEqualTo(persist.getSalesPrice());
        assertThat(product.get().getPageCount()).isEqualTo(persist.getPageCount());
        assertThat(product.get().getCreatedAt()).isAfter(now);
    }

    @Test
    @DisplayName("모든 상품 조회 테스트")
    void getAllProducts() {
        // given
        Pageable pageable = Pageable.ofSize(10);

        // when
        Page<GetProductListResponseDto> allProducts = productRepository.getAllProducts(pageable);

        // then
        assertThat(allProducts.getContent()).isNotEmpty();
        assertThat(allProducts.getContent())
                .hasSize(1);
    }

    @Test
    @DisplayName("제목 검색 테스트")
    void getProductListLikeTitle() {
        // given
        Product persist = entityManager.persist(product);
        Pageable pageable = Pageable.ofSize(10);

        // when
        Page<GetProductListResponseDto> likeTitle = productRepository.getProductListLikeTitle(persist.getTitle(), pageable);

        // then
        assertThat(likeTitle.getContent()).isNotEmpty();
    }

    @Test
    @DisplayName("상품 단건 조회 테스트")
    void getProductDetailById() {
        // given
        Product persist = entityManager.persist(product);
        Author author = AuthorDummy.dummy();
        entityManager.persist(author);

        persist.getProductAuthors().add(
                new ProductAuthor(
                        new ProductAuthor.Pk(author.getAuthorNo(), product.getProductNo()), author, product));

        Category category = CategoryDummy.dummy();
        entityManager.persist(category);

        persist.getProductCategories().add(
                new ProductCategory(
                        new ProductCategory.Pk(category.getCategoryNo(), product.getProductNo()), category, product));

        Tag tag = TagDummy.dummy();
        entityManager.persist(tag);
        persist.getProductTags().add(
                new ProductTag(new ProductTag.Pk(tag.getTagNo(), product.getProductNo()), tag, product));
        Product save = productRepository.save(product);

        // when
        Optional<GetProductDetailResponseDto> result = productRepository.getProductDetailById(save.getProductNo());

        // then
        assertThat(result).isNotEmpty();
    }

}