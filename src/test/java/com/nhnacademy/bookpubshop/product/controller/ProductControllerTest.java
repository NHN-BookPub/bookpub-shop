package com.nhnacademy.bookpubshop.product.controller;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nhnacademy.bookpubshop.author.dummy.AuthorDummy;
import com.nhnacademy.bookpubshop.author.entity.Author;
import com.nhnacademy.bookpubshop.category.dummy.CategoryDummy;
import com.nhnacademy.bookpubshop.category.entity.Category;
import com.nhnacademy.bookpubshop.error.ShopAdviceController;
import com.nhnacademy.bookpubshop.file.dummy.FileDummy;
import com.nhnacademy.bookpubshop.filemanager.dto.response.GetDownloadInfo;
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
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductAuthor;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductCategory;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTag;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import com.nhnacademy.bookpubshop.product.service.ProductService;
import com.nhnacademy.bookpubshop.state.FileCategory;
import com.nhnacademy.bookpubshop.tag.dummy.TagDummy;
import com.nhnacademy.bookpubshop.tag.entity.Tag;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

/**
 * ProductController 테스트.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@WebMvcTest(ProductController.class)
@Import(ShopAdviceController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
class ProductControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    ProductService productService;
    ObjectMapper mapper;
    Product product;
    CreateProductRequestDto requestDto;
    GetProductDetailResponseDto responseDto;
    GetProductListResponseDto listResponseDto;
    ProductPolicy productPolicy;
    ProductTypeStateCode typeStateCode;
    ProductSaleStateCode saleStateCode;
    String url = "/api/products";
    String tokenUrl = "/token/products";
    Author author;
    Category category;
    Tag tag;
    Map<String, MultipartFile> files = new HashMap<>();
    String imageContent = "234kh2kl4h2l34k2j34hlk23h4";
    MockMultipartFile thumbnail;
    MockMultipartFile detail;
    MockMultipartFile ebook;
    MockMultipartFile requestDtoFile;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper().registerModule(new JavaTimeModule());

        productPolicy = new ProductPolicy(1, "method", true, 7);
        typeStateCode = new ProductTypeStateCode(1, "name", true, "info");
        saleStateCode = new ProductSaleStateCode(1, "category", true, "info");
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

        product.setProductFiles(List.of(
                FileDummy.dummy(
                        null,
                        null,
                        null,
                        product,
                        null,
                        FileCategory.PRODUCT_THUMBNAIL, null),
                FileDummy.dummy(
                        null,
                        null,
                        null,
                        product,
                        null,
                        FileCategory.PRODUCT_DETAIL, null),
                FileDummy.dummy(
                        null,
                        null,
                        null,
                        product,
                        null,
                        FileCategory.PRODUCT_EBOOK, null)));

        requestDto = new CreateProductRequestDto();
        responseDto = new GetProductDetailResponseDto(product);

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

        thumbnail = new MockMultipartFile("thumbnail",
                "thumbnail.jpeg",
                "image/jpeg",
                imageContent.getBytes());

        detail = new MockMultipartFile("detail",
                "detail.jpeg",
                "image/jpeg",
                imageContent.getBytes());

        ebook = new MockMultipartFile("ebook",
                "ebook.pdf",
                "application/pdf",
                imageContent.getBytes());
    }

    @Test
    @DisplayName("전체 상품 조회 성공")
    void productListSuccess() throws Exception {
        List<GetProductListResponseDto> responses = List.of(listResponseDto);

        Pageable pageable = PageRequest.of(0, 10);
        Page<GetProductListResponseDto> page =
                PageableExecutionUtils.getPage(responses, pageable, () -> 1L);

        when(productService.getAllProducts(pageable))
                .thenReturn(page);

        mockMvc.perform(get(url)
                        .param("page", mapper.writeValueAsString(pageable.getPageNumber()))
                        .param("size", mapper.writeValueAsString(pageable.getPageSize()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andExpect(jsonPath("$.content[0].productNo").value(listResponseDto.getProductNo()))
                .andExpect(jsonPath("$.content[0].title").value(listResponseDto.getTitle()))
                .andExpect(jsonPath("$.content[0].productStock").value(listResponseDto.getProductStock()))
                .andExpect(jsonPath("$.content[0].salesPrice").value(listResponseDto.getSalesPrice()))
                .andExpect(jsonPath("$.content[0].saleRate").value(listResponseDto.getSaleRate()))
                .andExpect(jsonPath("$.content[0].productPrice").value(listResponseDto.getProductPrice()))
                .andExpect(jsonPath("$.content[0].deleted").value(listResponseDto.isDeleted()))
                .andDo(print())
                .andDo(document("product-getList",
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("page").description("조회할 페이지 번호"),
                                parameterWithName("size").description("페이지 사이즈")
                        ),
                        responseFields(
                                fieldWithPath("totalPages").description("총 페이지 개수"),
                                fieldWithPath("number").description("현재 페이지 번호"),
                                fieldWithPath("previous").description("이전 페이지 존재 여부"),
                                fieldWithPath("next").description("다음 페이지 존재 여부"),
                                fieldWithPath("content[].productNo").description("상품 번호"),
                                fieldWithPath("content[].title").description("상품 제목"),
                                fieldWithPath("content[].productStock").description("상품 재고수량"),
                                fieldWithPath("content[].salesPrice").description("상품 판매가"),
                                fieldWithPath("content[].saleRate").description("상품의 할인율"),
                                fieldWithPath("content[].productSubscribed").description("구독 가능 여부"),
                                fieldWithPath("content[].productPrice").description("상품 정가"),
                                fieldWithPath("content[].deleted").description("상품 삭제여부"),
                                fieldWithPath("content[].thumbnail").description("상품 썸네일")
                        )));

        verify(productService, times(1)).getAllProducts(pageable);

    }

    @Test
    @DisplayName("상품 생성 성공")
    void productAddSuccessTest() throws Exception {
        requestDtoFile = new MockMultipartFile("requestDto",
                "",
                "application/json",
                mapper.writeValueAsString(requestDto)
                        .getBytes(StandardCharsets.UTF_8));

        doNothing().when(productService).createProduct(requestDto, files);
        mockMvc.perform(multipart(tokenUrl)
                        .file(thumbnail)
                        .file(detail)
                        .file(ebook)
                        .file(requestDtoFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("product-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("thumbnail").description("상품 썸네일"),
                                partWithName("detail").description("상품 상세이미지"),
                                partWithName("ebook").description("상품 이북 파일"),
                                partWithName("requestDto").description("상품 생성시 사용되는 Dto")
                        )));

        then(productService).should().createProduct(any(CreateProductRequestDto.class), any());
    }

    @Test
    @DisplayName("상품 생성 실패_ISBN이 없는 경우")
    void productAddFailTest_IsbnIsNull() throws Exception {
        ReflectionTestUtils.setField(requestDto, "productIsbn", null);

        requestDtoFile = new MockMultipartFile("requestDto",
                "",
                "application/json",
                mapper.writeValueAsString(requestDto)
                        .getBytes(StandardCharsets.UTF_8));

        doNothing().when(productService).createProduct(requestDto, files);

        mockMvc.perform(multipart(tokenUrl)
                        .file(thumbnail)
                        .file(detail)
                        .file(ebook)
                        .file(requestDtoFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andDo(document("product-create-fail-isbnNull",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("thumbnail").description("상품 썸네일"),
                                partWithName("detail").description("상품 상세이미지"),
                                partWithName("ebook").description("상품 이북 파일"),
                                partWithName("requestDto").description("상품 생성시 사용되는 Dto")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("ISBN을 입력해주세요.")
                        )));

        verify(productService, times(0)).createProduct(any(CreateProductRequestDto.class), any());
    }

    @Test
    @DisplayName("상품 생성 실패_ISBN 길이가 초과된 경우")
    void productAddFailTest_IsbnIsTooLong() throws Exception {
        ReflectionTestUtils.setField(requestDto, "productIsbn", "asdfasdfasdfasdfsadfsa");

        requestDtoFile = new MockMultipartFile("requestDto",
                "",
                "application/json",
                mapper.writeValueAsString(requestDto)
                        .getBytes(StandardCharsets.UTF_8));

        doNothing().when(productService).createProduct(requestDto, files);

        mockMvc.perform(multipart(tokenUrl)
                        .file(thumbnail)
                        .file(detail)
                        .file(ebook)
                        .file(requestDtoFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andDo(document("product-create-fail-isbnOver",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("thumbnail").description("상품 썸네일"),
                                partWithName("detail").description("상품 상세이미지"),
                                partWithName("ebook").description("상품 이북 파일"),
                                partWithName("requestDto").description("상품 생성시 사용되는 Dto")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("ISBN은 10자 혹은 13자입니다.")
                        )));

        verify(productService, times(0)).createProduct(any(CreateProductRequestDto.class), any());
    }

    @Test
    @DisplayName("상품 생성 실패_제목 길이가 초과된 경우")
    void productAddFailTest_TitleIsTooLong() throws Exception {
        ReflectionTestUtils.setField(requestDto, "title",
                "asdfasdfasdfasdfsadfsaasdfasdfdasdfasfafasfdasdfasfasfdaasdfasdfasdfasdfasdfsfsadfasfasfsafsafasfsafsafsafdsdf");

        requestDtoFile = new MockMultipartFile("requestDto",
                "",
                "application/json",
                mapper.writeValueAsString(requestDto)
                        .getBytes(StandardCharsets.UTF_8));

        doNothing().when(productService).createProduct(requestDto, files);

        mockMvc.perform(multipart(tokenUrl)
                        .file(thumbnail)
                        .file(detail)
                        .file(ebook)
                        .file(requestDtoFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andDo(document("product-create-fail-titleOver",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("thumbnail").description("상품 썸네일"),
                                partWithName("detail").description("상품 상세이미지"),
                                partWithName("ebook").description("상품 이북 파일"),
                                partWithName("requestDto").description("상품 생성시 사용되는 Dto")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("제목은 최대 100자입니다.")
                        )));

        verify(productService, times(0)).createProduct(any(CreateProductRequestDto.class), any());
    }

    @Test
    @DisplayName("상품 생성 실패_출판사가 null인 경우")
    void productAddFailTest_productPublisherIsNull() throws Exception {
        ReflectionTestUtils.setField(requestDto, "productPublisher", null);

        requestDtoFile = new MockMultipartFile("requestDto",
                "",
                "application/json",
                mapper.writeValueAsString(requestDto)
                        .getBytes(StandardCharsets.UTF_8));

        doNothing().when(productService).createProduct(requestDto, files);

        mockMvc.perform(multipart(tokenUrl)
                        .file(thumbnail)
                        .file(detail)
                        .file(ebook)
                        .file(requestDtoFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andDo(document("product-create-fail-publisherNull",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("thumbnail").description("상품 썸네일"),
                                partWithName("detail").description("상품 상세이미지"),
                                partWithName("ebook").description("상품 이북 파일"),
                                partWithName("requestDto").description("상품 생성시 사용되는 Dto")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("출판사를 입력해주세요.")
                        )));

        verify(productService, times(0)).createProduct(any(CreateProductRequestDto.class), any());
    }

    @Test
    @DisplayName("상품 생성 실패_출판사 이름 길이를 초과한 경우")
    void productAddFailTest_productPublisherTooLong() throws Exception {
        ReflectionTestUtils.setField(requestDto, "productPublisher",
                "asdfasdfasdfadfasdfasdfasdfadfasfsafasdfsadfasdfsadfasdfsadfasdffd");

        requestDtoFile = new MockMultipartFile("requestDto",
                "",
                "application/json",
                mapper.writeValueAsString(requestDto)
                        .getBytes(StandardCharsets.UTF_8));

        doNothing().when(productService).createProduct(requestDto, files);

        mockMvc.perform(multipart(tokenUrl)
                        .file(thumbnail)
                        .file(detail)
                        .file(ebook)
                        .file(requestDtoFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andDo(document("product-create-fail-publisherOver",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("thumbnail").description("상품 썸네일"),
                                partWithName("detail").description("상품 상세이미지"),
                                partWithName("ebook").description("상품 이북 파일"),
                                partWithName("requestDto").description("상품 생성시 사용되는 Dto")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("50자를 넘을 수 없습니다.")
                        )));

        verify(productService, times(0)).createProduct(any(CreateProductRequestDto.class), any());
    }

    @Test
    @DisplayName("상품 생성 실패_상품 설명 길이를 초과한 경우")
    void productAddFailTest_productDescriptionTooLong() throws Exception {
        String result = "a".repeat(5050);

        ReflectionTestUtils.setField(requestDto, "productDescription", result);

        requestDtoFile = new MockMultipartFile("requestDto",
                "",
                "application/json",
                mapper.writeValueAsString(requestDto)
                        .getBytes(StandardCharsets.UTF_8));

        doNothing().when(productService).createProduct(requestDto, files);

        mockMvc.perform(multipart(tokenUrl)
                        .file(thumbnail)
                        .file(detail)
                        .file(ebook)
                        .file(requestDtoFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andDo(document("product-create-fail-descriptionOver",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("thumbnail").description("상품 썸네일"),
                                partWithName("detail").description("상품 상세이미지"),
                                partWithName("ebook").description("상품 이북 파일"),
                                partWithName("requestDto").description("상품 생성시 사용되는 Dto")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("설명은 최대 2000자입니다.")
                        )));

        verify(productService, times(0)).createProduct(any(CreateProductRequestDto.class), any());
    }

    @Test
    @DisplayName("상품 생성 실패_판매가가 null인 경우")
    void productAddFailTest_salePriceIsNull() throws Exception {
        ReflectionTestUtils.setField(requestDto, "salePrice", null);

        requestDtoFile = new MockMultipartFile("requestDto",
                "",
                "application/json",
                mapper.writeValueAsString(requestDto)
                        .getBytes(StandardCharsets.UTF_8));

        doNothing().when(productService).createProduct(requestDto, files);

        mockMvc.perform(multipart(tokenUrl)
                        .file(thumbnail)
                        .file(detail)
                        .file(ebook)
                        .file(requestDtoFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andDo(document("product-create-fail-salePriceNull",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("thumbnail").description("상품 썸네일"),
                                partWithName("detail").description("상품 상세이미지"),
                                partWithName("ebook").description("상품 이북 파일"),
                                partWithName("requestDto").description("상품 생성시 사용되는 Dto")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("판매가를 입력해주세요.")
                        )));

        verify(productService, times(0)).createProduct(any(CreateProductRequestDto.class), any());
    }

    @Test
    @DisplayName("상품 생성 실패_정가가 null인 경우")
    void productAddFailTest_productPriceIsNull() throws Exception {
        ReflectionTestUtils.setField(requestDto, "productPrice", null);

        requestDtoFile = new MockMultipartFile("requestDto",
                "",
                "application/json",
                mapper.writeValueAsString(requestDto)
                        .getBytes(StandardCharsets.UTF_8));

        doNothing().when(productService).createProduct(requestDto, files);

        mockMvc.perform(multipart(tokenUrl)
                        .file(thumbnail)
                        .file(detail)
                        .file(ebook)
                        .file(requestDtoFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andDo(document("product-create-fail-priceNull",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("thumbnail").description("상품 썸네일"),
                                partWithName("detail").description("상품 상세이미지"),
                                partWithName("ebook").description("상품 이북 파일"),
                                partWithName("requestDto").description("상품 생성시 사용되는 Dto")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("정가를 입력해주세요.")
                        )));

        verify(productService, times(0)).createProduct(any(CreateProductRequestDto.class), any());
    }

    @Test
    @DisplayName("상품 생성 실패_상품 재고가 null인 경우")
    void productAddFailTest_productStockIsNull() throws Exception {
        ReflectionTestUtils.setField(requestDto, "productStock", null);

        requestDtoFile = new MockMultipartFile("requestDto",
                "",
                "application/json",
                mapper.writeValueAsString(requestDto)
                        .getBytes(StandardCharsets.UTF_8));

        doNothing().when(productService).createProduct(requestDto, files);

        mockMvc.perform(multipart(tokenUrl)
                        .file(thumbnail)
                        .file(detail)
                        .file(ebook)
                        .file(requestDtoFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andDo(document("product-create-stockNull",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("thumbnail").description("상품 썸네일"),
                                partWithName("detail").description("상품 상세이미지"),
                                partWithName("ebook").description("상품 이북 파일"),
                                partWithName("requestDto").description("상품 생성시 사용되는 Dto")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("상품 재고를 입력해주세요.")
                        )));

        verify(productService, times(0)).createProduct(any(CreateProductRequestDto.class), any());
    }

    @Test
    @DisplayName("상품 생성 실패_출판 일시가 null인 경우")
    void productAddFailTest_publishedAtIsNull() throws Exception {
        ReflectionTestUtils.setField(requestDto, "publishedAt", null);

        requestDtoFile = new MockMultipartFile("requestDto",
                "",
                "application/json",
                mapper.writeValueAsString(requestDto)
                        .getBytes(StandardCharsets.UTF_8));

        doNothing().when(productService).createProduct(requestDto, files);

        mockMvc.perform(multipart(tokenUrl)
                        .file(thumbnail)
                        .file(detail)
                        .file(ebook)
                        .file(requestDtoFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andDo(document("product-create-publishedAtNull",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("thumbnail").description("상품 썸네일"),
                                partWithName("detail").description("상품 상세이미지"),
                                partWithName("ebook").description("상품 이북 파일"),
                                partWithName("requestDto").description("상품 생성시 사용되는 Dto")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("출판일시를 입력해주세요.")
                        )));

        verify(productService, times(0)).createProduct(any(CreateProductRequestDto.class), any());
    }

    @Test
    @DisplayName("상품 생성 실패_상품 정책 번호가 null인 경우")
    void productAddFailTest_productPolicyNoIsNull() throws Exception {
        ReflectionTestUtils.setField(requestDto, "productPolicyNo", null);

        requestDtoFile = new MockMultipartFile("requestDto",
                "",
                "application/json",
                mapper.writeValueAsString(requestDto)
                        .getBytes(StandardCharsets.UTF_8));

        doNothing().when(productService).createProduct(requestDto, files);

        mockMvc.perform(multipart(tokenUrl)
                        .file(thumbnail)
                        .file(detail)
                        .file(ebook)
                        .file(requestDtoFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andDo(document("product-create-fail-policyNull",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("thumbnail").description("상품 썸네일"),
                                partWithName("detail").description("상품 상세이미지"),
                                partWithName("ebook").description("상품 이북 파일"),
                                partWithName("requestDto").description("상품 생성시 사용되는 Dto")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("상품정책 번호를 입력해주세요.")
                        )));

        verify(productService, times(0)).createProduct(any(CreateProductRequestDto.class), any());
    }

    @Test
    @DisplayName("상품 생성 실패_상품판매여부코드 번가 null인 경우")
    void productAddFailTest_saleCodeNoIsNull() throws Exception {
        ReflectionTestUtils.setField(requestDto, "saleCodeNo", null);

        requestDtoFile = new MockMultipartFile("requestDto",
                "",
                "application/json",
                mapper.writeValueAsString(requestDto)
                        .getBytes(StandardCharsets.UTF_8));

        doNothing().when(productService).createProduct(requestDto, files);

        mockMvc.perform(multipart(tokenUrl)
                        .file(thumbnail)
                        .file(detail)
                        .file(ebook)
                        .file(requestDtoFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andDo(document("product-create-fail-saleCodeNull",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("thumbnail").description("상품 썸네일"),
                                partWithName("detail").description("상품 상세이미지"),
                                partWithName("ebook").description("상품 이북 파일"),
                                partWithName("requestDto").description("상품 생성시 사용되는 Dto")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("상품판매여부코드 번호를 입력해주세요.")
                        )));

        verify(productService, times(0)).createProduct(any(CreateProductRequestDto.class), any());
    }

    @Test
    @DisplayName("상품 생성 실패_상품판매여부코드 번호가 null인 경우")
    void productAddFailTest_typeCodeNoIsNull() throws Exception {
        ReflectionTestUtils.setField(requestDto, "typeCodeNo", null);

        requestDtoFile = new MockMultipartFile("requestDto",
                "",
                "application/json",
                mapper.writeValueAsString(requestDto)
                        .getBytes(StandardCharsets.UTF_8));

        doNothing().when(productService).createProduct(requestDto, files);

        mockMvc.perform(multipart(tokenUrl)
                        .file(thumbnail)
                        .file(detail)
                        .file(ebook)
                        .file(requestDtoFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andDo(document("product-create-fail-typeCodeNull",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("thumbnail").description("상품 썸네일"),
                                partWithName("detail").description("상품 상세이미지"),
                                partWithName("ebook").description("상품 이북 파일"),
                                partWithName("requestDto").description("상품 생성시 사용되는 Dto")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("상품유형코드 번호를 입력해주세요.")
                        )));

        verify(productService, times(0)).createProduct(any(CreateProductRequestDto.class), any());
    }

    @Test
    @DisplayName("상품 상세 조회 성공")
    void getProductDetailById() throws Exception {
        when(productService.getProductDetailById(anyLong()))
                .thenReturn(responseDto);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/products/{productNo}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productNo").value(responseDto.getProductNo()))
                .andExpect(jsonPath("$.productIsbn").value(responseDto.getProductIsbn()))
                .andExpect(jsonPath("$.title").value(responseDto.getTitle()))
                .andExpect(jsonPath("$.productPublisher").value(requestDto.getProductPublisher()))
                .andExpect(jsonPath("$.pageCount").value(responseDto.getPageCount()))
                .andExpect(jsonPath("$.productDescription").value(responseDto.getProductDescription()))
                .andExpect(jsonPath("$.salesPrice").value(responseDto.getSalesPrice()))
                .andExpect(jsonPath("$.productPrice").value(requestDto.getProductPrice()))
                .andExpect(jsonPath("$.salesRate").value(responseDto.getSalesRate()))
                .andExpect(jsonPath("$.productPriority").value(responseDto.getProductPriority()))
                .andExpect(jsonPath("$.productStock").value(responseDto.getProductStock()))
                .andExpect(jsonPath("$.deleted").value(responseDto.isDeleted()))
                .andExpect(jsonPath("$.productSubscribed").value(responseDto.isProductSubscribed()))
                .andExpect(jsonPath("$.saleStateCodeCategory").value(responseDto.getSaleStateCodeCategory()))
                .andExpect(jsonPath("$.typeStateCodeName").value(responseDto.getTypeStateCodeName()))
                .andExpect(jsonPath("$.policyMethod").value(responseDto.getPolicyMethod()))
                .andExpect(jsonPath("$.policySaved").value(responseDto.isPolicySaved()))
                .andExpect(jsonPath("$.policySaveRate").value(responseDto.getPolicySaveRate()))
                .andExpect(jsonPath("$.authors").value(responseDto.getAuthors()))
                .andExpect(jsonPath("$.categories").value(responseDto.getCategories()))
                .andExpect(jsonPath("$.tags").value(responseDto.getTags()))
                .andExpect(jsonPath("$.tagsColors").value(responseDto.getTagsColors()))
                .andDo(print())
                .andDo(document("product-get",
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("productNo").description("조회할 상품 번호")
                        ),
                        responseFields(
                                fieldWithPath("productNo").description("상품 번호"),
                                fieldWithPath("productIsbn").description("상품의 ISBN"),
                                fieldWithPath("title").description("상품 제목"),
                                fieldWithPath("productPublisher").description("상품 출판사"),
                                fieldWithPath("pageCount").description("상품의 총 페이지 수"),
                                fieldWithPath("productDescription").description("상품 설명"),
                                fieldWithPath("salesPrice").description("상품 판매가"),
                                fieldWithPath("productPrice").description("상품 정가"),
                                fieldWithPath("salesRate").description("상품의 할인율"),
                                fieldWithPath("productPriority").description("상품 노출 우선순위(숫자가 작을수록 우선순위 높음)"),
                                fieldWithPath("productStock").description("상품의 재고수량"),
                                fieldWithPath("publishDate").description("상품 출간 일자"),
                                fieldWithPath("deleted").description("상품 삭제 여부"),
                                fieldWithPath("productSubscribed").description("상품 구독 가능 여부"),
                                fieldWithPath("saleStateCodeCategory").description("상품 판매여부 종류(판매중, 품절 등)"),
                                fieldWithPath("typeStateCodeName").description("상품 유형 코드 종류(신간, 베스트셀러 등))"),
                                fieldWithPath("policyMethod").description("상품 포인트 적립 방식 기준(실구매가, 판매가 등)"),
                                fieldWithPath("policySaved").description("상품의 포인트 적립 여부"),
                                fieldWithPath("policySaveRate").description("상품 포인트 적립률"),
                                fieldWithPath("authors[]").description("상품(도서) 저자 리스트"),
                                fieldWithPath("categories[]").description("상품 카테고리 리스트"),
                                fieldWithPath("tags[]").description("상품 태그 리스트"),
                                fieldWithPath("tagsColors[]").description("태그 색 리스트"),
                                fieldWithPath("categoriesNo[]").description("상품 카테고리 번호 리스트"),
                                fieldWithPath("thumbnail").description("상품 썸네일"),
                                fieldWithPath("detail").description("상품 상세이미지"),
                                fieldWithPath("ebook").description("상품 이북"),
                                fieldWithPath("info").description("연관상품 정보")
                        )));

    }

    @Test
    @DisplayName("상품 제목 검색 성공")
    void getProductLikeTitle() throws Exception {
        List<GetProductListResponseDto> responses = new ArrayList<>();
        responses.add(listResponseDto);

        PageRequest pageable = PageRequest.of(0, 10);
        Page<GetProductListResponseDto> page =
                PageableExecutionUtils.getPage(responses, pageable, () -> 1L);

        when(productService.getProductListLikeTitle("test", pageable))
                .thenReturn(page);

        mockMvc.perform(get(url + "/search")
                        .param("title", "test")
                        .param("page", mapper.writeValueAsString(pageable.getPageNumber()))
                        .param("size", mapper.writeValueAsString(pageable.getPageSize()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].productNo").value(responses.get(0).getProductNo()))
                .andExpect(jsonPath("$.content[0].title").value(responses.get(0).getTitle()))
                .andExpect(jsonPath("$.content[0].productStock").value(responses.get(0).getProductStock()))
                .andExpect(jsonPath("$.content[0].salesPrice").value(responses.get(0).getSalesPrice()))
                .andExpect(jsonPath("$.content[0].productPrice").value(responses.get(0).getProductPrice()))
                .andExpect(jsonPath("$.content[0].deleted").value(responses.get(0).isDeleted()))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("get-titleProducts",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("title").description("상품 조회 시 조건이 될 제목"),
                                parameterWithName("page").description("조회할 페이지 번호"),
                                parameterWithName("size").description("한 페이지 당 데이터 개수")
                        ),
                        responseFields(
                                fieldWithPath("totalPages").description("총 페이지 개수"),
                                fieldWithPath("number").description("현재 페이지 번호"),
                                fieldWithPath("previous").description("이전 페이지 존재 여부"),
                                fieldWithPath("next").description("다음 페이지 존재 여부"),
                                fieldWithPath("content[].productNo").description("상품 번호"),
                                fieldWithPath("content[].title").description("상품 제목"),
                                fieldWithPath("content[].productStock").description("상품 재고수량"),
                                fieldWithPath("content[].salesPrice").description("상품 판매가"),
                                fieldWithPath("content[].saleRate").description("상품 할인율"),
                                fieldWithPath("content[].productPrice").description("상품 정가"),
                                fieldWithPath("content[].productSubscribed").description("구독 가능 여부"),
                                fieldWithPath("content[].deleted").description("상품의 삭제 여부"),
                                fieldWithPath("content[].thumbnail").description("상품 썸네일")
                        )));

        verify(productService, times(1)).getProductListLikeTitle("test", pageable);
    }

    @Test
    @DisplayName("상품 수정 성공")
    void modifyProduct() throws Exception {
        doNothing()
                .when(productService)
                .modifyProduct(requestDto, 1L);

        mockMvc.perform(RestDocumentationRequestBuilders.put(tokenUrl + "/{productNo}", 1L)
                        .content(mapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("product-modify",
                        preprocessRequest(prettyPrint()),
                        pathParameters(
                                parameterWithName("productNo").description("수정할 상품 번호")
                        ),
                        requestFields(
                                fieldWithPath("productIsbn").description("상품의 ISBN"),
                                fieldWithPath("title").description("상품 제목"),
                                fieldWithPath("productPublisher").description("상품 출판사"),
                                fieldWithPath("pageCount").description("상품의 총 페이지 수"),
                                fieldWithPath("productDescription").description("상품 설명"),
                                fieldWithPath("salePrice").description("상품 판매가"),
                                fieldWithPath("productPrice").description("상품 정가"),
                                fieldWithPath("salesRate").description("상품의 할인율"),
                                fieldWithPath("productPriority").description("상품 노출 우선순위(숫자가 작을수록 우선순위 높음)"),
                                fieldWithPath("productStock").description("상품의 재고수량"),
                                fieldWithPath("publishedAt").description("상품 출간 일자"),
                                fieldWithPath("subscribed").description("상품 구독 가능 여부"),
                                fieldWithPath("productPolicyNo").description("상품 포인트 정책 번호"),
                                fieldWithPath("saleCodeNo").description("상품판매여부 코드 번호)"),
                                fieldWithPath("typeCodeNo").description("상품 유형 코드 번호"),
                                fieldWithPath("authorsNo[]").description("상품(도서) 저자 리스트"),
                                fieldWithPath("categoriesNo[]").description("상품 카테고리 리스트"),
                                fieldWithPath("tagsNo[]").description("상품 태그 리스트")
                        )));

        verify(productService, times(1))
                .modifyProduct(any(), anyLong());
    }

    @Test
    @DisplayName("상품 삭제 여부 설정 성공")
    void setDeletedProduct() throws Exception {
        doNothing().when(productService)
                .setDeleteProduct(product.getProductNo());

        mockMvc.perform(RestDocumentationRequestBuilders.delete(tokenUrl + "/{productNo}", 1L)
                        .param("deleted", "false")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("product-deletedModify",
                        preprocessRequest(prettyPrint()),
                        pathParameters(
                                parameterWithName("productNo").description("삭제할 상품 번호(soft delete)")
                        ),
                        requestParameters(
                                parameterWithName("deleted").description("삭제 상태값")
                        )));

        verify(productService, times(1))
                .setDeleteProduct(anyLong());
    }

    @Test
    @DisplayName("상품 유형 번호를 가지고 상품 리스트 조회")
    void getProductsByType() throws Exception {
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
        when(productService.getProductsByType(anyInt(), anyInt()))
                .thenReturn(list);

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.get(url + "/types/{typeNo}", 5)
                        .param("limit", "5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$[0].salesPrice").value(9000))
                .andExpect(jsonPath("$[0].productPrice").value(10000))
                .andExpect(jsonPath("$[0].salesRate").value(10))
                .andDo(print())
                .andDo(document("get-typeProducts",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("typeNo").description("조회할 상품 유형 코드번호")
                        ),
                        requestParameters(
                                parameterWithName("limit").description("조회할 상품 갯수")
                        ),
                        responseFields(
                                fieldWithPath("[].productNo").description("상품 번호"),
                                fieldWithPath("[].title").description("상품 제목"),
                                fieldWithPath("[].salesPrice").description("상품 판매가"),
                                fieldWithPath("[].productPrice").description("상품 정가"),
                                fieldWithPath("[].salesRate").description("상품의 할인율"),
                                fieldWithPath("[].productCategories[]").description("상품 카테고리 리스트"),
                                fieldWithPath("[].thumbnail").description("상품 썸네일")
                        )));
    }

    @Test
    @DisplayName("카트에 담긴 상품들 번호를 가지고 상품 조회 테스트")
    void getProductsInCart() throws Exception {
        // given
        GetProductDetailResponseDto dto = new GetProductDetailResponseDto();
        ReflectionTestUtils.setField(dto, "productNo", 1L);
        ReflectionTestUtils.setField(dto, "title", "설명");
        ReflectionTestUtils.setField(dto, "productPublisher", "출판");
        ReflectionTestUtils.setField(dto, "salesPrice", 1000L);
        ReflectionTestUtils.setField(dto, "productPrice", 2000L);

        List<GetProductDetailResponseDto> list = List.of(dto);

        // when
        when(productService.getProductsInCart(List.of(dto.getProductNo())))
                .thenReturn(list);

        // then
        mockMvc.perform(get(url + "/cart")
                        .param("productNo", String.valueOf(dto.getProductNo()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$[0].productNo").value(dto.getProductNo()))
                .andExpect(jsonPath("$[0].title").value(dto.getTitle()))
                .andExpect(jsonPath("$[0].productPublisher").value(dto.getProductPublisher()))
                .andExpect(jsonPath("$[0].salesPrice").value(dto.getSalesPrice()))
                .andExpect(jsonPath("$[0].productPrice").value(dto.getProductPrice()))
                .andDo(print())
                .andDo(document("get-cartProducts",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("productNo").description("조회할 상품 번호")
                        ),
                        responseFields(
                                fieldWithPath("[].productNo").description("상품 번호"),
                                fieldWithPath("[].productIsbn").description("상품의 ISBN"),
                                fieldWithPath("[].title").description("상품 제목"),
                                fieldWithPath("[].productPublisher").description("상품 출판사"),
                                fieldWithPath("[].pageCount").description("상품의 총 페이지 수"),
                                fieldWithPath("[].productDescription").description("상품 설명"),
                                fieldWithPath("[].salesPrice").description("상품 판매가"),
                                fieldWithPath("[].productPrice").description("상품 정가"),
                                fieldWithPath("[].salesRate").description("상품의 할인율"),
                                fieldWithPath("[].productPriority").description("상품 노출 우선순위(숫자가 작을수록 우선순위 높음)"),
                                fieldWithPath("[].productStock").description("상품의 재고수량"),
                                fieldWithPath("[].publishDate").description("상품 출간 일자"),
                                fieldWithPath("[].deleted").description("상품 삭제 여부"),
                                fieldWithPath("[].productSubscribed").description("상품 구독 가능 여부"),
                                fieldWithPath("[].saleStateCodeCategory").description("상품 판매여부 종류(판매중, 품절 등)"),
                                fieldWithPath("[].typeStateCodeName").description("상품 유형 코드 종류(신간, 베스트셀러 등))"),
                                fieldWithPath("[].policyMethod").description("상품 포인트 적립 방식 기준(실구매가, 판매가 등)"),
                                fieldWithPath("[].policySaved").description("상품의 포인트 적립 여부"),
                                fieldWithPath("[].policySaveRate").description("상품 포인트 적립률"),
                                fieldWithPath("[].authors[]").description("상품(도서) 저자 리스트"),
                                fieldWithPath("[].categories[]").description("상품 카테고리 이름 리스트"),
                                fieldWithPath("[].tags[]").description("상품 태그 리스트"),
                                fieldWithPath("[].tagsColors[]").description("태그 색 리스트"),
                                fieldWithPath("[].categoriesNo[]").description("상품 카테고리 번호 리스트"),
                                fieldWithPath("[].thumbnail").description("상품 썸네일"),
                                fieldWithPath("[].detail").description("상품 상세이미지"),
                                fieldWithPath("[].ebook").description("상품 이북"),
                                fieldWithPath("[].info").description("상품 이북")
                        )));
    }

    @Test
    @DisplayName("카테고리별 상품 조회 API 테스트")
    void getProductsByCategory() throws Exception {
        // given
        GetProductByCategoryResponseDto dto = new GetProductByCategoryResponseDto();
        ReflectionTestUtils.setField(dto, "productNo", 1L);
        ReflectionTestUtils.setField(dto, "title", "제목");
        ReflectionTestUtils.setField(dto, "salesPrice", 1000L);
        ReflectionTestUtils.setField(dto, "salesRate", 10);
        ReflectionTestUtils.setField(dto, "categories", List.of("요리도서"));
        ReflectionTestUtils.setField(dto, "authors", List.of("저자 1"));

        List<GetProductByCategoryResponseDto> response = List.of(dto);

        Pageable pageable = PageRequest.of(0, 10);
        Page<GetProductByCategoryResponseDto> page =
                PageableExecutionUtils.getPage(response, pageable, () -> 1L);

        // when
        when(productService.getProductsByCategory(4, pageable))
                .thenReturn(page);

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.get(url +"/product/categories/{categoryNo}", 4)
                        .param("page", mapper.writeValueAsString(pageable.getPageNumber()))
                        .param("size", mapper.writeValueAsString(pageable.getPageSize()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andExpect(jsonPath("$.content[0].productNo").value(dto.getProductNo()))
                .andExpect(jsonPath("$.content[0].title").value(dto.getTitle()))
                .andExpect(jsonPath("$.content[0].salesPrice").value(dto.getSalesPrice()))
                .andExpect(jsonPath("$.content[0].salesRate").value(dto.getSalesRate()))
                .andExpect(jsonPath("$.content[0].productNo").value(dto.getProductNo()))
                .andDo(print())
                .andDo(document("product-get-category",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),

                        requestParameters(
                                parameterWithName("page").description("조회할 페이지 번호"),
                                parameterWithName("size").description("페이지 사이즈")
                        ),

                        pathParameters(
                                parameterWithName("categoryNo").description("카테고리 번호")
                        ),

                        responseFields(
                                fieldWithPath("totalPages").description("총 페이지 개수"),
                                fieldWithPath("number").description("현재 페이지 번호"),
                                fieldWithPath("previous").description("이전 페이지 존재 여부"),
                                fieldWithPath("next").description("다음 페이지 존재 여부"),
                                fieldWithPath("content[].productNo").description("상품 번호"),
                                fieldWithPath("content[].title").description("상품 제목"),
                                fieldWithPath("content[].salesPrice").description("판매가격"),
                                fieldWithPath("content[].salesRate").description("할인율"),
                                fieldWithPath("content[].title").description("상품 제목"),
                                fieldWithPath("content[].categories").description("카테고리"),
                                fieldWithPath("content[].authors").description("저자"),
                                fieldWithPath("content[].thumbnail").description("상품 썸네일"),
                                fieldWithPath("content[].ebook").description("상품 이북")
                        )
                ));


        verify(productService, times(1)).getProductsByCategory(4, pageable);
    }

    @Test
    @DisplayName("Ebook 조회 테스트")
    void getEbook() throws Exception {
        // given
        GetProductByCategoryResponseDto dto = new GetProductByCategoryResponseDto();
        ReflectionTestUtils.setField(dto, "productNo", 1L);
        ReflectionTestUtils.setField(dto, "title", "제목");
        ReflectionTestUtils.setField(dto, "thumbnail", "thumbnail");
        ReflectionTestUtils.setField(dto, "salesPrice", 1000L);
        ReflectionTestUtils.setField(dto, "salesRate", 10);
        ReflectionTestUtils.setField(dto, "categories", List.of("요리도서"));
        ReflectionTestUtils.setField(dto, "authors", List.of("저자 1"));

        List<GetProductByCategoryResponseDto> response = List.of(dto);

        Pageable pageable = PageRequest.of(0, 10);
        Page<GetProductByCategoryResponseDto> page =
                PageableExecutionUtils.getPage(response, pageable, () -> 1L);

        // when
        when(productService.getEbooks(pageable))
                .thenReturn(page);

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.get(url +"/ebooks")
                        .param("page", mapper.writeValueAsString(pageable.getPageNumber()))
                        .param("size", mapper.writeValueAsString(pageable.getPageSize()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andExpect(jsonPath("$.content[0].productNo").value(dto.getProductNo()))
                .andExpect(jsonPath("$.content[0].title").value(dto.getTitle()))
                .andExpect(jsonPath("$.content[0].thumbnail").value(dto.getThumbnail()))
                .andExpect(jsonPath("$.content[0].salesPrice").value(dto.getSalesPrice()))
                .andExpect(jsonPath("$.content[0].salesRate").value(dto.getSalesRate()))
                .andExpect(jsonPath("$.content[0].productNo").value(dto.getProductNo()))
                .andDo(print())
                .andDo(document("product-get-ebooks",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),

                        requestParameters(
                                parameterWithName("page").description("조회할 페이지 번호"),
                                parameterWithName("size").description("페이지 사이즈")
                        ),

                        responseFields(
                                fieldWithPath("totalPages").description("총 페이지 개수"),
                                fieldWithPath("number").description("현재 페이지 번호"),
                                fieldWithPath("previous").description("이전 페이지 존재 여부"),
                                fieldWithPath("next").description("다음 페이지 존재 여부"),
                                fieldWithPath("content[].productNo").description("상품 번호"),
                                fieldWithPath("content[].title").description("상품 제목"),
                                fieldWithPath("content[].thumbnail").description("상품 썸네일"),
                                fieldWithPath("content[].salesPrice").description("판매가격"),
                                fieldWithPath("content[].salesRate").description("할인율"),
                                fieldWithPath("content[].title").description("상품 제목"),
                                fieldWithPath("content[].categories").description("카테고리"),
                                fieldWithPath("content[].authors").description("저자"),
                                fieldWithPath("content[].thumbnail").description("상품 썸네일"),
                                fieldWithPath("content[].ebook").description("상품 이북")
                        )
                ));


        verify(productService, times(1)).getEbooks(pageable);
    }

    @Test
    @DisplayName("상품 정보 수정 api 테스트")
    void modifyProductInfo_success_test() throws Exception {
        // given
        ModifyProductInfoRequestDto dto = new ModifyProductInfoRequestDto();
        ReflectionTestUtils.setField(dto, "productIsbn", "12345678910");
        ReflectionTestUtils.setField(dto, "productPrice", 1000L);
        ReflectionTestUtils.setField(dto, "salesRate", 10);
        ReflectionTestUtils.setField(dto, "productPublisher", "출판사");
        ReflectionTestUtils.setField(dto, "publishedAt", LocalDateTime.now());
        ReflectionTestUtils.setField(dto, "pageCount", 1000);
        ReflectionTestUtils.setField(dto, "salesPrice", 900L);
        ReflectionTestUtils.setField(dto, "priority", 5);

        // when
        doNothing().when(productService).modifyProductInfo(1L, dto);

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.put("/token/products/{productNo}/info", 1)
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("product-modify-info-success",
                        preprocessRequest(prettyPrint()),
                        pathParameters(
                                parameterWithName("productNo").description("상품번호")
                        ),
                        requestFields(
                                fieldWithPath("productIsbn").description("상품 Isbn"),
                                fieldWithPath("productPrice").description("정가"),
                                fieldWithPath("salesRate").description("할인율"),
                                fieldWithPath("productPublisher").description("출판사"),
                                fieldWithPath("publishedAt").description("출판일"),
                                fieldWithPath("pageCount").description("페이지 수"),
                                fieldWithPath("salesPrice").description("할인가격"),
                                fieldWithPath("priority").description("우선순위")
                        )));

        verify(productService, times(1))
                .modifyProductInfo(anyLong(), any(ModifyProductInfoRequestDto.class));
    }

    @Test
    @DisplayName("상품 정보 수정 실패 테스트 (isbn 길이 초과)")
    void modifyProductInfo_fail_over_isbn_Test() throws Exception {
        // given
        ModifyProductInfoRequestDto dto = new ModifyProductInfoRequestDto();
        ReflectionTestUtils.setField(dto, "productIsbn", "10글자 미만 13글자 이상의 isbn");
        ReflectionTestUtils.setField(dto, "productPrice", 1000L);
        ReflectionTestUtils.setField(dto, "salesRate", 10);
        ReflectionTestUtils.setField(dto, "productPublisher", "출판사");
        ReflectionTestUtils.setField(dto, "publishedAt", LocalDateTime.now());
        ReflectionTestUtils.setField(dto, "pageCount", 1000);
        ReflectionTestUtils.setField(dto, "salesPrice", 900L);
        ReflectionTestUtils.setField(dto, "priority", 5);

        // when
        doNothing().when(productService).modifyProductInfo(1L, dto);

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.put("/token/products/{productNo}/info", 1)
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andDo(document("product-modify-info-fail-over-isbn",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("productNo").description("상품 번호")
                        ),
                        requestFields(
                                fieldWithPath("productIsbn").description("상품 Isbn"),
                                fieldWithPath("productPrice").description("정가"),
                                fieldWithPath("salesRate").description("할인율"),
                                fieldWithPath("productPublisher").description("출판사"),
                                fieldWithPath("publishedAt").description("출판일"),
                                fieldWithPath("pageCount").description("페이지 수"),
                                fieldWithPath("salesPrice").description("할인가격"),
                                fieldWithPath("priority").description("우선순위")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("ISBN은 10자 혹은 13자입니다.")
                        )
                ));
    }

    @Test
    @DisplayName("상품 정보 수정 실패 테스트 (isbn 빈값)")
    void modifyProductInfo_fail_test_null_isbn() throws Exception {
        // given
        ModifyProductInfoRequestDto dto = new ModifyProductInfoRequestDto();
        ReflectionTestUtils.setField(dto, "productIsbn", null);
        ReflectionTestUtils.setField(dto, "productPrice", 1000L);
        ReflectionTestUtils.setField(dto, "salesRate", 10);
        ReflectionTestUtils.setField(dto, "productPublisher", "출판사");
        ReflectionTestUtils.setField(dto, "publishedAt", LocalDateTime.now());
        ReflectionTestUtils.setField(dto, "pageCount", 1000);
        ReflectionTestUtils.setField(dto, "salesPrice", 900L);
        ReflectionTestUtils.setField(dto, "priority", 5);

        // when
        doNothing().when(productService).modifyProductInfo(1L, dto);

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.put("/token/products/{productNo}/info", 1)
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andDo(document("product-modify-info-fail-null-isbn",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("productNo").description("상품 번호")
                        ),
                        requestFields(
                                fieldWithPath("productIsbn").description("상품 Isbn"),
                                fieldWithPath("productPrice").description("정가"),
                                fieldWithPath("salesRate").description("할인율"),
                                fieldWithPath("productPublisher").description("출판사"),
                                fieldWithPath("publishedAt").description("출판일"),
                                fieldWithPath("pageCount").description("페이지 수"),
                                fieldWithPath("salesPrice").description("할인가격"),
                                fieldWithPath("priority").description("우선순위")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("ISBN은 필수입니다.")
                        )
                ));
    }

    @Test
    @DisplayName("상품 정보 수정 실패 테스트 (null price)")
    void modifyProductInfo_fail_test_null_price() throws Exception {
        // given
        ModifyProductInfoRequestDto dto = new ModifyProductInfoRequestDto();
        ReflectionTestUtils.setField(dto, "productIsbn", "1234561232");
        ReflectionTestUtils.setField(dto, "productPrice", null);
        ReflectionTestUtils.setField(dto, "salesRate", 10);
        ReflectionTestUtils.setField(dto, "productPublisher", "출판사");
        ReflectionTestUtils.setField(dto, "publishedAt", LocalDateTime.now());
        ReflectionTestUtils.setField(dto, "pageCount", 1000);
        ReflectionTestUtils.setField(dto, "salesPrice", 900L);
        ReflectionTestUtils.setField(dto, "priority", 5);

        // when
        doNothing().when(productService).modifyProductInfo(1L, dto);

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.put("/token/products/{productNo}/info", 1)
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andDo(document("product-modify-info-fail-null-productPrice",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("productNo").description("상품 번호")
                        ),
                        requestFields(
                                fieldWithPath("productIsbn").description("상품 Isbn"),
                                fieldWithPath("productPrice").description("정가"),
                                fieldWithPath("salesRate").description("할인율"),
                                fieldWithPath("productPublisher").description("출판사"),
                                fieldWithPath("publishedAt").description("출판일"),
                                fieldWithPath("pageCount").description("페이지 수"),
                                fieldWithPath("salesPrice").description("할인가격"),
                                fieldWithPath("priority").description("우선순위")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("정가는 입력이 필수입니다.")
                        )
                ));
    }

    @Test
    @DisplayName("상품 정보 수정 실패 테스트 (null productPublisher)")
    void modifyProductInfo_fail_test_null_productPublisher() throws Exception {
        // given
        ModifyProductInfoRequestDto dto = new ModifyProductInfoRequestDto();
        ReflectionTestUtils.setField(dto, "productIsbn", "1234561232");
        ReflectionTestUtils.setField(dto, "productPrice", 100L);
        ReflectionTestUtils.setField(dto, "salesRate", 10);
        ReflectionTestUtils.setField(dto, "productPublisher", null);
        ReflectionTestUtils.setField(dto, "publishedAt", LocalDateTime.now());
        ReflectionTestUtils.setField(dto, "pageCount", 1000);
        ReflectionTestUtils.setField(dto, "salesPrice", 900L);
        ReflectionTestUtils.setField(dto, "priority", 5);

        // when
        doNothing().when(productService).modifyProductInfo(1L, dto);

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.put("/token/products/{productNo}/info", 1)
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andDo(document("product-modify-info-fail-null-productPublisher",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("productNo").description("상품 번호")
                        ),
                        requestFields(
                                fieldWithPath("productIsbn").description("상품 Isbn"),
                                fieldWithPath("productPrice").description("정가"),
                                fieldWithPath("salesRate").description("할인율"),
                                fieldWithPath("productPublisher").description("출판사"),
                                fieldWithPath("publishedAt").description("출판일"),
                                fieldWithPath("pageCount").description("페이지 수"),
                                fieldWithPath("salesPrice").description("할인가격"),
                                fieldWithPath("priority").description("우선순위")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("출판사 값 입력은 필수입니다.")
                        )
                ));
    }

    @Test
    @DisplayName("상품 정보 수정 실패 테스트 (over productPublisher)")
    void modifyProductInfo_fail_test_over_productPublisher() throws Exception {
        // given
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            sb.append("출판사");
        }

        ModifyProductInfoRequestDto dto = new ModifyProductInfoRequestDto();
        ReflectionTestUtils.setField(dto, "productIsbn", "1234561232");
        ReflectionTestUtils.setField(dto, "productPrice", 100L);
        ReflectionTestUtils.setField(dto, "salesRate", 10);
        ReflectionTestUtils.setField(dto, "productPublisher", sb.toString());
        ReflectionTestUtils.setField(dto, "publishedAt", LocalDateTime.now());
        ReflectionTestUtils.setField(dto, "pageCount", 1000);
        ReflectionTestUtils.setField(dto, "salesPrice", 900L);
        ReflectionTestUtils.setField(dto, "priority", 5);

        // when
        doNothing().when(productService).modifyProductInfo(1L, dto);

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.put("/token/products/{productNo}/info", 1)
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andDo(document("product-modify-info-fail-over-productPublisher",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("productNo").description("상품 번호")
                        ),
                        requestFields(
                                fieldWithPath("productIsbn").description("상품 Isbn"),
                                fieldWithPath("productPrice").description("정가"),
                                fieldWithPath("salesRate").description("할인율"),
                                fieldWithPath("productPublisher").description("출판사"),
                                fieldWithPath("publishedAt").description("출판일"),
                                fieldWithPath("pageCount").description("페이지 수"),
                                fieldWithPath("salesPrice").description("할인가격"),
                                fieldWithPath("priority").description("우선순위")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("50자를 넘을 수 없습니다.")
                        )
                ));
    }

    @Test
    @DisplayName("상품 정보 수정 실패 테스트 (null salesPrice)")
    void modifyProductInfo_fail_test_null_salesPrice() throws Exception {
        // given
        ModifyProductInfoRequestDto dto = new ModifyProductInfoRequestDto();
        ReflectionTestUtils.setField(dto, "productIsbn", "1234561232");
        ReflectionTestUtils.setField(dto, "productPrice", 100L);
        ReflectionTestUtils.setField(dto, "salesRate", 10);
        ReflectionTestUtils.setField(dto, "productPublisher", "출판사");
        ReflectionTestUtils.setField(dto, "publishedAt", LocalDateTime.now());
        ReflectionTestUtils.setField(dto, "pageCount", 1000);
        ReflectionTestUtils.setField(dto, "salesPrice", null);
        ReflectionTestUtils.setField(dto, "priority", 5);

        // when
        doNothing().when(productService).modifyProductInfo(1L, dto);

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.put("/token/products/{productNo}/info", 1)
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andDo(document("product-modify-info-fail-null-salesPrice",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("productNo").description("상품 번호")
                        ),
                        requestFields(
                                fieldWithPath("productIsbn").description("상품 Isbn"),
                                fieldWithPath("productPrice").description("정가"),
                                fieldWithPath("salesRate").description("할인율"),
                                fieldWithPath("productPublisher").description("출판사"),
                                fieldWithPath("publishedAt").description("출판일"),
                                fieldWithPath("pageCount").description("페이지 수"),
                                fieldWithPath("salesPrice").description("할인가격"),
                                fieldWithPath("priority").description("우선순위")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("판매가는 필수입니다.")
                        )
                ));
    }

    @Test
    @DisplayName("상품 카테고리 수정 api 성공 테스트")
    void modifyProductCategory_success_test() throws Exception {
        // given
        ModifyProductCategoryRequestDto dto = new ModifyProductCategoryRequestDto();
        ReflectionTestUtils.setField(dto, "categoriesNo", List.of(1, 2));

        // when
        doNothing().when(productService)
                .modifyProductCategory(1L, dto);

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.put("/token/products/{productNo}/category", 1)
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("product-modify-category-success",
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("productNo").description("상품 번호")
                        ),
                        requestFields(
                                fieldWithPath("categoriesNo").description("카테고리 번호")
                        )
                ));

        verify(productService, times(1))
                .modifyProductCategory(anyLong(), any(ModifyProductCategoryRequestDto.class));
    }

    @Test
    @DisplayName("상품 카테고리 수정 실패 테스트 (null category No) ")
    void modifyProductCategory_fail_test_null_categoryNos() throws Exception {
        // given
        ModifyProductCategoryRequestDto dto = new ModifyProductCategoryRequestDto();
        ReflectionTestUtils.setField(dto, "categoriesNo", null);

        // when
        doNothing().when(productService)
                .modifyProductCategory(1L, dto);

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.put("/token/products/{productNo}/category", 1)
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andDo(document("product-modify-category-fail-null-categoryNo",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("productNo").description("상품 번호")
                        ),
                        requestFields(
                                fieldWithPath("categoriesNo").description("카테고리 번호")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("카테고리 번호는 필수입니다.")
                        )
                ));
    }

    @Test
    @DisplayName("상품 저자 수정 API 성공 테스트")
    void modifyProductAuthor_success_test() throws Exception {
        // given
        ModifyProductAuthorRequestDto dto = new ModifyProductAuthorRequestDto();
        ReflectionTestUtils.setField(dto, "authors", List.of(1, 2));

        // when
        doNothing().when(productService).modifyProductAuthor(1L, dto);

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.put("/token/products/{productNo}/author", 1)
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("product-modify-author-success",
                        preprocessRequest(prettyPrint()),
                        pathParameters(
                                parameterWithName("productNo").description("상품 번호")
                        ),
                        requestFields(
                                fieldWithPath("authors").description("저자 번호")
                        )
                ));

        verify(productService, times(1))
                .modifyProductAuthor(anyLong(), any(ModifyProductAuthorRequestDto.class));
    }

    @Test
    @DisplayName("상품 저자 수정 실패 테스트 (null authorsNo)")
    void modifyProductAuthor_fail_test_null_authorsNo() throws Exception {
        // given
        ModifyProductAuthorRequestDto dto = new ModifyProductAuthorRequestDto();
        ReflectionTestUtils.setField(dto, "authors", null);

        // when
        doNothing().when(productService).modifyProductAuthor(1L, dto);

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.put("/token/products/{productNo}/author", 1)
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andDo(document("product-modify-author-fail-null-authorNo",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("productNo").description("상품 번호")
                        ),
                        requestFields(
                                fieldWithPath("authors").description("저자 번호")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("저자 번호는 필수입니다.")
                        )
                ));
    }

    @Test
    @DisplayName("상품 태그 수정 API 성공 테스트")
    void modifyProductTag_success_test() throws Exception {
        // given
        ModifyProductTagRequestDto dto = new ModifyProductTagRequestDto();
        ReflectionTestUtils.setField(dto, "tags", List.of(1, 2));

        // when
        doNothing().when(productService).modifyProductTag(1L, dto);

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.put("/token/products/{productNo}/tag", 1)
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("product-modify-tag-success",
                        preprocessRequest(prettyPrint()),
                        pathParameters(
                                parameterWithName("productNo").description("상품 번호")
                        ),
                        requestFields(
                                fieldWithPath("tags").description("태그 번호")
                        )
                ));

        verify(productService, times(1))
                .modifyProductTag(anyLong(), any(ModifyProductTagRequestDto.class));
    }

    @Test
    @DisplayName("상품 태그 수정 실패 테스트 (null tagNo)")
    void modifyProductTag_fail_test_null_tagNo() throws Exception {
        // given
        ModifyProductTagRequestDto dto = new ModifyProductTagRequestDto();
        ReflectionTestUtils.setField(dto, "tags", null);

        // when
        doNothing().when(productService).modifyProductTag(1L, dto);

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.put("/token/products/{productNo}/tag", 1)
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andDo(document("product-modify-tag-fail-null-tagNo",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("productNo").description("상품 번호")
                        ),
                        requestFields(
                                fieldWithPath("tags").description("태그 번호")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("태그 번호는 필수입니다.")
                        )
                ));
    }

    @Test
    @DisplayName("상품 유형 수정 API 성공 테스트")
    void modifyProductType_success_test() throws Exception {
        // given
        Integer typeStateNo = 1;

        // when
        doNothing().when(productService).modifyProductType(1L, 1);

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.put("/token/products/{productNo}/type", 1)
                        .param("no", mapper.writeValueAsString(typeStateNo))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("product-modify-type-success",
                        pathParameters(
                                parameterWithName("productNo").description("상품 번호")
                        ),
                        requestParameters(
                                parameterWithName("no").description("상품 타입 번호")
                        )
                ));

        verify(productService, times(1))
                .modifyProductType(anyLong(), anyInt());
    }

    @Test
    @DisplayName("상품 판매 유형 수정 API 성공 테스트")
    void modifyProductSale_success_test() throws Exception {
        // given
        Integer saleCode = 1;

        // when
        doNothing().when(productService).modifyProductSale(anyLong(), anyInt());

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.put("/token/products/{productNo}/sale", 1)
                        .param("no", mapper.writeValueAsString(saleCode))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("product-modify-sale-success",
                        pathParameters(
                                parameterWithName("productNo").description("상품 번호")
                        ),
                        requestParameters(
                                parameterWithName("no").description("상품 판매 유형 번호")
                        )
                ));

        verify(productService, times(1))
                .modifyProductSale(anyLong(), anyInt());
    }

    @Test
    @DisplayName("상품 포인트 정책 수정 API 성공 테스트")
    void modifyProductPolicy_success_test() throws Exception {
        // given
        Integer policyNo = 1;

        // when
        doNothing().when(productService).modifyProductPolicy(anyLong(), anyInt());

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.put("/token/products/{productNo}/policy", 1)
                        .param("no", mapper.writeValueAsString(policyNo))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("product-modify-policy-success",
                        pathParameters(
                                parameterWithName("productNo").description("상품 번호")
                        ),
                        requestParameters(
                                parameterWithName("no").description("상품 포인트 정책 번호")
                        )
                ));

        verify(productService, times(1))
                .modifyProductPolicy(anyLong(), anyInt());
    }

    @Test
    @DisplayName("E-Book 정보 조회 성공 API 테스트")
    void getEBookInfo_success_test() throws Exception {
        // given
        GetDownloadInfo info = new GetDownloadInfo("object storage path", "token", "파일 본명", "파일 저장명", "파일 확장자");

        // when
        when(productService.getEbookInfo(anyLong()))
                .thenReturn(info);

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/token/downloads/e-book/{productNo}/{memberNo}", 1, 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("path").value(info.getPath()))
                .andExpect(jsonPath("token").value(info.getToken()))
                .andExpect(jsonPath("nameOrigin").value(info.getNameOrigin()))
                .andExpect(jsonPath("nameSaved").value(info.getNameSaved()))
                .andExpect(jsonPath("fileExtension").value(info.getFileExtension()))
                .andDo(print())
                .andDo(document("product-get-EBook-success",
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("productNo").description("상품 번호"),
                                parameterWithName("memberNo").description("멤버 번호")
                        ),
                        responseFields(
                                fieldWithPath("path").description("오브젝트 스토리지 경로"),
                                fieldWithPath("token").description("오브젝트 스토리지 접근 경로"),
                                fieldWithPath("nameOrigin").description("파일 본명"),
                                fieldWithPath("nameSaved").description("파일 저장명"),
                                fieldWithPath("fileExtension").description("파일 확장자")
                        )
                ));

        verify(productService, times(1))
                .getEbookInfo(anyLong());
    }

    @Test
    @DisplayName("상품 설명 수정 성공 API 테스트")
    void modifyProductDescription_success_test() throws Exception {
        // given
        ModifyProductDescriptionRequestDto dto = new ModifyProductDescriptionRequestDto();
        ReflectionTestUtils.setField(dto, "description", "# 설명");

        // when
        doNothing().when(productService).modifyProductDescription(anyLong(), any(ModifyProductDescriptionRequestDto.class));

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.put("/token/products/{productNo}/description", 1)
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("product-modify-description-success",
                        preprocessRequest(prettyPrint()),
                        pathParameters(
                                parameterWithName("productNo").description("상품 번호")
                        ),
                        requestFields(
                                fieldWithPath("description").description("상품 설명")
                        )
                ));

        verify(productService, times(1))
                .modifyProductDescription(anyLong(), any(ModifyProductDescriptionRequestDto.class));
    }

    @Test
    @DisplayName("상품 설명 수정 실패 테스트 (over description)")
    void modifyProductDescription_fail_test_over_description() throws Exception {
        // given
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            sb.append("초과된 상품 설명");
        }

        ModifyProductDescriptionRequestDto dto = new ModifyProductDescriptionRequestDto();
        ReflectionTestUtils.setField(dto, "description", sb.toString());

        // when
        doNothing().when(productService).modifyProductDescription(anyLong(), any(ModifyProductDescriptionRequestDto.class));

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.put("/token/products/{productNo}/description", 1)
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andDo(document("product-modify-description-fail-over-description",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("productNo").description("상품 번호")
                        ),
                        requestFields(
                                fieldWithPath("description").description("상품 설명")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("5000자를 넘길 수 없습니다.")
                        )
                ));
    }

    @Test
    @DisplayName("상품 설명 수정 실패 테스트 (null description)")
    void modifyProductDescription_fail_test_null_description() throws Exception {
        // given
        ModifyProductDescriptionRequestDto dto = new ModifyProductDescriptionRequestDto();
        ReflectionTestUtils.setField(dto, "description", null);

        // when
        doNothing().when(productService).modifyProductDescription(anyLong(), any(ModifyProductDescriptionRequestDto.class));

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.put("/token/products/{productNo}/description", 1)
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andDo(document("product-modify-description-fail-null-description",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("productNo").description("상품 번호")
                        ),
                        requestFields(
                                fieldWithPath("description").description("상품 설명")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("설명은 비어있으면 안됩니다.")
                        )
                ));
    }

    @Test
    @DisplayName("E-Book 수정 API 테스트")
    void modifyEBook_success_test() throws Exception {
        // given
        String imageContent = "234kh2kl4h2l34k2j34hlk23h4";
        MockMultipartFile eBook = new MockMultipartFile("ebook", "imageName.jpeg", "image/jpeg", imageContent.getBytes());

        // when
        doNothing().when(productService).modifyProductEbook(1L, eBook);

        // then
        mockMvc.perform(multipart("/token/products/{productNo}/e-book", 1)
                        .file(eBook)
                        .with(req -> {
                            req.setMethod("PUT");
                            return req;
                        }))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("product-modify-eBook-success",
                        preprocessRequest(prettyPrint()),
                        requestParts(
                                partWithName("ebook").description("E-Book 파일")
                        )
                ));
    }

    @Test
    @DisplayName("썸네일 파일 수정 API 테스트")
    void modifyThumbnail_success_test() throws Exception {
        // given
        String imageContent = "234kh2kl4h2l34k2j34hlk23h4";
        MockMultipartFile thumbnail = new MockMultipartFile("image", "imageName.jpeg", "image/jpeg", imageContent.getBytes());

        // when
        doNothing().when(productService).modifyProductImage(1L, thumbnail);

        // then
        mockMvc.perform(multipart("/token/products/{productNo}/thumbnail", 1)
                        .file(thumbnail)
                        .with(req -> {
                            req.setMethod("PUT");
                            return req;
                        }))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("product-modify-thumbnail-success",
                        preprocessRequest(prettyPrint()),
                        requestParts(
                                partWithName("image").description("썸네일 이미지 파일")
                        )
                ));
    }

    @Test
    @DisplayName("상품 상세이미지 파일 수정 API 테스트")
    void modifyDetailImage_success_test() throws Exception {
        // given
        String imageContent = "234kh2kl4h2l34k2j34hlk23h4";
        MockMultipartFile detailImage = new MockMultipartFile("detailImage", "imageName.jpeg", "image/jpeg", imageContent.getBytes());

        // when
        doNothing().when(productService).modifyProductDetailImage(1L, detailImage);

        // then
        mockMvc.perform(multipart("/token/products/{productNo}/detail-image", 1)
                        .file(detailImage)
                        .with(req -> {
                            req.setMethod("PUT");
                            return req;
                        }))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("product-modify-detail-image-success",
                        preprocessRequest(prettyPrint()),
                        requestParts(
                                partWithName("detailImage").description("상세 이미지 파일")
                        )
                ));
    }


    @Test
    @DisplayName("상품 썸네일 이미지 파일 추가 API 테스트")
    void addProductImage_success_test() throws Exception {
        // given
        String imageContent = "234kh2kl4h2l34k2j34hlk23h4";
        MockMultipartFile thumbnail = new MockMultipartFile("image", "imageName.jpeg", "image/jpeg", imageContent.getBytes());

        // when
        doNothing().when(productService).addProductImage(1L, thumbnail);

        // then
        mockMvc.perform(multipart("/token/products/{productNo}/new-thumbnail", 1)
                        .file(thumbnail)
                        .with(req -> {
                            req.setMethod("PUT");
                            return req;
                        }))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("product-modify-new-thumbnail-success",
                        preprocessRequest(prettyPrint()),
                        requestParts(
                                partWithName("image").description("썸네일 이미지 파일")
                        )
                ));
    }

    @Test
    @DisplayName("상품 상세 이미지 파일 추가 API 테스트")
    void addProductDetailImage_success_test() throws Exception {
        // given
        String imageContent = "234kh2kl4h2l34k2j34hlk23h4";
        MockMultipartFile detailImage = new MockMultipartFile("detailImage", "imageName.jpeg", "image/jpeg", imageContent.getBytes());

        // when
        doNothing().when(productService).addProductDetailImage(1L, detailImage);

        // then
        mockMvc.perform(multipart("/token/products/{productNo}/new-detail-image", 1)
                        .file(detailImage)
                        .with(req -> {
                            req.setMethod("PUT");
                            return req;
                        }))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("product-modify-new-detail-image-success",
                        preprocessRequest(prettyPrint()),
                        requestParts(
                                partWithName("detailImage").description("상세 이미지 파일")
                        )
                ));
    }

}
