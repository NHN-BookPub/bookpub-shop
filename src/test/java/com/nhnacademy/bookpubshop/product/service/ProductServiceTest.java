package com.nhnacademy.bookpubshop.product.service;

import static com.nhnacademy.bookpubshop.state.ProductTypeState.BEST_SELLER;
import static com.nhnacademy.bookpubshop.state.ProductTypeState.NEW;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import com.nhnacademy.bookpubshop.author.entity.Author;
import com.nhnacademy.bookpubshop.author.repository.AuthorRepository;
import com.nhnacademy.bookpubshop.product.dto.CreateProductRequestDto;
import com.nhnacademy.bookpubshop.product.dto.GetProductDetailResponseDto;
import com.nhnacademy.bookpubshop.product.dto.GetProductListResponseDto;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.exception.NotFoundProductPolicyException;
import com.nhnacademy.bookpubshop.product.exception.NotFoundStateCodeException;
import com.nhnacademy.bookpubshop.product.exception.ProductNotFoundException;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductAuthor;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import com.nhnacademy.bookpubshop.product.relationship.repository.ProductAuthorRepository;
import com.nhnacademy.bookpubshop.product.relationship.repository.ProductPolicyRepository;
import com.nhnacademy.bookpubshop.product.relationship.repository.ProductSaleStateCodeRepository;
import com.nhnacademy.bookpubshop.product.relationship.repository.ProductTypeStateCodeRepository;
import com.nhnacademy.bookpubshop.product.repository.ProductRepository;
import com.nhnacademy.bookpubshop.product.service.impl.ProductServiceImpl;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * 상품 서비스 테스트.
 *
 * @author : 여운석
 * @since : 1.0
 **/
class ProductServiceTest {
    ProductPolicyRepository productPolicyRepository;
    ProductSaleStateCodeRepository saleStateCodeRepository;
    ProductTypeStateCodeRepository typeStateCodeRepository;
    ProductAuthorRepository productAuthorRepository;
    AuthorRepository authorRepository;
    ProductRepository productRepository;
    ProductService productService;
    Product product;
    CreateProductRequestDto requestDto;
    GetProductDetailResponseDto responseDto;
    GetProductListResponseDto listResponseDto;
    ProductPolicy productPolicy;
    ProductTypeStateCode typeStateCode;
    ProductSaleStateCode saleStateCode;
    ArgumentCaptor<Product> captor;
    Author author;

    @BeforeEach
    void setUp() {
        productRepository = Mockito.mock(ProductRepository.class);
        productPolicyRepository = Mockito.mock(ProductPolicyRepository.class);
        saleStateCodeRepository = Mockito.mock(ProductSaleStateCodeRepository.class);
        typeStateCodeRepository = Mockito.mock(ProductTypeStateCodeRepository.class);
        productAuthorRepository = Mockito.mock(ProductAuthorRepository.class);
        authorRepository = Mockito.mock(AuthorRepository.class);

        productService = new ProductServiceImpl(
                productRepository,
                productPolicyRepository,
                saleStateCodeRepository,
                typeStateCodeRepository,
                productAuthorRepository,
                authorRepository);

        productPolicy = new ProductPolicy(1,"method",true,1);
        typeStateCode = new ProductTypeStateCode(1,BEST_SELLER.getName(),BEST_SELLER.isUsed(),"info");
        saleStateCode = new ProductSaleStateCode(1, NEW.getName(),NEW.isUsed(),"info");

        product = new Product(1L,
                productPolicy,
                typeStateCode,
                saleStateCode,
                Collections.EMPTY_LIST,
                "1231231233",
                "test",
                "test_publisher",
                130,
                "test_description",
                "thumbnail.png",
                "test.txt",
                8000L,
                10000L,
                20,
                0L,
                10,
                false,
                100,
                LocalDateTime.now(),
                false);

        requestDto = new CreateProductRequestDto();
        responseDto = new GetProductDetailResponseDto(
                product.getProductNo(),
                product.getProductIsbn(),
                product.getTitle(),
                product.getPageCount(),
                product.getProductDescription(),
                product.getProductThumbnail(),
                product.getSalesPrice(),
                product.getSalesRate(),
                product.getProductPriority(),
                product.getProductStock(),
                product.getPublishDate(),
                product.isProductDeleted(),
                product.isProductSubscribed(),
                product.getProductSaleStateCode(),
                product.getProductTypeStateCode(),
                product.getProductPolicy());

        listResponseDto = new GetProductListResponseDto(
                product.getProductNo(),
                product.getProductThumbnail(),
                product.getTitle(),
                product.getProductStock(),
                product.getSalesPrice(),
                product.getSalesRate(),
                product.isProductDeleted(),
                product.getPublishDate());

        author = new Author(1, "test");

        ReflectionTestUtils.setField(requestDto, "productIsbn", product.getProductIsbn());
        ReflectionTestUtils.setField(requestDto, "title", product.getTitle());
        ReflectionTestUtils.setField(requestDto, "productPublisher", product.getProductPublisher());
        ReflectionTestUtils.setField(requestDto, "pageCount", product.getPageCount());
        ReflectionTestUtils.setField(requestDto, "productDescription", product.getProductDescription());
        ReflectionTestUtils.setField(requestDto, "thumbnailPath", product.getProductThumbnail());
        ReflectionTestUtils.setField(requestDto, "ebookPath", product.getEbookFilePath());
        ReflectionTestUtils.setField(requestDto, "salePrice", product.getSalesPrice());
        ReflectionTestUtils.setField(requestDto, "productPrice", product.getProductPrice());
        ReflectionTestUtils.setField(requestDto, "productPriority", product.getProductPriority());
        ReflectionTestUtils.setField(requestDto, "productStock", product.getProductStock());
        ReflectionTestUtils.setField(requestDto, "publishedAt", product.getPublishDate());
        ReflectionTestUtils.setField(requestDto, "productPolicyNo", product.getProductPolicy().getPolicyNo());
        ReflectionTestUtils.setField(requestDto, "saleCodeNo", product.getProductSaleStateCode().getCodeNumber());
        ReflectionTestUtils.setField(requestDto, "typeCodeNo", product.getProductTypeStateCode().getCodeNo());
        ReflectionTestUtils.setField(requestDto, "authorNos", Collections.EMPTY_LIST);
        ReflectionTestUtils.setField(requestDto, "relationProducts", product.getRelationProduct());

        captor = ArgumentCaptor.forClass(Product.class);

    }

    @Test
    @DisplayName("상품 번호로 조회 성공")
    void getProductDetailById() {
        when(productRepository.getProductDetailById(product.getProductNo()))
                .thenReturn(Optional.ofNullable(responseDto));

        assertThat(productService.getProductDetailById(product.getProductNo()).getProductNo())
                .isEqualTo(product.getProductNo());
        assertThat(productService.getProductDetailById(product.getProductNo()).getProductDescription())
                .isEqualTo(product.getProductDescription());
        assertThat(productService.getProductDetailById(product.getProductNo()).getProductIsbn())
                .isEqualTo(product.getProductIsbn());
        assertThat(productService.getProductDetailById(product.getProductNo()).getProductPriority())
                .isEqualTo(product.getProductPriority());
        assertThat(productService.getProductDetailById(product.getProductNo()).getProductStock())
                .isEqualTo(product.getProductStock());
        assertThat(productService.getProductDetailById(product.getProductNo()).getProductThumbnail())
                .isEqualTo(product.getProductThumbnail());
        assertThat(productService.getProductDetailById(product.getProductNo()).getPublishDate())
                .isEqualTo(product.getPublishDate());
        assertThat(productService.getProductDetailById(product.getProductNo()).getPageCount())
                .isEqualTo(product.getPageCount());
        assertThat(productService.getProductDetailById(product.getProductNo()).getProductPolicy().getPolicyNo())
                .isEqualTo(product.getProductPolicy().getPolicyNo());
        assertThat(productService.getProductDetailById(product.getProductNo()).getSaleCode().getCodeNumber())
                .isEqualTo(product.getProductSaleStateCode().getCodeNumber());
        assertThat(productService.getProductDetailById(product.getProductNo()).getTypeCode().getCodeNo())
                .isEqualTo(product.getProductTypeStateCode().getCodeNo());
        assertThat(productService.getProductDetailById(product.getProductNo()).getTitle())
                .isEqualTo(product.getTitle());
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
        when(saleStateCodeRepository.findById(saleStateCode.getCodeNumber()))
                .thenReturn(Optional.ofNullable(saleStateCode));
        when(authorRepository.findById(any()))
                .thenReturn(Optional.ofNullable(author));
        when(productRepository.findById(any()))
                .thenReturn(Optional.ofNullable(product));
        when(productRepository.save(any()))
                .thenReturn(product);

        List<Integer> authorNos = new ArrayList<>();
        authorNos.add(1);

        ProductAuthor productAuthor = new ProductAuthor(new ProductAuthor.Pk(
                authorNos.get(0), product.getProductNo()), author, product);

        ReflectionTestUtils.setField(requestDto, "authorNos", authorNos);

        when(productAuthorRepository.save(
                new ProductAuthor(new ProductAuthor.Pk(
                        authorNos.get(0), product.getProductNo()), author, product)))
                .thenReturn(productAuthor);

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
        when(saleStateCodeRepository.findById(saleStateCode.getCodeNumber()))
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
        when(saleStateCodeRepository.findById(saleStateCode.getCodeNumber()))
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
        when(saleStateCodeRepository.findById(saleStateCode.getCodeNumber()))
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
                .getContent().get(0).getThumbnailPath())
                .isEqualTo(listResponseDto.getThumbnailPath());
        assertThat(productService.getAllProducts(pageable)
                .getContent().get(0).getSaleRate())
                .isEqualTo(listResponseDto.getSaleRate());
        assertThat(productService.getAllProducts(pageable)
                .getContent().get(0).getTitle())
                .isEqualTo(listResponseDto.getTitle());
        assertThat(productService.getAllProducts(pageable)
                .getContent().get(0).getPublishedAt())
                .isEqualTo(listResponseDto.getPublishedAt());
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
                .getContent().get(0).getThumbnailPath())
                .isEqualTo(listResponseDto.getThumbnailPath());
        assertThat(productService.getProductListLikeTitle("tes", pageable)
                .getContent().get(0).getSaleRate())
                .isEqualTo(listResponseDto.getSaleRate());
        assertThat(productService.getProductListLikeTitle("tes", pageable)
                .getContent().get(0).getTitle())
                .isEqualTo(listResponseDto.getTitle());
        assertThat(productService.getProductListLikeTitle("tes", pageable)
                .getContent().get(0).getPublishedAt())
                .isEqualTo(listResponseDto.getPublishedAt());
        assertThat(productService.getProductListLikeTitle("tes", pageable)
                .getContent().get(0).getSalesPrice())
                .isEqualTo(listResponseDto.getSalesPrice());
    }

    @Test
    @DisplayName("상품 수정 성공")
    void modifyProductSuccess() {
        when(productPolicyRepository.findById(productPolicy.getPolicyNo()))
                .thenReturn(Optional.ofNullable(productPolicy));
        when(typeStateCodeRepository.findById(typeStateCode.getCodeNo()))
                .thenReturn(Optional.ofNullable(typeStateCode));
        when(saleStateCodeRepository.findById(saleStateCode.getCodeNumber()))
                .thenReturn(Optional.ofNullable(saleStateCode));
        when(productRepository.findById(product.getProductNo()))
                .thenReturn(Optional.ofNullable(product));
        when(productRepository.save(any()))
                .thenReturn(product);

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
        when(productPolicyRepository.findById(productPolicy.getPolicyNo()))
                .thenReturn(Optional.empty());
        when(typeStateCodeRepository.findById(typeStateCode.getCodeNo()))
                .thenReturn(Optional.ofNullable(typeStateCode));
        when(saleStateCodeRepository.findById(saleStateCode.getCodeNumber()))
                .thenReturn(Optional.ofNullable(saleStateCode));
        when(productRepository.findById(product.getProductNo()))
                .thenReturn(Optional.ofNullable(product));
        when(productRepository.save(any()))
                .thenReturn(product);

        assertThatThrownBy(() -> productService.modifyProduct(requestDto, 1L))
                .isInstanceOf(NotFoundProductPolicyException.class);
    }

    @Test
    @DisplayName("상품 수정 실패 - 유형 미기입")
    void modifyProductFailedByType() {
        when(productPolicyRepository.findById(productPolicy.getPolicyNo()))
                .thenReturn(Optional.ofNullable(productPolicy));
        when(typeStateCodeRepository.findById(typeStateCode.getCodeNo()))
                .thenReturn(Optional.empty());
        when(saleStateCodeRepository.findById(saleStateCode.getCodeNumber()))
                .thenReturn(Optional.ofNullable(saleStateCode));
        when(productRepository.findById(product.getProductNo()))
                .thenReturn(Optional.ofNullable(product));
        when(productRepository.save(any()))
                .thenReturn(product);

        assertThatThrownBy(() -> productService.modifyProduct(requestDto, 1L))
                .isInstanceOf(NotFoundStateCodeException.class);
    }

    @Test
    @DisplayName("상품 수정 실패 - 적립유형 미기입")
    void modifyProductFailedBySale() {
        when(productPolicyRepository.findById(productPolicy.getPolicyNo()))
                .thenReturn(Optional.ofNullable(productPolicy));
        when(typeStateCodeRepository.findById(typeStateCode.getCodeNo()))
                .thenReturn(Optional.ofNullable(typeStateCode));
        when(saleStateCodeRepository.findById(saleStateCode.getCodeNumber()))
                .thenReturn(Optional.empty());
        when(productRepository.findById(product.getProductNo()))
                .thenReturn(Optional.ofNullable(product));
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
        when(saleStateCodeRepository.findById(saleStateCode.getCodeNumber()))
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
        when(productRepository.findById(product.getProductNo()))
                .thenReturn(Optional.ofNullable(product));
        when(productPolicyRepository.findById(productPolicy.getPolicyNo()))
                .thenReturn(Optional.ofNullable(productPolicy));
        when(typeStateCodeRepository.findById(typeStateCode.getCodeNo()))
                .thenReturn(Optional.ofNullable(typeStateCode));
        when(saleStateCodeRepository.findById(saleStateCode.getCodeNumber()))
                .thenReturn(Optional.ofNullable(saleStateCode));

        productService.setDeleteProduct(product.getProductNo(), false);

        verify(productRepository, times(1))
                .save(captor.capture());

        Product result = captor.getValue();

        Assertions.assertThat(requestDto.getTitle())
                .isEqualTo(result.getTitle());

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
        when(saleStateCodeRepository.findById(saleStateCode.getCodeNumber()))
                .thenReturn(Optional.ofNullable(saleStateCode));

        assertThatThrownBy( () ->
                productService.setDeleteProduct(product.getProductNo(), false))
                .isInstanceOf(ProductNotFoundException.class);
    }
}