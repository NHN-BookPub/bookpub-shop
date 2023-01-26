package com.nhnacademy.bookpubshop.product.controller;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import com.nhnacademy.bookpubshop.product.dto.request.CreateProductRequestDto;
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
import com.nhnacademy.bookpubshop.tag.dummy.TagDummy;
import com.nhnacademy.bookpubshop.tag.entity.Tag;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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
    Author author;
    Category category;
    Tag tag;

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

        requestDto = new CreateProductRequestDto();
        responseDto = new GetProductDetailResponseDto(product);

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
                .andDo(document("get-products",
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
                                fieldWithPath("content[].productPrice").description("상품 정가"),
                                fieldWithPath("content[].deleted").description("상품 삭제여부")
                        )));

        verify(productService, times(1)).getAllProducts(pageable);

    }

    @Test
    @DisplayName("상품 생성 성공")
    void productAddSuccessTest() throws Exception {
        doNothing().when(productService).createProduct(requestDto);

        mockMvc.perform(MockMvcRequestBuilders.post(url)
                        .content(mapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("product-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("productIsbn").type(JsonFieldType.STRING).description("상품 ISBN"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("상품 제목"),
                                fieldWithPath("productPublisher").type(JsonFieldType.STRING).description("상품 출판사"),
                                fieldWithPath("pageCount").type(JsonFieldType.NUMBER).description("상품 총 페이지 수"),
                                fieldWithPath("productDescription").type(JsonFieldType.STRING).description("상품 설명"),
                                fieldWithPath("salePrice").type(JsonFieldType.NUMBER).description("판매가"),
                                fieldWithPath("productPrice").type(JsonFieldType.NUMBER).description("정가"),
                                fieldWithPath("salesRate").type(JsonFieldType.NUMBER).description("할인율"),
                                fieldWithPath("productPriority").type(JsonFieldType.NUMBER).description("상품 우선순위(숫자가 작을수록 우선순위가 높음)"),
                                fieldWithPath("productStock").type(JsonFieldType.NUMBER).description("상품 재고수량"),
                                fieldWithPath("publishedAt").type(JsonFieldType.STRING).description("출판일시"),
                                fieldWithPath("subscribed").type(JsonFieldType.BOOLEAN).description("상품 구독가능여부"),
                                fieldWithPath("productPolicyNo").type(JsonFieldType.NUMBER).description("상품 정책번호"),
                                fieldWithPath("saleCodeNo").type(JsonFieldType.NUMBER).description("상품판매여부 코드번호()"),
                                fieldWithPath("typeCodeNo").type(JsonFieldType.NUMBER).description("상품유형 코드번호"),
                                fieldWithPath("authorsNo[]").type(JsonFieldType.ARRAY).description("작가 번호 리스트"),
                                fieldWithPath("categoriesNo[]").type(JsonFieldType.ARRAY).description("카테고리 번호 리스트"),
                                fieldWithPath("tagsNo[]").type(JsonFieldType.ARRAY).description("태그 번호 리스트"),
                                fieldWithPath("relationProducts[]").type(JsonFieldType.ARRAY).description("연관상품 번호 리스트")
                        )));

        then(productService).should().createProduct(any(CreateProductRequestDto.class));
    }

    @Test
    @DisplayName("상품 생성 실패_ISBN이 없는 경우")
    void productAddFailTest_IsbnIsNull() throws Exception {
        ReflectionTestUtils.setField(requestDto, "productIsbn", null);

        doNothing().when(productService).createProduct(requestDto);

        mockMvc.perform(MockMvcRequestBuilders.post(url)
                        .content(mapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andDo(document("product-create-isbnFail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].message").description("ISBN을 입력해주세요.")
                        )));

        verify(productService, times(0)).createProduct(any(CreateProductRequestDto.class));
    }

    @Test
    @DisplayName("상품 생성 실패_ISBN 길이가 초과된 경우")
    void productAddFailTest_IsbnIsTooLong() throws Exception {
        ReflectionTestUtils.setField(requestDto, "productIsbn", "asdfasdfasdfasdfsadfsa");

        doNothing().when(productService).createProduct(requestDto);

        mockMvc.perform(MockMvcRequestBuilders.post(url)
                        .content(mapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andDo(document("product-create-isbnFail-tooLong",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].message").description("ISBN은 10자 혹은 13자입니다.")
                        )));

        verify(productService, times(0)).createProduct(any(CreateProductRequestDto.class));
    }

    @Test
    @DisplayName("상품 생성 실패_제목 길이가 초과된 경우")
    void productAddFailTest_TitleIsTooLong() throws Exception {
        ReflectionTestUtils.setField(requestDto, "title",
                "asdfasdfasdfasdfsadfsaasdfasdfdasdfasfafasfdasdfasfasfdaasdfasdfasdfasdfasdfsfsadfasfasfsafsafasfsafsafsafdsdf");

        doNothing().when(productService).createProduct(requestDto);

        mockMvc.perform(MockMvcRequestBuilders.post(url)
                        .content(mapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andDo(document("product-create-titleFail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].message").description("제목은 최대 100자입니다.")
                        )));

        verify(productService, times(0)).createProduct(any(CreateProductRequestDto.class));
    }

    @Test
    @DisplayName("상품 생성 실패_출판사가 null인 경우")
    void productAddFailTest_productPublisherIsNull() throws Exception {
        ReflectionTestUtils.setField(requestDto, "productPublisher", null);

        doNothing().when(productService).createProduct(requestDto);

        mockMvc.perform(MockMvcRequestBuilders.post(url)
                        .content(mapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andDo(document("product-create-productPublisherFail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].message").description("출판사를 입력해주세요.")
                        )));

        verify(productService, times(0)).createProduct(any(CreateProductRequestDto.class));
    }

    @Test
    @DisplayName("상품 생성 실패_출판사 이름 길이를 초과한 경우")
    void productAddFailTest_productPublisherTooLong() throws Exception {
        ReflectionTestUtils.setField(requestDto, "productPublisher",
                "asdfasdfasdfadfasdfasdfasdfadfasfsafasdfsadfasdfsadfasdfsadfasdffd");

        doNothing().when(productService).createProduct(requestDto);

        mockMvc.perform(MockMvcRequestBuilders.post(url)
                        .content(mapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andDo(document("product-create-productPublisherFail-tooLong",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].message").description("50자를 넘을 수 없습니다.")
                        )));

        verify(productService, times(0)).createProduct(any(CreateProductRequestDto.class));
    }

    @Test
    @DisplayName("상품 생성 실패_상품 설명 길이를 초과한 경우")
    void productAddFailTest_productDescriptionTooLong() throws Exception {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 2050; i++) {
            sb.append("a");
        }

        String result = sb.toString();

        ReflectionTestUtils.setField(requestDto, "productDescription", result);

        doNothing().when(productService).createProduct(requestDto);

        mockMvc.perform(MockMvcRequestBuilders.post(url)
                        .content(mapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andDo(document("product-create-productDescriptionFail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].message").description("설명은 최대 2000자입니다.")
                        )));

        verify(productService, times(0)).createProduct(any(CreateProductRequestDto.class));
    }

    @Test
    @DisplayName("상품 생성 실패_판매가가 null인 경우")
    void productAddFailTest_salePriceIsNull() throws Exception {
        ReflectionTestUtils.setField(requestDto, "salePrice", null);

        doNothing().when(productService).createProduct(requestDto);

        mockMvc.perform(MockMvcRequestBuilders.post(url)
                        .content(mapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andDo(document("product-create-salePriceFail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].message").description("판매가를 입력해주세요.")
                        )));

        verify(productService, times(0)).createProduct(any(CreateProductRequestDto.class));
    }

    @Test
    @DisplayName("상품 생성 실패_정가가 null인 경우")
    void productAddFailTest_productPriceIsNull() throws Exception {
        ReflectionTestUtils.setField(requestDto, "productPrice", null);

        doNothing().when(productService).createProduct(requestDto);

        mockMvc.perform(MockMvcRequestBuilders.post(url)
                        .content(mapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andDo(document("product-create-productPriceFail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].message").description("정가를 입력해주세요.")
                        )));

        verify(productService, times(0)).createProduct(any(CreateProductRequestDto.class));
    }

    @Test
    @DisplayName("상품 생성 실패_상품 재가 null인 경우")
    void productAddFailTest_productStockIsNull() throws Exception {
        ReflectionTestUtils.setField(requestDto, "productStock", null);

        doNothing().when(productService).createProduct(requestDto);

        mockMvc.perform(MockMvcRequestBuilders.post(url)
                        .content(mapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andDo(document("product-create-productStockFail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].message").description("상품 재고를 입력해주세요.")
                        )));

        verify(productService, times(0)).createProduct(any(CreateProductRequestDto.class));
    }

    @Test
    @DisplayName("상품 생성 실패_출판 일시가 null인 경우")
    void productAddFailTest_publishedAtIsNull() throws Exception {
        ReflectionTestUtils.setField(requestDto, "publishedAt", null);

        doNothing().when(productService).createProduct(requestDto);

        mockMvc.perform(MockMvcRequestBuilders.post(url)
                        .content(mapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andDo(document("product-create-publishedAtFail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].message").description("출판일시를 입력해주세요.")
                        )));

        verify(productService, times(0)).createProduct(any(CreateProductRequestDto.class));
    }

    @Test
    @DisplayName("상품 생성 실패_상품 정책 번호가 null인 경우")
    void productAddFailTest_productPolicyNoIsNull() throws Exception {
        ReflectionTestUtils.setField(requestDto, "productPolicyNo", null);

        doNothing().when(productService).createProduct(requestDto);

        mockMvc.perform(MockMvcRequestBuilders.post(url)
                        .content(mapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andDo(document("product-create-productPolicyNoFail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].message").description("상품정책 번호를 입력해주세요.")
                        )));

        verify(productService, times(0)).createProduct(any(CreateProductRequestDto.class));
    }

    @Test
    @DisplayName("상품 생성 실패_상품판매여부코드 번가 null인 경우")
    void productAddFailTest_saleCodeNoIsNull() throws Exception {
        ReflectionTestUtils.setField(requestDto, "saleCodeNo", null);

        doNothing().when(productService).createProduct(requestDto);

        mockMvc.perform(MockMvcRequestBuilders.post(url)
                        .content(mapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andDo(document("product-create-saleCodeNoFail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].message").description("상품판매여부코드 번호를 입력해주세요.")
                        )));

        verify(productService, times(0)).createProduct(any(CreateProductRequestDto.class));
    }

    @Test
    @DisplayName("상품 생성 실패_상품판매여부코드 번호가 null인 경우")
    void productAddFailTest_typeCodeNoIsNull() throws Exception {
        ReflectionTestUtils.setField(requestDto, "typeCodeNo", null);

        doNothing().when(productService).createProduct(requestDto);

        mockMvc.perform(MockMvcRequestBuilders.post(url)
                        .content(mapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andDo(document("product-create-typeCodeNoFail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].message").description("상품유형코드 번호를 입력해주세요.")
                        )));

        verify(productService, times(0)).createProduct(any(CreateProductRequestDto.class));
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
                .andExpect(jsonPath("$.publishDate").value(requestDto.getPublishedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
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
                .andDo(document("get-product",
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
                                fieldWithPath("tagsColors[]").description("태그 색 리스트")
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
                                fieldWithPath("content[].deleted").description("상품의 삭제 여부")
                        )));

        verify(productService, times(1)).getProductListLikeTitle("test", pageable);
    }

    @Test
    @DisplayName("상품 수정 성공")
    void modifyProduct() throws Exception {
        doNothing()
                .when(productService)
                .modifyProduct(requestDto, 1L);

        mockMvc.perform(RestDocumentationRequestBuilders.put(url + "/{productNo}", 1L)
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
                                fieldWithPath("tagsNo[]").description("상품 태그 리스트"),
                                fieldWithPath("relationProducts[]").description("연관 관계 리스트")
                        )));

        verify(productService, times(1))
                .modifyProduct(any(), anyLong());
    }

    @Test
    @DisplayName("상품 삭제 여부 설정 성공")
    void setDeletedProduct() throws Exception {
        doNothing().when(productService)
                .setDeleteProduct(product.getProductNo());

        mockMvc.perform(RestDocumentationRequestBuilders.put(url + "/deleted/{productNo}", 1L)
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
                                fieldWithPath("[].productCategories[]").description("상품 카테고리 리스트")
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
                                fieldWithPath("[].categories[]").description("상품 카테고리 리스트"),
                                fieldWithPath("[].tags[]").description("상품 태그 리스트"),
                                fieldWithPath("[].tagsColors[]").description("태그 색 리스트")
                        )));
    }

}