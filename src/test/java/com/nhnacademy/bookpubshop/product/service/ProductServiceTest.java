package com.nhnacademy.bookpubshop.product.service;

import static com.nhnacademy.bookpubshop.state.ProductTypeState.BEST_SELLER;
import static com.nhnacademy.bookpubshop.state.ProductTypeState.NEW;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import com.nhnacademy.bookpubshop.author.dummy.AuthorDummy;
import com.nhnacademy.bookpubshop.author.entity.Author;
import com.nhnacademy.bookpubshop.author.repository.AuthorRepository;
import com.nhnacademy.bookpubshop.category.dummy.CategoryDummy;
import com.nhnacademy.bookpubshop.category.entity.Category;
import com.nhnacademy.bookpubshop.category.repository.CategoryRepository;
import com.nhnacademy.bookpubshop.product.dto.CreateProductRequestDto;
import com.nhnacademy.bookpubshop.product.dto.response.GetProductByTypeResponseDto;
import com.nhnacademy.bookpubshop.product.dto.response.GetProductDetailResponseDto;
import com.nhnacademy.bookpubshop.product.dto.response.GetProductListResponseDto;
import com.nhnacademy.bookpubshop.product.dummy.ProductDummy;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.exception.NotFoundProductPolicyException;
import com.nhnacademy.bookpubshop.product.exception.NotFoundStateCodeException;
import com.nhnacademy.bookpubshop.product.exception.ProductNotFoundException;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductAuthor;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductCategory;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTag;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import com.nhnacademy.bookpubshop.product.relationship.repository.ProductAuthorRepository;
import com.nhnacademy.bookpubshop.product.relationship.repository.ProductPolicyRepository;
import com.nhnacademy.bookpubshop.product.relationship.repository.ProductSaleStateCodeRepository;
import com.nhnacademy.bookpubshop.product.relationship.repository.ProductTypeStateCodeRepository;
import com.nhnacademy.bookpubshop.product.repository.ProductRepository;
import com.nhnacademy.bookpubshop.product.service.impl.ProductServiceImpl;
import com.nhnacademy.bookpubshop.tag.dummy.TagDummy;
import com.nhnacademy.bookpubshop.tag.entity.Tag;
import com.nhnacademy.bookpubshop.tag.repository.TagRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * 상품 서비스 테스트.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@ExtendWith(SpringExtension.class)
@Import(ProductServiceImpl.class)
class ProductServiceTest {

    @Autowired
    ProductService productService;

    @MockBean
    ProductPolicyRepository productPolicyRepository;
    @MockBean
    ProductSaleStateCodeRepository saleStateCodeRepository;
    @MockBean
    ProductTypeStateCodeRepository typeStateCodeRepository;
    @MockBean
    ProductAuthorRepository productAuthorRepository;
    @MockBean
    CategoryRepository categoryRepository;
    @MockBean
    TagRepository tagRepository;
    @MockBean
    AuthorRepository authorRepository;
    @MockBean
    ProductRepository productRepository;

    Product product;
    CreateProductRequestDto requestDto;
    GetProductDetailResponseDto responseDto;
    GetProductListResponseDto listResponseDto;
    ProductPolicy productPolicy;
    ProductTypeStateCode typeStateCode;
    ProductSaleStateCode saleStateCode;
    ArgumentCaptor<Product> captor;
    Author author;
    Category category;
    Tag tag;

    @BeforeEach
    void setUp() {
        productPolicy = new ProductPolicy(1, "method", true, 1);
        typeStateCode = new ProductTypeStateCode(1, BEST_SELLER.getName(), BEST_SELLER.isUsed(), "info");
        saleStateCode = new ProductSaleStateCode(1, NEW.getName(), NEW.isUsed(), "info");

        product = ProductDummy.dummy(productPolicy, typeStateCode, saleStateCode);

        author = AuthorDummy.dummy();
        ProductAuthor productAuthor =
                new ProductAuthor(new ProductAuthor.Pk(author.getAuthorNo(), product.getProductNo()),
                        author, product);
        product.getProductAuthors().add(productAuthor);

        category = CategoryDummy.dummy();
        ProductCategory productCategory =
                new ProductCategory(new ProductCategory.Pk(category.getCategoryNo(), product.getProductNo()), category, product);
        product.getProductCategories().add(productCategory);

        tag = TagDummy.dummy();
        ProductTag productTag =
                new ProductTag(new ProductTag.Pk(tag.getTagNo(), product.getProductNo()), tag, product);
        product.getProductTags().add(productTag);

        requestDto = new CreateProductRequestDto();

        listResponseDto = new GetProductListResponseDto(
                product.getProductNo(),
                product.getTitle(),
                product.getProductStock(),
                product.getSalesPrice(),
                product.getSalesRate(),
                product.getProductPrice(),
                product.isProductDeleted());


        List<Long> relation = new ArrayList<>();

        for (Product relationProduct : product.getRelationProduct()) {
            relation.add(relationProduct.getProductNo());
        }

        ReflectionTestUtils.setField(requestDto, "productIsbn", product.getProductIsbn());
        ReflectionTestUtils.setField(requestDto, "title", product.getTitle());
        ReflectionTestUtils.setField(requestDto, "productPublisher", product.getProductPublisher());
        ReflectionTestUtils.setField(requestDto, "pageCount", product.getPageCount());
        ReflectionTestUtils.setField(requestDto, "productDescription", product.getProductDescription());
        ReflectionTestUtils.setField(requestDto, "salePrice", product.getSalesPrice());
        ReflectionTestUtils.setField(requestDto, "productPrice", product.getProductPrice());
        ReflectionTestUtils.setField(requestDto, "salesRate", product.getSalesRate());
        ReflectionTestUtils.setField(requestDto, "productPriority", product.getProductPriority());
        ReflectionTestUtils.setField(requestDto, "productStock", product.getProductStock());
        ReflectionTestUtils.setField(requestDto, "publishedAt", product.getPublishDate());
        ReflectionTestUtils.setField(requestDto, "subscribed", product.isProductSubscribed());
        ReflectionTestUtils.setField(requestDto, "productPolicyNo", product.getProductPolicy().getPolicyNo());
        ReflectionTestUtils.setField(requestDto, "saleCodeNo", product.getProductSaleStateCode().getCodeNo());
        ReflectionTestUtils.setField(requestDto, "typeCodeNo", product.getProductTypeStateCode().getCodeNo());
        ReflectionTestUtils.setField(requestDto, "authorsNo", List.of(1));
        ReflectionTestUtils.setField(requestDto, "categoriesNo", List.of(1));
        ReflectionTestUtils.setField(requestDto, "tagsNo", List.of(1));
        ReflectionTestUtils.setField(requestDto, "relationProducts", relation);

        responseDto = new GetProductDetailResponseDto(product);

        captor = ArgumentCaptor.forClass(Product.class);
    }

    @Test
    @DisplayName("단건 조회 성공 테스트")
    void getProductDetail_Success_Test() {
        when(productRepository.getProductDetailById(anyLong()))
                .thenReturn(Optional.ofNullable(responseDto));

        assertThat(responseDto.getProductNo()).isEqualTo(product.getProductNo());
        assertThat(responseDto.getProductIsbn()).isEqualTo(product.getProductIsbn());
        assertThat(responseDto.getPolicyMethod()).isEqualTo(productPolicy.getPolicyMethod());
    }

    @Test
    @DisplayName("상품 번호로 조회 실패")
    void getProductDetailByIdFailed() {
        when(productRepository.findById(product.getProductNo()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService
                .getProductDetailById(product.getProductNo()))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    @DisplayName("상품 생성 성공")
    void createProduct() {
        when(productPolicyRepository.findById(productPolicy.getPolicyNo()))
                .thenReturn(Optional.ofNullable(productPolicy));
        when(typeStateCodeRepository.findById(typeStateCode.getCodeNo()))
                .thenReturn(Optional.ofNullable(typeStateCode));
        when(saleStateCodeRepository.findById(saleStateCode.getCodeNo()))
                .thenReturn(Optional.ofNullable(saleStateCode));
        when(authorRepository.findById(any()))
                .thenReturn(Optional.ofNullable(author));
        when(productRepository.findById(any()))
                .thenReturn(Optional.ofNullable(product));
        when(productRepository.save(any()))
                .thenReturn(product);

        when(categoryRepository.findById(anyInt()))
                .thenReturn(Optional.of(category));
        when(tagRepository.findById(anyInt()))
                .thenReturn(Optional.of(tag));

        productService.createProduct(requestDto);

        verify(productRepository, times(1))
                .save(captor.capture());

        Product result = captor.getValue();

        assertThat(result.getTitle()).isEqualTo(product.getTitle());
    }

    @Test
    @DisplayName("상품 등록 실패 - 정책 미기입")
    void createProductFailedByPolicy() {
        when(productPolicyRepository.findById(productPolicy.getPolicyNo()))
                .thenReturn(Optional.empty());
        when(typeStateCodeRepository.findById(typeStateCode.getCodeNo()))
                .thenReturn(Optional.ofNullable(typeStateCode));
        when(saleStateCodeRepository.findById(saleStateCode.getCodeNo()))
                .thenReturn(Optional.ofNullable(saleStateCode));
        when(productRepository.save(any()))
                .thenReturn(product);

        assertThatThrownBy(() -> productService.createProduct(requestDto))
                .isInstanceOf(NotFoundProductPolicyException.class);
    }

    @Test
    @DisplayName("상품 등록 실패 - 유형 미기입")
    void createProductFailedByType() {
        when(productPolicyRepository.findById(productPolicy.getPolicyNo()))
                .thenReturn(Optional.ofNullable(productPolicy));
        when(typeStateCodeRepository.findById(typeStateCode.getCodeNo()))
                .thenReturn(Optional.empty());
        when(saleStateCodeRepository.findById(saleStateCode.getCodeNo()))
                .thenReturn(Optional.ofNullable(saleStateCode));
        when(productRepository.save(any()))
                .thenReturn(product);

        assertThatThrownBy(() -> productService.createProduct(requestDto))
                .isInstanceOf(NotFoundStateCodeException.class);
    }

    @Test
    @DisplayName("상품 등록 실패 - 적립상태 미기입")
    void createProductFailedBySale() {
        when(productPolicyRepository.findById(productPolicy.getPolicyNo()))
                .thenReturn(Optional.ofNullable(productPolicy));
        when(typeStateCodeRepository.findById(typeStateCode.getCodeNo()))
                .thenReturn(Optional.ofNullable(typeStateCode));
        when(saleStateCodeRepository.findById(saleStateCode.getCodeNo()))
                .thenReturn(Optional.empty());
        when(productRepository.save(any()))
                .thenReturn(product);

        assertThatThrownBy(() -> productService.createProduct(requestDto))
                .isInstanceOf(NotFoundStateCodeException.class);
    }

    @Test
    @DisplayName("모든 상품 조회 성공")
    void getAllProducts() {
        List<GetProductListResponseDto> responses = new ArrayList<>();
        responses.add(listResponseDto);

        Pageable pageable = Pageable.ofSize(5);
        Page<GetProductListResponseDto> page =
                PageableExecutionUtils.getPage(responses, pageable, () -> 1L);

        when(productRepository.getAllProducts(pageable))
                .thenReturn(page);

        assertThat(productService.getAllProducts(pageable))
                .isEqualTo(page);
        assertThat(productService.getAllProducts(pageable)
                .getContent().get(0).getProductNo())
                .isEqualTo(listResponseDto.getProductNo());
        assertThat(productService.getAllProducts(pageable)
                .getContent().get(0).getSaleRate())
                .isEqualTo(listResponseDto.getSaleRate());
        assertThat(productService.getAllProducts(pageable)
                .getContent().get(0).getTitle())
                .isEqualTo(listResponseDto.getTitle());
        assertThat(productService.getAllProducts(pageable)
                .getContent().get(0).getSalesPrice())
                .isEqualTo(listResponseDto.getSalesPrice());
    }

    @Test
    @DisplayName("모든 상품 조회 실패, 결과가 0개")
    void getAllProductsFailNotFound() {
        List<GetProductListResponseDto> responses = new ArrayList<>();
        responses.add(listResponseDto);

        Pageable pageable = Pageable.ofSize(5);

        when(productRepository.getAllProducts(pageable))
                .thenReturn(PageableExecutionUtils.getPage(
                        Collections.EMPTY_LIST,
                        pageable,
                        () -> 0L));

        assertThatThrownBy(() -> productService.getAllProducts(pageable))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    @DisplayName("제목으로 상품 검색 조회 성공")
    void getProductListLikeTitle() {
        List<GetProductListResponseDto> responses = new ArrayList<>();
        responses.add(listResponseDto);

        Pageable pageable = Pageable.ofSize(5);
        Page<GetProductListResponseDto> page =
                PageableExecutionUtils.getPage(responses, pageable, () -> 1L);

        when(productRepository.getProductListLikeTitle("tes", pageable))
                .thenReturn(page);

        assertThat(productService.getProductListLikeTitle("tes", pageable))
                .isEqualTo(page);
        assertThat(productService.getProductListLikeTitle("tes", pageable)
                .getContent().get(0).getProductNo())
                .isEqualTo(listResponseDto.getProductNo());
        assertThat(productService.getProductListLikeTitle("tes", pageable)
                .getContent().get(0).getSaleRate())
                .isEqualTo(listResponseDto.getSaleRate());
        assertThat(productService.getProductListLikeTitle("tes", pageable)
                .getContent().get(0).getTitle())
                .isEqualTo(listResponseDto.getTitle());
        assertThat(productService.getProductListLikeTitle("tes", pageable)
                .getContent().get(0).getSalesPrice())
                .isEqualTo(listResponseDto.getSalesPrice());
    }

    @Test
    @DisplayName("상품 수정 성공")
    void modifyProductSuccess() {
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(product));
        when(productPolicyRepository.findById(anyInt()))
                .thenReturn(Optional.ofNullable(productPolicy));
        when(typeStateCodeRepository.findById(anyInt()))
                .thenReturn(Optional.ofNullable(typeStateCode));
        when(saleStateCodeRepository.findById(anyInt()))
                .thenReturn(Optional.ofNullable(saleStateCode));

        productService.modifyProduct(requestDto, 1L);

        verify(productRepository, times(1))
                .save(captor.capture());

        Product result = captor.getValue();

        Assertions.assertThat(requestDto.getTitle())
                .isEqualTo(result.getTitle());
    }

    @Test
    @DisplayName("상품 수정 실패 - 정책 미기입")
    void modifyProductFailedByPolicy() {
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.of(product));
        when(productPolicyRepository.findById(anyInt()))
                .thenReturn(Optional.empty());
        when(typeStateCodeRepository.findById(anyInt()))
                .thenReturn(Optional.of(typeStateCode));
        when(saleStateCodeRepository.findById(anyInt()))
                .thenReturn(Optional.of(saleStateCode));

        assertThatThrownBy(() -> productService.modifyProduct(requestDto, 1L))
                .isInstanceOf(NotFoundProductPolicyException.class);
    }

    @Test
    @DisplayName("상품 수정 실패 - 유형 미기입")
    void modifyProductFailedByType() {
        when(productPolicyRepository.findById(anyInt()))
                .thenReturn(Optional.ofNullable(productPolicy));
        when(typeStateCodeRepository.findById(anyInt()))
                .thenReturn(Optional.empty());
        when(saleStateCodeRepository.findById(anyInt()))
                .thenReturn(Optional.ofNullable(saleStateCode));
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(product));
        when(productRepository.save(any()))
                .thenReturn(product);

        assertThatThrownBy(() -> productService.modifyProduct(requestDto, 1L))
                .isInstanceOf(NotFoundStateCodeException.class);
    }

    @Test
    @DisplayName("상품 수정 실패 - 적립유형 미기입")
    void modifyProductFailedBySale() {
        when(productPolicyRepository.findById(anyInt()))
                .thenReturn(Optional.of(productPolicy));
        when(typeStateCodeRepository.findById(anyInt()))
                .thenReturn(Optional.of(typeStateCode));
        when(saleStateCodeRepository.findById(anyInt()))
                .thenReturn(Optional.empty());
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.of(product));
        when(productRepository.save(any()))
                .thenReturn(product);

        assertThatThrownBy(() -> productService.modifyProduct(requestDto, 1L))
                .isInstanceOf(NotFoundStateCodeException.class);
    }

    @Test
    @DisplayName("상품 수정 실패 - 없는 상품")
    void modifyProductFailedByProduct() {
        when(productPolicyRepository.findById(productPolicy.getPolicyNo()))
                .thenReturn(Optional.ofNullable(productPolicy));
        when(typeStateCodeRepository.findById(typeStateCode.getCodeNo()))
                .thenReturn(Optional.ofNullable(typeStateCode));
        when(saleStateCodeRepository.findById(saleStateCode.getCodeNo()))
                .thenReturn(Optional.ofNullable(saleStateCode));
        when(productRepository.findById(product.getProductNo()))
                .thenReturn(Optional.empty());
        when(productRepository.save(any()))
                .thenReturn(product);

        assertThatThrownBy(() -> productService.modifyProduct(requestDto, 1L))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    @DisplayName("삭제여부 설정 성공")
    void setDeleteProduct() {
        ReflectionTestUtils.setField(product, "productNo", 1L);

        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.of(product));
        ReflectionTestUtils.setField(product, "productNo", 1L);
        productService.setDeleteProduct(product.getProductNo());

        verify(productRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("삭제여부 설정 실패, 없는 상품")
    void setDeleteProductFail() {
        when(productRepository.findById(product.getProductNo()))
                .thenReturn(Optional.empty());
        when(productPolicyRepository.findById(productPolicy.getPolicyNo()))
                .thenReturn(Optional.ofNullable(productPolicy));
        when(typeStateCodeRepository.findById(typeStateCode.getCodeNo()))
                .thenReturn(Optional.ofNullable(typeStateCode));
        when(saleStateCodeRepository.findById(saleStateCode.getCodeNo()))
                .thenReturn(Optional.ofNullable(saleStateCode));

        assertThatThrownBy(() ->
                productService.setDeleteProduct(product.getProductNo()))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    @DisplayName("상품 유형별 상품 조회 테스트")
    void getProductsByType() {
        // given
        GetProductByTypeResponseDto response = new GetProductByTypeResponseDto();
        ReflectionTestUtils.setField(response, "productNo", product.getProductNo());
        ReflectionTestUtils.setField(response, "title", product.getTitle());
        ReflectionTestUtils.setField(response, "salesPrice", product.getSalesPrice());
        ReflectionTestUtils.setField(response, "productPrice", product.getProductPrice());
        ReflectionTestUtils.setField(response, "salesRate", product.getSalesRate());
        ReflectionTestUtils.setField(response, "productCategories", List.of("1", "2"));

        List<GetProductByTypeResponseDto> list = List.of(response);

        // when
        when(productRepository.findProductListByType(1, 1))
                .thenReturn(list);
        productService.getProductsByType(1, 1);

        // then
        verify(productRepository, times(1))
                .findProductListByType(1, 1);
    }
}