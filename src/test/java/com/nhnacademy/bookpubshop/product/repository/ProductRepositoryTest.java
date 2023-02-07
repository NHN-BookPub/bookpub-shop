package com.nhnacademy.bookpubshop.product.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.bookpubshop.author.dummy.AuthorDummy;
import com.nhnacademy.bookpubshop.author.entity.Author;
import com.nhnacademy.bookpubshop.category.dummy.CategoryDummy;
import com.nhnacademy.bookpubshop.category.entity.Category;
import com.nhnacademy.bookpubshop.product.dto.response.GetProductByCategoryResponseDto;
import com.nhnacademy.bookpubshop.file.dummy.FileDummy;
import com.nhnacademy.bookpubshop.file.entity.File;
import com.nhnacademy.bookpubshop.product.dto.response.GetProductByTypeResponseDto;
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
import com.nhnacademy.bookpubshop.state.FileCategory;
import com.nhnacademy.bookpubshop.tag.dummy.TagDummy;
import com.nhnacademy.bookpubshop.tag.entity.Tag;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    File file;


    @BeforeEach
    void setUp() {
        file = FileDummy.dummy(null, null,
                null, null,product, null, FileCategory.PRODUCT_THUMBNAIL);
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

        product.setProductFiles(List.of(file));

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
        assertThat(persist.getFiles().get(0).getFileNo()).isEqualTo(file.getFileNo());
        assertThat(persist.getFiles().get(0).getFilePath()).isEqualTo(file.getFilePath());
        assertThat(persist.getFiles().get(0).getFileExtension()).isEqualTo(file.getFileExtension());
        assertThat(persist.getFiles().get(0).getFileCategory()).isEqualTo(file.getFileCategory());
        assertThat(persist.getFiles().get(0).getNameOrigin()).isEqualTo(file.getNameOrigin());
        assertThat(persist.getFiles().get(0).getNameSaved()).isEqualTo(file.getNameSaved());
    }

    @Test
    @DisplayName("모든 상품 조회 테스트")
    void getAllProducts() {
        // given
        Pageable pageable = Pageable.ofSize(10);
        entityManager.persist(file);
        // when
        Page<GetProductListResponseDto> allProducts = productRepository.getAllProducts(pageable);

        // then
        assertThat(allProducts.getContent()).isNotEmpty();
    }

    @Test
    @DisplayName("제목 검색 테스트")
    void getProductListLikeTitle() {
        // given
        Product persist = entityManager.persist(product);
        Pageable pageable = Pageable.ofSize(10);

        entityManager.persist(file);

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

        entityManager.persist(file);


        // when
        Optional<GetProductDetailResponseDto> result = productRepository.getProductDetailById(save.getProductNo());

        // then
        assertThat(result).isNotEmpty();
        assertThat(result.get().getSalesRate()).isEqualTo(persist.getSalesRate());
        assertThat(result.get().isProductSubscribed()).isTrue();
        assertThat(result.get().getProductNo()).isEqualTo(persist.getProductNo());
        assertThat(result.get().getProductIsbn()).isEqualTo(persist.getProductIsbn());
        assertThat(result.get().getProductDescription()).isEqualTo(persist.getProductDescription());
        assertThat(result.get().getProductPriority()).isEqualTo(persist.getProductPriority());
        assertThat(result.get().getProductStock()).isEqualTo(persist.getProductStock());
        assertThat(result.get().getSalesPrice()).isEqualTo(persist.getSalesPrice());
        assertThat(result.get().getPageCount()).isEqualTo(persist.getPageCount());
    }

    @Disabled
    @Test
    @DisplayName("상품 유형으로 조회하는 테스트")
    void findProductListByType_Test() {
        // given
        Product persist = entityManager.persist(product);
        Author author = AuthorDummy.dummy();
        entityManager.persist(author);

        persist.getProductAuthors().add(
                new ProductAuthor(
                        new ProductAuthor.Pk(author.getAuthorNo(), product.getProductNo()), author, product));

        Category category = CategoryDummy.dummy();
        entityManager.persist(category);
        entityManager.persist(file.getSubscribe());

        entityManager.persist(file);

        persist.getProductCategories().add(
                new ProductCategory(
                        new ProductCategory.Pk(category.getCategoryNo(), product.getProductNo()), category, product));

        Tag tag = TagDummy.dummy();
        entityManager.persist(tag);
        persist.getProductTags().add(
                new ProductTag(new ProductTag.Pk(tag.getTagNo(), product.getProductNo()), tag, product));
        entityManager.persist(product);

        // when
        List<GetProductByTypeResponseDto> list = productRepository.findProductListByType(persist.getProductTypeStateCode().getCodeNo(), 5);

        // then
        assertThat(list).isNotEmpty();
    }

    @Test
    @DisplayName("카트에 담긴 상품 정보 조회 테스트")
    void getProductsInCart() {
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
        entityManager.persist(product);

        entityManager.persist(file);


        // when
        List<GetProductDetailResponseDto> productsInCart = productRepository.getProductsInCart(List.of(persist.getProductNo()));

        // then
        assertThat(productsInCart).isNotEmpty();
        assertThat(productsInCart.get(0).getProductNo()).isEqualTo(persist.getProductNo());
        assertThat(productsInCart.get(0).getTitle()).isEqualTo(persist.getTitle());
        assertThat(productsInCart.get(0).getProductPublisher()).isEqualTo(persist.getProductPublisher());
        assertThat(productsInCart.get(0).getProductPrice()).isEqualTo(persist.getProductPrice());
        assertThat(productsInCart.get(0).getSalesPrice()).isEqualTo(persist.getSalesPrice());
    }

    @Test
    @DisplayName("카테고리별 상품 조회 테스트")
    void getProductsByCategory() {
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
        entityManager.persist(product);

        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<GetProductByCategoryResponseDto> result = productRepository.getProductsByCategory(category.getCategoryNo(), pageable);

        // then
        List<GetProductByCategoryResponseDto> content = result.getContent();
        assertThat(content).isNotEmpty();
        assertThat(content.get(0).getProductNo()).isEqualTo(persist.getProductNo());
        assertThat(content.get(0).getTitle()).isEqualTo(persist.getTitle());
        assertThat(content.get(0).getSalesPrice()).isEqualTo(persist.getSalesPrice());
        assertThat(content.get(0).getSalesRate()).isEqualTo(persist.getSalesRate());
        assertThat(content.get(0).getCategories()).isEqualTo(List.of(category.getCategoryName()));
        assertThat(content.get(0).getAuthors()).isEqualTo(List.of(author.getAuthorName()));
    }
}