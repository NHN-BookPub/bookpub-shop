package com.nhnacademy.bookpubshop.product.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.bookpubshop.author.dummy.AuthorDummy;
import com.nhnacademy.bookpubshop.author.entity.Author;
import com.nhnacademy.bookpubshop.category.dummy.CategoryDummy;
import com.nhnacademy.bookpubshop.category.entity.Category;
import com.nhnacademy.bookpubshop.file.dummy.FileDummy;
import com.nhnacademy.bookpubshop.file.entity.File;
import com.nhnacademy.bookpubshop.member.dummy.MemberDummy;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.order.dummy.OrderDummy;
import com.nhnacademy.bookpubshop.order.entity.BookpubOrder;
import com.nhnacademy.bookpubshop.order.relationship.entity.OrderProduct;
import com.nhnacademy.bookpubshop.order.relationship.entity.OrderProductStateCode;
import com.nhnacademy.bookpubshop.orderstatecode.dummy.OrderStateCodeDummy;
import com.nhnacademy.bookpubshop.orderstatecode.entity.OrderStateCode;
import com.nhnacademy.bookpubshop.pricepolicy.dummy.PricePolicyDummy;
import com.nhnacademy.bookpubshop.pricepolicy.entity.PricePolicy;
import com.nhnacademy.bookpubshop.product.dto.response.GetProductByCategoryResponseDto;
import com.nhnacademy.bookpubshop.product.dto.response.GetProductByTypeResponseDto;
import com.nhnacademy.bookpubshop.product.dto.response.GetProductDetailResponseDto;
import com.nhnacademy.bookpubshop.product.dto.response.GetProductListForOrderResponseDto;
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
import com.nhnacademy.bookpubshop.tier.dummy.TierDummy;
import com.nhnacademy.bookpubshop.tier.entity.BookPubTier;
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
        productPolicy = ProductPolicyDummy.dummy();
        productTypeStateCode = ProductTypeStateCodeDummy.dummy();
        productSaleStateCode = ProductSaleStateCodeDummy.dummy();

        entityManager.persist(productPolicy);
        entityManager.persist(productTypeStateCode);
        entityManager.persist(productSaleStateCode);

        product = ProductDummy.dummy(productPolicy, productTypeStateCode, productSaleStateCode);
    }

    @Test
    @DisplayName("상품 save 테스트")
    void productSaveTest() {
        LocalDateTime now = LocalDateTime.now();

        file = entityManager.persist(
                FileDummy.dummy(null, null,
                        null, product, null, FileCategory.PRODUCT_THUMBNAIL, null));

        product.setProductFiles(List.of(file));

        Product persist = entityManager.persist(product);

        Optional<Product> product = productRepository.findById(persist.getProductNo());
        assertThat(product).isPresent();
        assertThat(product.get().getProductPolicy().getPolicyNo()).isEqualTo(persist.getProductPolicy().getPolicyNo());
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
        Product persist = entityManager.persist(product);
        Pageable pageable = Pageable.ofSize(10);

        file = entityManager.persist(
                FileDummy.dummy(null, null,
                        null, product, null, FileCategory.PRODUCT_THUMBNAIL, null));

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

        file = entityManager.persist(
                FileDummy.dummy(null, null,
                        null, product, null, FileCategory.PRODUCT_THUMBNAIL, null));

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

        file = entityManager.persist(
                FileDummy.dummy(null, null,
                        null, product, null, FileCategory.PRODUCT_THUMBNAIL, null));

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

        file = entityManager.persist(
                FileDummy.dummy(null, null,
                        null, product, null, FileCategory.PRODUCT_THUMBNAIL, null));

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

        file = entityManager.persist(
                FileDummy.dummy(null, null,
                        null, product, null, FileCategory.PRODUCT_THUMBNAIL, null));

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

    @Test
    void getEbooks() {
        Product persist = entityManager.persist(product);
        Author author = entityManager.persist(AuthorDummy.dummy());
        File file = entityManager.persist(FileDummy
                .dummy(null, null,
                        null, persist, null, FileCategory.PRODUCT_THUMBNAIL, null));

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

        Page<GetProductByCategoryResponseDto> result = productRepository.getProductsByCategory(category.getCategoryNo(), pageable);

        List<GetProductByCategoryResponseDto> content = result.getContent();
        assertThat(content).isNotEmpty();
        assertThat(content.get(0).getProductNo()).isEqualTo(persist.getProductNo());
        assertThat(content.get(0).getTitle()).isEqualTo(persist.getTitle());
        assertThat(content.get(0).getSalesPrice()).isEqualTo(persist.getSalesPrice());
        assertThat(content.get(0).getSalesRate()).isEqualTo(persist.getSalesRate());
        assertThat(content.get(0).getCategories()).isEqualTo(List.of(category.getCategoryName()));
        assertThat(content.get(0).getAuthors()).isEqualTo(List.of(author.getAuthorName()));
        assertThat(content.get(0).getThumbnail()).isEqualTo(file.getFilePath());
    }

    @Disabled
    @Test
    @DisplayName("주문 번호로 상품 리스트 조회 테스트")
    void getProduct_By_Order_Number_Test() {
        // given
        Product productDummy = entityManager.persist(product);
        file = FileDummy.dummy(null, null, null, product,
                null, FileCategory.PRODUCT_THUMBNAIL, null);

        BookPubTier tier = TierDummy.dummy();
        Member member = MemberDummy.dummy2(tier);
        PricePolicy pricePolicy = PricePolicyDummy.dummy();
        PricePolicy pricePolicy2 = PricePolicyDummy.dummy();
        OrderStateCode orderStateCode = OrderStateCodeDummy.dummy();

        BookpubOrder order = OrderDummy.dummy(member, pricePolicy, pricePolicy2, orderStateCode);
        OrderProductStateCode orderProductStateCode = new OrderProductStateCode(
                null, "name", true, "info");
        OrderProduct orderProduct = new OrderProduct(null, product, order, orderProductStateCode, 10,
                10L, 10L, "A", 100L,"");
        File fileDummy = entityManager.persist(file);
        entityManager.persist(tier);
        entityManager.persist(member);
        entityManager.persist(pricePolicy);
        entityManager.persist(pricePolicy2);
        entityManager.persist(orderStateCode);
        BookpubOrder orderDummy = entityManager.persist(order);
        OrderProductStateCode codeDummy = entityManager.persist(orderProductStateCode);
        OrderProduct orderProductDummy = entityManager.persist(orderProduct);

        // when
        List<GetProductListForOrderResponseDto> list = productRepository.getProductListByOrderNo(orderDummy.getOrderNo());

        // then
        assertThat(list).isNotEmpty();
        assertThat(list.get(0).getProductNo()).isEqualTo(productDummy.getProductNo());
        assertThat(list.get(0).getTitle()).isEqualTo(productDummy.getTitle());
        assertThat(list.get(0).getSalesPrice()).isEqualTo(productDummy.getSalesPrice());
        assertThat(list.get(0).getThumbnail()).isEqualTo(fileDummy.getFilePath());
        assertThat(list.get(0).getProductAmount()).isEqualTo(orderProductDummy.getProductAmount());
        assertThat(list.get(0).getStateCode()).isEqualTo(codeDummy.getCodeName());
    }

    @Test
    @DisplayName("E-Book 조회 테스트")
    void get_EBooks() {
        // given
        Product productDummy = entityManager.persist(product);
        file = FileDummy.dummy(null, null, null, product,
                null, FileCategory.PRODUCT_EBOOK, null);
        Category category = new Category(null, null, "cate", 1, true);
        ProductCategory productCategory = new ProductCategory(
                new ProductCategory.Pk(category.getCategoryNo(), product.getProductNo()), category, product);
        Author author = new Author(null, "au", "book");
        ProductAuthor productAuthor = new ProductAuthor(
                new ProductAuthor.Pk(author.getAuthorNo(), product.getProductNo()), author, product);

        entityManager.persist(category);
        entityManager.persist(productCategory);
        entityManager.persist(author);
        entityManager.persist(productAuthor);

        File fileDummy = entityManager.persist(file);
        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<GetProductByCategoryResponseDto> ebooks = productRepository.getEbooks(pageable);

        // then
        assertThat(ebooks.getContent().get(0).getProductNo()).isEqualTo(productDummy.getProductNo());
        assertThat(ebooks.getContent().get(0).getTitle()).isEqualTo(productDummy.getTitle());
        assertThat(ebooks.getContent().get(0).getSalesPrice()).isEqualTo(productDummy.getSalesPrice());
        assertThat(ebooks.getContent().get(0).getSalesRate()).isEqualTo(productDummy.getSalesRate());
        assertThat(ebooks.getContent().get(0).getCategories().get(0)).isEqualTo(category.getCategoryName());
        assertThat(ebooks.getContent().get(0).getAuthors().get(0)).isEqualTo(author.getAuthorName());
    }

    @Test
    @DisplayName("e-book 파일 경로 조회 테스트")
    void getEBook_Path_Test() {
        // given
        file = FileDummy.dummy(null, null, null, product,
                null, FileCategory.PRODUCT_EBOOK, null);
        Product productDummy = entityManager.persist(product);
        File fileDummy = entityManager.persist(file);

        // when
        String filePath = productRepository.getFilePath(productDummy.getProductNo());

        // then
        assertThat(filePath).isEqualTo(fileDummy.getFilePath());
    }
}