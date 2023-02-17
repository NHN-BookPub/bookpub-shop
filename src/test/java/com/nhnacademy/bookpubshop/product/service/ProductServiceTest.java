package com.nhnacademy.bookpubshop.product.service;

import static com.nhnacademy.bookpubshop.state.ProductTypeState.BEST_SELLER;
import static com.nhnacademy.bookpubshop.state.ProductTypeState.NEW;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

import com.nhnacademy.bookpubshop.author.dummy.AuthorDummy;
import com.nhnacademy.bookpubshop.author.entity.Author;
import com.nhnacademy.bookpubshop.author.exception.NotFoundAuthorException;
import com.nhnacademy.bookpubshop.author.repository.AuthorRepository;
import com.nhnacademy.bookpubshop.category.dummy.CategoryDummy;
import com.nhnacademy.bookpubshop.category.entity.Category;
import com.nhnacademy.bookpubshop.category.exception.CategoryNotFoundException;
import com.nhnacademy.bookpubshop.category.repository.CategoryRepository;
import com.nhnacademy.bookpubshop.file.dummy.FileDummy;
import com.nhnacademy.bookpubshop.file.entity.File;
import com.nhnacademy.bookpubshop.file.exception.FileNotFoundException;
import com.nhnacademy.bookpubshop.filemanager.FileManagement;
import com.nhnacademy.bookpubshop.product.dto.request.CreateProductRequestDto;
import com.nhnacademy.bookpubshop.product.dto.request.ModifyProductAuthorRequestDto;
import com.nhnacademy.bookpubshop.product.dto.request.ModifyProductCategoryRequestDto;
import com.nhnacademy.bookpubshop.product.dto.request.ModifyProductDescriptionRequestDto;
import com.nhnacademy.bookpubshop.product.dto.request.ModifyProductInfoRequestDto;
import com.nhnacademy.bookpubshop.product.dto.request.ModifyProductTagRequestDto;
import com.nhnacademy.bookpubshop.product.dto.response.GetProductByCategoryResponseDto;
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
import com.nhnacademy.bookpubshop.product.relationship.repository.ProductCategoryRepository;
import com.nhnacademy.bookpubshop.product.relationship.repository.ProductPolicyRepository;
import com.nhnacademy.bookpubshop.product.relationship.repository.ProductSaleStateCodeRepository;
import com.nhnacademy.bookpubshop.product.relationship.repository.ProductTypeStateCodeRepository;
import com.nhnacademy.bookpubshop.product.repository.ProductRepository;
import com.nhnacademy.bookpubshop.product.service.impl.ProductServiceImpl;
import com.nhnacademy.bookpubshop.state.FileCategory;
import com.nhnacademy.bookpubshop.tag.dummy.TagDummy;
import com.nhnacademy.bookpubshop.tag.entity.Tag;
import com.nhnacademy.bookpubshop.tag.exception.TagNotFoundException;
import com.nhnacademy.bookpubshop.tag.repository.TagRepository;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

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
    @MockBean
    ProductCategoryRepository productCategoryRepository;

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
    Map<String, MultipartFile> files = new HashMap<>();
    @MockBean
    FileManagement fileManagement;

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

        List<File> filesDummy = new ArrayList<>();
        filesDummy.add(FileDummy.dummy(
                null,
                null,
                null,
                product,
                null,
                FileCategory.PRODUCT_THUMBNAIL, null));
        filesDummy.add(FileDummy.dummy(
                null,
                null,
                null,
                product,
                null,
                FileCategory.PRODUCT_DETAIL, null));
        filesDummy.add(FileDummy.dummy(
                null,
                null,
                null,
                product,
                null,
                FileCategory.PRODUCT_EBOOK, null));

        product.setProductFiles(filesDummy);

        requestDto = new CreateProductRequestDto();

        listResponseDto = new GetProductListResponseDto(
                product.getProductNo(),
                product.getTitle(),
                product.getFiles().get(0).getFilePath(),
                product.getProductStock(),
                product.getSalesPrice(),
                product.getSalesRate(),
                product.isProductSubscribed(),
                product.getProductPrice(),
                product.isProductDeleted());


        List<Long> relation = new ArrayList<>();

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
    void createProduct() throws IOException {
        String imageContent = "234kh2kl4h2l34k2j34hlk23h4";
        MultipartFile multipartFile = new MockMultipartFile("image", "imageName.jpeg", "image/*", imageContent.getBytes());

        Map<String, MultipartFile> fileMap = new HashMap<>();
        fileMap.put("detail", multipartFile);
        fileMap.put("ebook", multipartFile);

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

        productService.createProduct(requestDto, fileMap);

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

        assertThatThrownBy(() -> productService.createProduct(requestDto, files))
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

        assertThatThrownBy(() -> productService.createProduct(requestDto, files))
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

        assertThatThrownBy(() -> productService.createProduct(requestDto, files))
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

        then(productRepository).should().findById(1L);
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

    @Test
    @DisplayName("카트에 담긴 상품 조회 테스트")
    void getProductsInCart() {
        // given
        GetProductDetailResponseDto dto = new GetProductDetailResponseDto();
        ReflectionTestUtils.setField(dto, "productNo", 1L);
        ReflectionTestUtils.setField(dto, "title", "설명");
        ReflectionTestUtils.setField(dto, "productPublisher", "출판");
        ReflectionTestUtils.setField(dto, "salesPrice", 1000L);
        ReflectionTestUtils.setField(dto, "productPrice", 2000L);

        List<GetProductDetailResponseDto> list = List.of(dto);

        // when
        when(productRepository.getProductsInCart(List.of(dto.getProductNo())))
                .thenReturn(list);
        productService.getProductsInCart(List.of(dto.getProductNo()));

        // then
        verify(productRepository, times(1))
                .getProductsInCart(List.of(dto.getProductNo()));
    }

    @Test
    @DisplayName("카테고리별 상품 조회 테스트")
    void getProductsByCategory() {
        // given
        GetProductByCategoryResponseDto dto = new GetProductByCategoryResponseDto();
        ReflectionTestUtils.setField(dto, "productNo", 1L);
        ReflectionTestUtils.setField(dto, "title", "제목");
        ReflectionTestUtils.setField(dto, "salesPrice", 1000L);
        ReflectionTestUtils.setField(dto, "categories", List.of("요리도서"));
        ReflectionTestUtils.setField(dto, "authors", List.of("저자 1"));

        List<GetProductByCategoryResponseDto> response = List.of(dto);
        Pageable pageable = Pageable.ofSize(5);

        // when
        Page<GetProductByCategoryResponseDto> page =
                PageableExecutionUtils.getPage(response, pageable, () -> 1L);

        // then
        productService.getProductsByCategory(1, pageable);

        verify(productRepository, times(1))
                .getProductsByCategory(1, pageable);
    }

    @Test
    @DisplayName("E-book 조회")
    void getEbooks() {
        GetProductByCategoryResponseDto dto = new GetProductByCategoryResponseDto();
        ReflectionTestUtils.setField(dto, "productNo", 1L);
        ReflectionTestUtils.setField(dto, "title", "제목");
        ReflectionTestUtils.setField(dto, "thumbnail", "thumbnail");
        ReflectionTestUtils.setField(dto, "salesPrice", 1000L);
        ReflectionTestUtils.setField(dto, "categories", List.of("요리도서"));
        ReflectionTestUtils.setField(dto, "authors", List.of("저자 1"));

        List<GetProductByCategoryResponseDto> response = List.of(dto);
        Pageable pageable = Pageable.ofSize(5);

        Page<GetProductByCategoryResponseDto> page =
                PageableExecutionUtils.getPage(response, pageable, () -> 1L);

        productService.getEbooks(pageable);

        verify(productRepository, times(1))
                .getEbooks(pageable);
    }

    @Test
    @DisplayName("상품 정보 수정 성공 테스트")
    void modify_productInfo_Success_Test() {
        // given
        ModifyProductInfoRequestDto dto = new ModifyProductInfoRequestDto();
        ReflectionTestUtils.setField(dto, "productIsbn", "12312312312");
        ReflectionTestUtils.setField(dto, "productPrice", 1000L);
        ReflectionTestUtils.setField(dto, "salesRate", 10);
        ReflectionTestUtils.setField(dto, "productPublisher", "출판사");
        ReflectionTestUtils.setField(dto, "publishedAt", LocalDateTime.now());
        ReflectionTestUtils.setField(dto, "pageCount", 12);
        ReflectionTestUtils.setField(dto, "salesPrice", 800L);
        ReflectionTestUtils.setField(dto, "priority", 10);

        // when
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.of(product));

        // then
        productService.modifyProductInfo(1L, dto);

        verify(productRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("상품 정보 수정 실패 테스트(상품이 없는 경우)")
    void modify_productInfo_Fail_Test() {
        // given
        ModifyProductInfoRequestDto dto = new ModifyProductInfoRequestDto();
        ReflectionTestUtils.setField(dto, "productIsbn", "12312312312");
        ReflectionTestUtils.setField(dto, "productPrice", 1000L);
        ReflectionTestUtils.setField(dto, "salesRate", 10);
        ReflectionTestUtils.setField(dto, "productPublisher", "출판사");
        ReflectionTestUtils.setField(dto, "publishedAt", LocalDateTime.now());
        ReflectionTestUtils.setField(dto, "pageCount", 12);
        ReflectionTestUtils.setField(dto, "salesPrice", 800L);
        ReflectionTestUtils.setField(dto, "priority", 10);

        // when
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> productService.modifyProductInfo(1L, dto))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining(ProductNotFoundException.MESSAGE);
    }

    @Test
    @DisplayName("상품 카테고리 수정 성공 테스트")
    void modify_productCategory_Success_Test() {
        // given
        ModifyProductCategoryRequestDto dto = new ModifyProductCategoryRequestDto();
        ReflectionTestUtils.setField(dto, "categoriesNo", List.of(1));

        // when
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.of(product));
        when(categoryRepository.findById(anyInt()))
                .thenReturn(Optional.of(category));

        // then
        productService.modifyProductCategory(1L, dto);

        verify(productRepository, times(1))
                .findById(anyLong());
        verify(categoryRepository, times(1))
                .findById(anyInt());
    }

    @Test
    @DisplayName("상품 카테고리 수정 실패 테스트 (not found product)")
    void modify_productCategory_Fail_Test_NotFoundProduct() {
        // given
        ModifyProductCategoryRequestDto dto = new ModifyProductCategoryRequestDto();

        // when
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> productService.modifyProductCategory(1L, dto))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining(ProductNotFoundException.MESSAGE);
    }

    @Test
    @DisplayName("상품 카테고리 수정 실패 테스트 (not found category)")
    void modify_productCategory_Fail_Test_NotFoundCategory() {
        // given
        ModifyProductCategoryRequestDto dto = new ModifyProductCategoryRequestDto();
        ReflectionTestUtils.setField(dto, "categoriesNo", List.of(1));

        // when
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.of(product));
        when(categoryRepository.findById(anyInt()))
                .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> productService.modifyProductCategory(1L, dto))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessageContaining(CategoryNotFoundException.MESSAGE);
    }

    @Test
    @DisplayName("상품 저자 수정 성공 테스트")
    void modify_productAuthor_Success_Test() {
        // given
        ModifyProductAuthorRequestDto dto = new ModifyProductAuthorRequestDto();
        ReflectionTestUtils.setField(dto, "authors", List.of(1));

        // when
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.of(product));
        when(authorRepository.findById(anyInt()))
                .thenReturn(Optional.of(author));

        // then
        productService.modifyProductAuthor(1L, dto);

        verify(productRepository, times(1))
                .findById(anyLong());
        verify(authorRepository, times(1))
                .findById(anyInt());
    }

    @Test
    @DisplayName("상품 저자 수정 실패 테스트 (상품 없음)")
    void modify_productAuthor_Fail_Test_notFoundProduct() {
        // given
        ModifyProductAuthorRequestDto dto = new ModifyProductAuthorRequestDto();

        // when
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> productService.modifyProductAuthor(1L, dto))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining(ProductNotFoundException.MESSAGE);
    }

    @Test
    @DisplayName("상품 저자 수정 실패 테스트 (저자 없음)")
    void modify_productAuthor_Fail_Test_notFoundAuthor() {
        // given
        ModifyProductAuthorRequestDto dto = new ModifyProductAuthorRequestDto();
        ReflectionTestUtils.setField(dto, "authors", List.of(1));

        // when
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.of(product));
        when(authorRepository.findById(anyInt()))
                .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> productService.modifyProductAuthor(1L, dto))
                .isInstanceOf(NotFoundAuthorException.class)
                .hasMessageContaining(NotFoundAuthorException.MESSAGE);
    }

    @Test
    @DisplayName("상품 태그 수정 성공 테스트")
    void modify_productTag_Success_Test() {
        // given
        ModifyProductTagRequestDto dto = new ModifyProductTagRequestDto();
        ReflectionTestUtils.setField(dto, "tags", List.of(1));

        // when
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.of(product));
        when(tagRepository.findById(anyInt()))
                .thenReturn(Optional.of(tag));

        // then
        productService.modifyProductTag(1L, dto);

        verify(productRepository, times(1))
                .findById(anyLong());
        verify(tagRepository, times(1))
                .findById(anyInt());
    }

    @Test
    @DisplayName("상품 태그 수정 실패 테스트 (상품 없음)")
    void modify_productTag_Fail_Test_notFoundProduct() {
        // given
        ModifyProductTagRequestDto dto = new ModifyProductTagRequestDto();

        // when
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> productService.modifyProductTag(1L, dto))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining(ProductNotFoundException.MESSAGE);
    }

    @Test
    @DisplayName("상품 태그 수정 실패 테스트 (태그 없음)")
    void modify_productTag_Fail_Test_notFoundTag() {
        // given
        ModifyProductTagRequestDto dto = new ModifyProductTagRequestDto();
        ReflectionTestUtils.setField(dto, "tags", List.of(1));

        // when
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.of(product));
        when(tagRepository.findById(anyInt()))
                .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> productService.modifyProductTag(1L, dto))
                .isInstanceOf(TagNotFoundException.class)
                .hasMessageContaining(TagNotFoundException.ERROR_MESSAGE);
    }

    @Test
    @DisplayName("상품 유형 변경 성공 테스트")
    void modify_productType_success_test() {
        // given

        // when
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.of(product));
        when(typeStateCodeRepository.findById(anyInt()))
                .thenReturn(Optional.of(typeStateCode));

        // then
        productService.modifyProductType(1L, 1);

        verify(productRepository, times(1))
                .findById(anyLong());
        verify(typeStateCodeRepository, times(1))
                .findById(anyInt());
    }

    @Test
    @DisplayName("상품 유형 변경 실패 테스트 (상품 없음)")
    void modify_productType_fail_test_not_found_product() {
        // given

        // when
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> productService.modifyProductSale(1L, 1))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining(ProductNotFoundException.MESSAGE);
    }

    @Test
    @DisplayName("상품 유형 변경 실패 테스트 (없는 유형)")
    void modify_productType_fail_test_not_found_type() {
        // given

        // when
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.of(product));
        when(typeStateCodeRepository.findById(anyInt()))
                .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> productService.modifyProductType(1L, 1))
                .isInstanceOf(NotFoundStateCodeException.class)
                .hasMessageContaining(NotFoundStateCodeException.MESSAGE);
    }

    @Test
    @DisplayName("상품 판매 유형 변경 성공 테스트")
    void modify_productSale_success_test() {
        // given

        // when
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.of(product));
        when(saleStateCodeRepository.findById(anyInt()))
                .thenReturn(Optional.of(saleStateCode));

        // then
        productService.modifyProductSale(1L, 1);

        verify(productRepository, times(1))
                .findById(anyLong());
        verify(saleStateCodeRepository, times(1))
                .findById(anyInt());
    }

    @Test
    @DisplayName("상품 판매 유형 변경 실패 테스트 (상품 없음)")
    void modify_productSale_fail_test_not_found_product() {
        // given

        // when
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> productService.modifyProductSale(1L, 1))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining(ProductNotFoundException.MESSAGE);
    }

    @Test
    @DisplayName("상품 판메 유형 변경 실패 테스트 (유형 없음)")
    void modify_productSale_fail_test_not_fond_saleCode() {
        // given

        // when
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.of(product));
        when(saleStateCodeRepository.findById(anyInt()))
                .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> productService.modifyProductSale(1L, 1))
                .isInstanceOf(NotFoundStateCodeException.class)
                .hasMessageContaining(NotFoundStateCodeException.MESSAGE);
    }

    @Test
    @DisplayName("상품 포인트 정책 수정 성공 테스트")
    void modify_productPolicy_success_test() {
        // given

        // when
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.of(product));
        when(productPolicyRepository.findById(anyInt()))
                .thenReturn(Optional.of(productPolicy));

        // then
        productService.modifyProductPolicy(1L, 1);

        verify(productRepository, times(1))
                .findById(anyLong());
        verify(productPolicyRepository, times(1))
                .findById(anyInt());
    }

    @Test
    @DisplayName("상품 포인트 정책 수정 실패 테스트 (상품 없음)")
    void modify_productPolicy_fail_test_not_found_product() {
        // given

        // when
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> productService.modifyProductSale(1L, 1))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining(ProductNotFoundException.MESSAGE);
    }

    @Test
    @DisplayName("상품 포인트 정책 수정 실패 테스트 (정책 없음)")
    void modify_productPolicy_fail_test_not_found_policy() {
        // given

        // when
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.of(product));
        when(productPolicyRepository.findById(anyInt()))
                .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> productService.modifyProductPolicy(1L, 1))
                .isInstanceOf(NotFoundProductPolicyException.class)
                .hasMessageContaining(NotFoundProductPolicyException.MESSAGE);
    }

    @Test
    @DisplayName("상품 정보 조회 테스트")
    void getBookInfo() {

        // then
        productService.getEbookInfo(anyLong());

        verify(productRepository, times(1))
                .getFilePath(anyLong());
    }

    @Test
    @DisplayName("상품 설명 수정 성공 테스트")
    void modify_product_description_success_test() {
        // given
        ModifyProductDescriptionRequestDto dto = new ModifyProductDescriptionRequestDto();
        ReflectionTestUtils.setField(dto, "description", "a");

        // when
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.of(product));

        // then
        productService.modifyProductDescription(1L, dto);

        verify(productRepository, times(1))
                .findById(anyLong());
    }

    @Test
    @DisplayName("상픔 설명 수정 실패 테스트")
    void modify_product_description_fail_test() {
        // given

        // when
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> productService.modifyProductSale(1L, 1))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining(ProductNotFoundException.MESSAGE);
    }

    @Test
    @DisplayName("E-Book 수정 테스트")
    void modify_Ebook_success_test() throws IOException {
        // given
        String imageContent = "234kh2kl4h2l34k2j34hlk23h4";
        MultipartFile multipartFile = new MockMultipartFile("image", "imageName.jpeg", "image/*", imageContent.getBytes());

        // when
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.of(product));

        // then
        productService.modifyProductEbook(1L, multipartFile);

        verify(productRepository, times(1))
                .findById(anyLong());
        verify(fileManagement, times(1))
                .saveFile(any(), any(), any(), any(),
                        any(), any(), any(), anyString(), anyString());
    }

    @Test
    @DisplayName("E-book 수정 실패 (상품 없음)")
    void modify_ebook_fail_test_not_found_product() {
        // given

        // when
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> productService.modifyProductSale(1L, 1))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining(ProductNotFoundException.MESSAGE);
    }

    @Test
    @DisplayName("상품 이미지 수정 실패 (상품 없음)")
    void modify_image_fail_test_not_found_product() {
        // given

        // when
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> productService.modifyProductSale(1L, 1))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining(ProductNotFoundException.MESSAGE);
    }

    @Test
    @DisplayName("상품 상세 이미지 수정 실패 (상품 없음)")
    void modify_detailImage_fail_test_not_found_product() {
        // given

        // when
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> productService.modifyProductSale(1L, 1))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining(ProductNotFoundException.MESSAGE);
    }

    @Test
    @DisplayName("상품 이미지 추가 실패 (상품 없음)")
    void add_image_fail_test_not_found_product() {
        // given

        // when
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> productService.modifyProductSale(1L, 1))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining(ProductNotFoundException.MESSAGE);
    }

    @Test
    @DisplayName("상품 상세 이미지 추가 실패 (상품 없음)")
    void add_detailImage_fail_test_not_found_product() {
        // given

        // when
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> productService.modifyProductSale(1L, 1))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining(ProductNotFoundException.MESSAGE);
    }

    @Test
    @DisplayName("상품 이미지 수정 테스트")
    void modify_image_success_test() throws IOException {
        // given
        String imageContent = "234kh2kl4h2l34k2j34hlk23h4";
        MultipartFile multipartFile = new MockMultipartFile("image", "imageName.jpeg", "image/*", imageContent.getBytes());

        // when
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.of(product));

        // then
        productService.modifyProductImage(1L, multipartFile);

        verify(productRepository, times(1))
                .findById(anyLong());
        verify(fileManagement, times(1))
                .saveFile(any(), any(), any(), any(),
                        any(), any(), any(), anyString(), anyString());
    }

    @Test
    @DisplayName("상품 이미지 추가 테스트")
    void add_image_success_test() throws IOException {
        // given
        String imageContent = "234kh2kl4h2l34k2j34hlk23h4";
        MultipartFile multipartFile = new MockMultipartFile("image", "imageName.jpeg", "image/*", imageContent.getBytes());

        // when
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.of(product));

        // then
        productService.addProductImage(1L, multipartFile);

        verify(productRepository, times(1))
                .findById(anyLong());
        verify(fileManagement, times(1))
                .saveFile(any(), any(), any(), any(),
                        any(), any(), any(), anyString(), anyString());
    }

    @Test
    @DisplayName("상품 상세 이미지 추가 테스트")
    void add_detailImage_success_test() throws IOException {
        // given
        String imageContent = "234kh2kl4h2l34k2j34hlk23h4";
        MultipartFile multipartFile = new MockMultipartFile("image", "imageName.jpeg", "image/*", imageContent.getBytes());

        // when
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.of(product));

        // then
        productService.addProductDetailImage(1L, multipartFile);

        verify(productRepository, times(1))
                .findById(anyLong());
        verify(fileManagement, times(1))
                .saveFile(any(), any(), any(), any(),
                        any(), any(), any(), anyString(), anyString());
    }

    @Test
    @DisplayName("e-book 이미지 수정 실패 테스트1")
    void modify_ebook_fail_test_fileException_1() throws IOException {
        // given
        String imageContent = "234kh2kl4h2l34k2j34hlk23h4";
        MultipartFile multipartFile = new MockMultipartFile("image", "imageName.jpeg", "image/*", imageContent.getBytes());

        // when
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.of(product));

        doThrow(new IOException()).when(fileManagement).deleteFile(anyString());

        // then
        assertThatThrownBy(() -> productService.modifyProductEbook(1L, multipartFile))
                .isInstanceOf(FileNotFoundException.class)
                .hasMessageContaining(FileNotFoundException.MESSAGE);
    }

    @Test
    @DisplayName("e-book 이미지 수정 실패 테스트2")
    void modify_ebook_fail_test_fileException_2() throws IOException {
        // given
        String imageContent = "234kh2kl4h2l34k2j34hlk23h4";
        MultipartFile multipartFile = new MockMultipartFile("image", "imageName.jpeg", "image/*", imageContent.getBytes());

        // when
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.of(product));

        when(fileManagement.saveFile(any(), any(), any(), any(), any(), any(), any(), anyString(), anyString()))
                .thenThrow(IOException.class);

        // then
        assertThatThrownBy(() -> productService.modifyProductEbook(1L, multipartFile))
                .isInstanceOf(FileNotFoundException.class)
                .hasMessageContaining(FileNotFoundException.MESSAGE);
    }

    @Test
    @DisplayName("상품 이미지 수정 실패 테스트1")
    void modify_image_fail_test_fileException_1() throws IOException {
        // given
        String imageContent = "234kh2kl4h2l34k2j34hlk23h4";
        MultipartFile multipartFile = new MockMultipartFile("image", "imageName.jpeg", "image/*", imageContent.getBytes());

        // when
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.of(product));

        doThrow(new IOException()).when(fileManagement).deleteFile(anyString());

        // then
        assertThatThrownBy(() -> productService.modifyProductImage(1L, multipartFile))
                .isInstanceOf(FileNotFoundException.class)
                .hasMessageContaining(FileNotFoundException.MESSAGE);
    }

    @Test
    @DisplayName("상품 이미지 수정 실패 테스트 2")
    void modify_image_fail_test_fileException_2() throws IOException {
        // given
        String imageContent = "234kh2kl4h2l34k2j34hlk23h4";
        MultipartFile multipartFile = new MockMultipartFile("image", "imageName.jpeg", "image/*", imageContent.getBytes());

        // when
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.of(product));

        when(fileManagement.saveFile(any(), any(), any(), any(), any(), any(), any(), anyString(), anyString()))
                .thenThrow(IOException.class);

        // then
        assertThatThrownBy(() -> productService.modifyProductImage(1L, multipartFile))
                .isInstanceOf(FileNotFoundException.class)
                .hasMessageContaining(FileNotFoundException.MESSAGE);
    }

    @Test
    @DisplayName("상품 상세 이미지 수정 성공 테스트")
    void modify_detailImage_success_test() throws IOException {
        // given
        String imageContent = "234kh2kl4h2l34k2j34hlk23h4";
        MultipartFile multipartFile = new MockMultipartFile("image", "imageName.jpeg", "image/*", imageContent.getBytes());

        // when
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.of(product));

        // then
        productService.modifyProductDetailImage(1L, multipartFile);

        verify(productRepository, times(1))
                .findById(anyLong());
        verify(fileManagement, times(1))
                .saveFile(any(), any(), any(), any(), any(), any(), any(),
                        anyString(), anyString());
    }

    @Test
    @DisplayName("상품 상세 이미지 수정 실패 테스트-1")
    void modify_detailImage_fail_test_fileException_1() throws IOException {
        // given
        String imageContent = "234kh2kl4h2l34k2j34hlk23h4";
        MultipartFile multipartFile = new MockMultipartFile("image", "imageName.jpeg", "image/*", imageContent.getBytes());

        // when
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.of(product));

        doThrow(new IOException()).when(fileManagement).deleteFile(anyString());

        // then
        assertThatThrownBy(() -> productService.modifyProductDetailImage(1L, multipartFile))
                .isInstanceOf(FileNotFoundException.class)
                .hasMessageContaining(FileNotFoundException.MESSAGE);
    }

    @Test
    @DisplayName("상품 상세 이미지 수정 실패 테스트-2")
    void modify_detailImage_fail_test_fileException_2() throws IOException {
        // given
        String imageContent = "234kh2kl4h2l34k2j34hlk23h4";
        MultipartFile multipartFile = new MockMultipartFile("image", "imageName.jpeg", "image/*", imageContent.getBytes());

        // when
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.of(product));

        when(fileManagement.saveFile(any(), any(), any(), any(), any(), any(), any(), anyString(), anyString()))
                .thenThrow(IOException.class);

        // then
        assertThatThrownBy(() -> productService.modifyProductDetailImage(1L, multipartFile))
                .isInstanceOf(FileNotFoundException.class)
                .hasMessageContaining(FileNotFoundException.MESSAGE);
    }

    @Test
    @DisplayName("상품 이미지 추가 실패 테스트")
    void add_image_fail_test_fileException() throws IOException {
        // given
        String imageContent = "234kh2kl4h2l34k2j34hlk23h4";
        MultipartFile multipartFile = new MockMultipartFile("image", "imageName.jpeg", "image/*", imageContent.getBytes());

        // when
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.of(product));

        when(fileManagement.saveFile(any(), any(), any(), any(), any(), any(), any(), anyString(), anyString()))
                .thenThrow(IOException.class);

        // then
        assertThatThrownBy(() -> productService.addProductImage(1L, multipartFile))
                .isInstanceOf(FileNotFoundException.class)
                .hasMessageContaining(FileNotFoundException.MESSAGE);
    }

    @Test
    @DisplayName("상품 이미지 추가 실패 테스트")
    void add_detailImage_fail_test_fileException() throws IOException {
        // given
        String imageContent = "234kh2kl4h2l34k2j34hlk23h4";
        MultipartFile multipartFile = new MockMultipartFile("image", "imageName.jpeg", "image/*", imageContent.getBytes());

        // when
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.of(product));

        when(fileManagement.saveFile(any(), any(), any(), any(), any(), any(), any(), anyString(), anyString()))
                .thenThrow(IOException.class);

        // then
        assertThatThrownBy(() -> productService.addProductDetailImage(1L, multipartFile))
                .isInstanceOf(FileNotFoundException.class)
                .hasMessageContaining(FileNotFoundException.MESSAGE);
    }

}