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
 * ProductController ?????????.
 *
 * @author : ?????????
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
    @DisplayName("?????? ?????? ?????? ??????")
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
                                parameterWithName("page").description("????????? ????????? ??????"),
                                parameterWithName("size").description("????????? ?????????")
                        ),
                        responseFields(
                                fieldWithPath("totalPages").description("??? ????????? ??????"),
                                fieldWithPath("number").description("?????? ????????? ??????"),
                                fieldWithPath("previous").description("?????? ????????? ?????? ??????"),
                                fieldWithPath("next").description("?????? ????????? ?????? ??????"),
                                fieldWithPath("content[].productNo").description("?????? ??????"),
                                fieldWithPath("content[].title").description("?????? ??????"),
                                fieldWithPath("content[].productStock").description("?????? ????????????"),
                                fieldWithPath("content[].salesPrice").description("?????? ?????????"),
                                fieldWithPath("content[].saleRate").description("????????? ?????????"),
                                fieldWithPath("content[].productSubscribed").description("?????? ?????? ??????"),
                                fieldWithPath("content[].productPrice").description("?????? ??????"),
                                fieldWithPath("content[].deleted").description("?????? ????????????"),
                                fieldWithPath("content[].thumbnail").description("?????? ?????????")
                        )));

        verify(productService, times(1)).getAllProducts(pageable);

    }

    @Test
    @DisplayName("?????? ?????? ??????")
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
                                partWithName("thumbnail").description("?????? ?????????"),
                                partWithName("detail").description("?????? ???????????????"),
                                partWithName("ebook").description("?????? ?????? ??????"),
                                partWithName("requestDto").description("?????? ????????? ???????????? Dto")
                        )));

        then(productService).should().createProduct(any(CreateProductRequestDto.class), any());
    }

    @Test
    @DisplayName("?????? ?????? ??????_ISBN??? ?????? ??????")
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
                                partWithName("thumbnail").description("?????? ?????????"),
                                partWithName("detail").description("?????? ???????????????"),
                                partWithName("ebook").description("?????? ?????? ??????"),
                                partWithName("requestDto").description("?????? ????????? ???????????? Dto")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("ISBN??? ??????????????????.")
                        )));

        verify(productService, times(0)).createProduct(any(CreateProductRequestDto.class), any());
    }

    @Test
    @DisplayName("?????? ?????? ??????_ISBN ????????? ????????? ??????")
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
                                partWithName("thumbnail").description("?????? ?????????"),
                                partWithName("detail").description("?????? ???????????????"),
                                partWithName("ebook").description("?????? ?????? ??????"),
                                partWithName("requestDto").description("?????? ????????? ???????????? Dto")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("ISBN??? 10??? ?????? 13????????????.")
                        )));

        verify(productService, times(0)).createProduct(any(CreateProductRequestDto.class), any());
    }

    @Test
    @DisplayName("?????? ?????? ??????_?????? ????????? ????????? ??????")
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
                                partWithName("thumbnail").description("?????? ?????????"),
                                partWithName("detail").description("?????? ???????????????"),
                                partWithName("ebook").description("?????? ?????? ??????"),
                                partWithName("requestDto").description("?????? ????????? ???????????? Dto")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("????????? ?????? 100????????????.")
                        )));

        verify(productService, times(0)).createProduct(any(CreateProductRequestDto.class), any());
    }

    @Test
    @DisplayName("?????? ?????? ??????_???????????? null??? ??????")
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
                                partWithName("thumbnail").description("?????? ?????????"),
                                partWithName("detail").description("?????? ???????????????"),
                                partWithName("ebook").description("?????? ?????? ??????"),
                                partWithName("requestDto").description("?????? ????????? ???????????? Dto")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("???????????? ??????????????????.")
                        )));

        verify(productService, times(0)).createProduct(any(CreateProductRequestDto.class), any());
    }

    @Test
    @DisplayName("?????? ?????? ??????_????????? ?????? ????????? ????????? ??????")
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
                                partWithName("thumbnail").description("?????? ?????????"),
                                partWithName("detail").description("?????? ???????????????"),
                                partWithName("ebook").description("?????? ?????? ??????"),
                                partWithName("requestDto").description("?????? ????????? ???????????? Dto")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("50?????? ?????? ??? ????????????.")
                        )));

        verify(productService, times(0)).createProduct(any(CreateProductRequestDto.class), any());
    }

    @Test
    @DisplayName("?????? ?????? ??????_?????? ?????? ????????? ????????? ??????")
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
                                partWithName("thumbnail").description("?????? ?????????"),
                                partWithName("detail").description("?????? ???????????????"),
                                partWithName("ebook").description("?????? ?????? ??????"),
                                partWithName("requestDto").description("?????? ????????? ???????????? Dto")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("????????? ?????? 2000????????????.")
                        )));

        verify(productService, times(0)).createProduct(any(CreateProductRequestDto.class), any());
    }

    @Test
    @DisplayName("?????? ?????? ??????_???????????? null??? ??????")
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
                                partWithName("thumbnail").description("?????? ?????????"),
                                partWithName("detail").description("?????? ???????????????"),
                                partWithName("ebook").description("?????? ?????? ??????"),
                                partWithName("requestDto").description("?????? ????????? ???????????? Dto")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("???????????? ??????????????????.")
                        )));

        verify(productService, times(0)).createProduct(any(CreateProductRequestDto.class), any());
    }

    @Test
    @DisplayName("?????? ?????? ??????_????????? null??? ??????")
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
                                partWithName("thumbnail").description("?????? ?????????"),
                                partWithName("detail").description("?????? ???????????????"),
                                partWithName("ebook").description("?????? ?????? ??????"),
                                partWithName("requestDto").description("?????? ????????? ???????????? Dto")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("????????? ??????????????????.")
                        )));

        verify(productService, times(0)).createProduct(any(CreateProductRequestDto.class), any());
    }

    @Test
    @DisplayName("?????? ?????? ??????_?????? ????????? null??? ??????")
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
                                partWithName("thumbnail").description("?????? ?????????"),
                                partWithName("detail").description("?????? ???????????????"),
                                partWithName("ebook").description("?????? ?????? ??????"),
                                partWithName("requestDto").description("?????? ????????? ???????????? Dto")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("?????? ????????? ??????????????????.")
                        )));

        verify(productService, times(0)).createProduct(any(CreateProductRequestDto.class), any());
    }

    @Test
    @DisplayName("?????? ?????? ??????_?????? ????????? null??? ??????")
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
                                partWithName("thumbnail").description("?????? ?????????"),
                                partWithName("detail").description("?????? ???????????????"),
                                partWithName("ebook").description("?????? ?????? ??????"),
                                partWithName("requestDto").description("?????? ????????? ???????????? Dto")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("??????????????? ??????????????????.")
                        )));

        verify(productService, times(0)).createProduct(any(CreateProductRequestDto.class), any());
    }

    @Test
    @DisplayName("?????? ?????? ??????_?????? ?????? ????????? null??? ??????")
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
                                partWithName("thumbnail").description("?????? ?????????"),
                                partWithName("detail").description("?????? ???????????????"),
                                partWithName("ebook").description("?????? ?????? ??????"),
                                partWithName("requestDto").description("?????? ????????? ???????????? Dto")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("???????????? ????????? ??????????????????.")
                        )));

        verify(productService, times(0)).createProduct(any(CreateProductRequestDto.class), any());
    }

    @Test
    @DisplayName("?????? ?????? ??????_???????????????????????? ?????? null??? ??????")
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
                                partWithName("thumbnail").description("?????? ?????????"),
                                partWithName("detail").description("?????? ???????????????"),
                                partWithName("ebook").description("?????? ?????? ??????"),
                                partWithName("requestDto").description("?????? ????????? ???????????? Dto")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("???????????????????????? ????????? ??????????????????.")
                        )));

        verify(productService, times(0)).createProduct(any(CreateProductRequestDto.class), any());
    }

    @Test
    @DisplayName("?????? ?????? ??????_???????????????????????? ????????? null??? ??????")
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
                                partWithName("thumbnail").description("?????? ?????????"),
                                partWithName("detail").description("?????? ???????????????"),
                                partWithName("ebook").description("?????? ?????? ??????"),
                                partWithName("requestDto").description("?????? ????????? ???????????? Dto")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("?????????????????? ????????? ??????????????????.")
                        )));

        verify(productService, times(0)).createProduct(any(CreateProductRequestDto.class), any());
    }

    @Test
    @DisplayName("?????? ?????? ?????? ??????")
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
                                parameterWithName("productNo").description("????????? ?????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("productNo").description("?????? ??????"),
                                fieldWithPath("productIsbn").description("????????? ISBN"),
                                fieldWithPath("title").description("?????? ??????"),
                                fieldWithPath("productPublisher").description("?????? ?????????"),
                                fieldWithPath("pageCount").description("????????? ??? ????????? ???"),
                                fieldWithPath("productDescription").description("?????? ??????"),
                                fieldWithPath("salesPrice").description("?????? ?????????"),
                                fieldWithPath("productPrice").description("?????? ??????"),
                                fieldWithPath("salesRate").description("????????? ?????????"),
                                fieldWithPath("productPriority").description("?????? ?????? ????????????(????????? ???????????? ???????????? ??????)"),
                                fieldWithPath("productStock").description("????????? ????????????"),
                                fieldWithPath("publishDate").description("?????? ?????? ??????"),
                                fieldWithPath("deleted").description("?????? ?????? ??????"),
                                fieldWithPath("productSubscribed").description("?????? ?????? ?????? ??????"),
                                fieldWithPath("saleStateCodeCategory").description("?????? ???????????? ??????(?????????, ?????? ???)"),
                                fieldWithPath("typeStateCodeName").description("?????? ?????? ?????? ??????(??????, ??????????????? ???))"),
                                fieldWithPath("policyMethod").description("?????? ????????? ?????? ?????? ??????(????????????, ????????? ???)"),
                                fieldWithPath("policySaved").description("????????? ????????? ?????? ??????"),
                                fieldWithPath("policySaveRate").description("?????? ????????? ?????????"),
                                fieldWithPath("authors[]").description("??????(??????) ?????? ?????????"),
                                fieldWithPath("categories[]").description("?????? ???????????? ?????????"),
                                fieldWithPath("tags[]").description("?????? ?????? ?????????"),
                                fieldWithPath("tagsColors[]").description("?????? ??? ?????????"),
                                fieldWithPath("categoriesNo[]").description("?????? ???????????? ?????? ?????????"),
                                fieldWithPath("thumbnail").description("?????? ?????????"),
                                fieldWithPath("detail").description("?????? ???????????????"),
                                fieldWithPath("ebook").description("?????? ??????"),
                                fieldWithPath("info").description("???????????? ??????")
                        )));

    }

    @Test
    @DisplayName("?????? ?????? ?????? ??????")
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
                                parameterWithName("title").description("?????? ?????? ??? ????????? ??? ??????"),
                                parameterWithName("page").description("????????? ????????? ??????"),
                                parameterWithName("size").description("??? ????????? ??? ????????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("totalPages").description("??? ????????? ??????"),
                                fieldWithPath("number").description("?????? ????????? ??????"),
                                fieldWithPath("previous").description("?????? ????????? ?????? ??????"),
                                fieldWithPath("next").description("?????? ????????? ?????? ??????"),
                                fieldWithPath("content[].productNo").description("?????? ??????"),
                                fieldWithPath("content[].title").description("?????? ??????"),
                                fieldWithPath("content[].productStock").description("?????? ????????????"),
                                fieldWithPath("content[].salesPrice").description("?????? ?????????"),
                                fieldWithPath("content[].saleRate").description("?????? ?????????"),
                                fieldWithPath("content[].productPrice").description("?????? ??????"),
                                fieldWithPath("content[].productSubscribed").description("?????? ?????? ??????"),
                                fieldWithPath("content[].deleted").description("????????? ?????? ??????"),
                                fieldWithPath("content[].thumbnail").description("?????? ?????????")
                        )));

        verify(productService, times(1)).getProductListLikeTitle("test", pageable);
    }

    @Test
    @DisplayName("?????? ?????? ??????")
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
                                parameterWithName("productNo").description("????????? ?????? ??????")
                        ),
                        requestFields(
                                fieldWithPath("productIsbn").description("????????? ISBN"),
                                fieldWithPath("title").description("?????? ??????"),
                                fieldWithPath("productPublisher").description("?????? ?????????"),
                                fieldWithPath("pageCount").description("????????? ??? ????????? ???"),
                                fieldWithPath("productDescription").description("?????? ??????"),
                                fieldWithPath("salePrice").description("?????? ?????????"),
                                fieldWithPath("productPrice").description("?????? ??????"),
                                fieldWithPath("salesRate").description("????????? ?????????"),
                                fieldWithPath("productPriority").description("?????? ?????? ????????????(????????? ???????????? ???????????? ??????)"),
                                fieldWithPath("productStock").description("????????? ????????????"),
                                fieldWithPath("publishedAt").description("?????? ?????? ??????"),
                                fieldWithPath("subscribed").description("?????? ?????? ?????? ??????"),
                                fieldWithPath("productPolicyNo").description("?????? ????????? ?????? ??????"),
                                fieldWithPath("saleCodeNo").description("?????????????????? ?????? ??????)"),
                                fieldWithPath("typeCodeNo").description("?????? ?????? ?????? ??????"),
                                fieldWithPath("authorsNo[]").description("??????(??????) ?????? ?????????"),
                                fieldWithPath("categoriesNo[]").description("?????? ???????????? ?????????"),
                                fieldWithPath("tagsNo[]").description("?????? ?????? ?????????")
                        )));

        verify(productService, times(1))
                .modifyProduct(any(), anyLong());
    }

    @Test
    @DisplayName("?????? ?????? ?????? ?????? ??????")
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
                                parameterWithName("productNo").description("????????? ?????? ??????(soft delete)")
                        ),
                        requestParameters(
                                parameterWithName("deleted").description("?????? ?????????")
                        )));

        verify(productService, times(1))
                .setDeleteProduct(anyLong());
    }

    @Test
    @DisplayName("?????? ?????? ????????? ????????? ?????? ????????? ??????")
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
                                parameterWithName("typeNo").description("????????? ?????? ?????? ????????????")
                        ),
                        requestParameters(
                                parameterWithName("limit").description("????????? ?????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("[].productNo").description("?????? ??????"),
                                fieldWithPath("[].title").description("?????? ??????"),
                                fieldWithPath("[].salesPrice").description("?????? ?????????"),
                                fieldWithPath("[].productPrice").description("?????? ??????"),
                                fieldWithPath("[].salesRate").description("????????? ?????????"),
                                fieldWithPath("[].productCategories[]").description("?????? ???????????? ?????????"),
                                fieldWithPath("[].thumbnail").description("?????? ?????????")
                        )));
    }

    @Test
    @DisplayName("????????? ?????? ????????? ????????? ????????? ?????? ?????? ?????????")
    void getProductsInCart() throws Exception {
        // given
        GetProductDetailResponseDto dto = new GetProductDetailResponseDto();
        ReflectionTestUtils.setField(dto, "productNo", 1L);
        ReflectionTestUtils.setField(dto, "title", "??????");
        ReflectionTestUtils.setField(dto, "productPublisher", "??????");
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
                                parameterWithName("productNo").description("????????? ?????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("[].productNo").description("?????? ??????"),
                                fieldWithPath("[].productIsbn").description("????????? ISBN"),
                                fieldWithPath("[].title").description("?????? ??????"),
                                fieldWithPath("[].productPublisher").description("?????? ?????????"),
                                fieldWithPath("[].pageCount").description("????????? ??? ????????? ???"),
                                fieldWithPath("[].productDescription").description("?????? ??????"),
                                fieldWithPath("[].salesPrice").description("?????? ?????????"),
                                fieldWithPath("[].productPrice").description("?????? ??????"),
                                fieldWithPath("[].salesRate").description("????????? ?????????"),
                                fieldWithPath("[].productPriority").description("?????? ?????? ????????????(????????? ???????????? ???????????? ??????)"),
                                fieldWithPath("[].productStock").description("????????? ????????????"),
                                fieldWithPath("[].publishDate").description("?????? ?????? ??????"),
                                fieldWithPath("[].deleted").description("?????? ?????? ??????"),
                                fieldWithPath("[].productSubscribed").description("?????? ?????? ?????? ??????"),
                                fieldWithPath("[].saleStateCodeCategory").description("?????? ???????????? ??????(?????????, ?????? ???)"),
                                fieldWithPath("[].typeStateCodeName").description("?????? ?????? ?????? ??????(??????, ??????????????? ???))"),
                                fieldWithPath("[].policyMethod").description("?????? ????????? ?????? ?????? ??????(????????????, ????????? ???)"),
                                fieldWithPath("[].policySaved").description("????????? ????????? ?????? ??????"),
                                fieldWithPath("[].policySaveRate").description("?????? ????????? ?????????"),
                                fieldWithPath("[].authors[]").description("??????(??????) ?????? ?????????"),
                                fieldWithPath("[].categories[]").description("?????? ???????????? ?????? ?????????"),
                                fieldWithPath("[].tags[]").description("?????? ?????? ?????????"),
                                fieldWithPath("[].tagsColors[]").description("?????? ??? ?????????"),
                                fieldWithPath("[].categoriesNo[]").description("?????? ???????????? ?????? ?????????"),
                                fieldWithPath("[].thumbnail").description("?????? ?????????"),
                                fieldWithPath("[].detail").description("?????? ???????????????"),
                                fieldWithPath("[].ebook").description("?????? ??????"),
                                fieldWithPath("[].info").description("?????? ??????")
                        )));
    }

    @Test
    @DisplayName("??????????????? ?????? ?????? API ?????????")
    void getProductsByCategory() throws Exception {
        // given
        GetProductByCategoryResponseDto dto = new GetProductByCategoryResponseDto();
        ReflectionTestUtils.setField(dto, "productNo", 1L);
        ReflectionTestUtils.setField(dto, "title", "??????");
        ReflectionTestUtils.setField(dto, "salesPrice", 1000L);
        ReflectionTestUtils.setField(dto, "salesRate", 10);
        ReflectionTestUtils.setField(dto, "categories", List.of("????????????"));
        ReflectionTestUtils.setField(dto, "authors", List.of("?????? 1"));

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
                                parameterWithName("page").description("????????? ????????? ??????"),
                                parameterWithName("size").description("????????? ?????????")
                        ),

                        pathParameters(
                                parameterWithName("categoryNo").description("???????????? ??????")
                        ),

                        responseFields(
                                fieldWithPath("totalPages").description("??? ????????? ??????"),
                                fieldWithPath("number").description("?????? ????????? ??????"),
                                fieldWithPath("previous").description("?????? ????????? ?????? ??????"),
                                fieldWithPath("next").description("?????? ????????? ?????? ??????"),
                                fieldWithPath("content[].productNo").description("?????? ??????"),
                                fieldWithPath("content[].title").description("?????? ??????"),
                                fieldWithPath("content[].salesPrice").description("????????????"),
                                fieldWithPath("content[].salesRate").description("?????????"),
                                fieldWithPath("content[].title").description("?????? ??????"),
                                fieldWithPath("content[].categories").description("????????????"),
                                fieldWithPath("content[].authors").description("??????"),
                                fieldWithPath("content[].thumbnail").description("?????? ?????????"),
                                fieldWithPath("content[].ebook").description("?????? ??????")
                        )
                ));


        verify(productService, times(1)).getProductsByCategory(4, pageable);
    }

    @Test
    @DisplayName("Ebook ?????? ?????????")
    void getEbook() throws Exception {
        // given
        GetProductByCategoryResponseDto dto = new GetProductByCategoryResponseDto();
        ReflectionTestUtils.setField(dto, "productNo", 1L);
        ReflectionTestUtils.setField(dto, "title", "??????");
        ReflectionTestUtils.setField(dto, "thumbnail", "thumbnail");
        ReflectionTestUtils.setField(dto, "salesPrice", 1000L);
        ReflectionTestUtils.setField(dto, "salesRate", 10);
        ReflectionTestUtils.setField(dto, "categories", List.of("????????????"));
        ReflectionTestUtils.setField(dto, "authors", List.of("?????? 1"));

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
                                parameterWithName("page").description("????????? ????????? ??????"),
                                parameterWithName("size").description("????????? ?????????")
                        ),

                        responseFields(
                                fieldWithPath("totalPages").description("??? ????????? ??????"),
                                fieldWithPath("number").description("?????? ????????? ??????"),
                                fieldWithPath("previous").description("?????? ????????? ?????? ??????"),
                                fieldWithPath("next").description("?????? ????????? ?????? ??????"),
                                fieldWithPath("content[].productNo").description("?????? ??????"),
                                fieldWithPath("content[].title").description("?????? ??????"),
                                fieldWithPath("content[].thumbnail").description("?????? ?????????"),
                                fieldWithPath("content[].salesPrice").description("????????????"),
                                fieldWithPath("content[].salesRate").description("?????????"),
                                fieldWithPath("content[].title").description("?????? ??????"),
                                fieldWithPath("content[].categories").description("????????????"),
                                fieldWithPath("content[].authors").description("??????"),
                                fieldWithPath("content[].thumbnail").description("?????? ?????????"),
                                fieldWithPath("content[].ebook").description("?????? ??????")
                        )
                ));


        verify(productService, times(1)).getEbooks(pageable);
    }

    @Test
    @DisplayName("?????? ?????? ?????? api ?????????")
    void modifyProductInfo_success_test() throws Exception {
        // given
        ModifyProductInfoRequestDto dto = new ModifyProductInfoRequestDto();
        ReflectionTestUtils.setField(dto, "productIsbn", "12345678910");
        ReflectionTestUtils.setField(dto, "productPrice", 1000L);
        ReflectionTestUtils.setField(dto, "salesRate", 10);
        ReflectionTestUtils.setField(dto, "productPublisher", "?????????");
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
                                parameterWithName("productNo").description("????????????")
                        ),
                        requestFields(
                                fieldWithPath("productIsbn").description("?????? Isbn"),
                                fieldWithPath("productPrice").description("??????"),
                                fieldWithPath("salesRate").description("?????????"),
                                fieldWithPath("productPublisher").description("?????????"),
                                fieldWithPath("publishedAt").description("?????????"),
                                fieldWithPath("pageCount").description("????????? ???"),
                                fieldWithPath("salesPrice").description("????????????"),
                                fieldWithPath("priority").description("????????????")
                        )));

        verify(productService, times(1))
                .modifyProductInfo(anyLong(), any(ModifyProductInfoRequestDto.class));
    }

    @Test
    @DisplayName("?????? ?????? ?????? ?????? ????????? (isbn ?????? ??????)")
    void modifyProductInfo_fail_over_isbn_Test() throws Exception {
        // given
        ModifyProductInfoRequestDto dto = new ModifyProductInfoRequestDto();
        ReflectionTestUtils.setField(dto, "productIsbn", "10?????? ?????? 13?????? ????????? isbn");
        ReflectionTestUtils.setField(dto, "productPrice", 1000L);
        ReflectionTestUtils.setField(dto, "salesRate", 10);
        ReflectionTestUtils.setField(dto, "productPublisher", "?????????");
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
                                parameterWithName("productNo").description("?????? ??????")
                        ),
                        requestFields(
                                fieldWithPath("productIsbn").description("?????? Isbn"),
                                fieldWithPath("productPrice").description("??????"),
                                fieldWithPath("salesRate").description("?????????"),
                                fieldWithPath("productPublisher").description("?????????"),
                                fieldWithPath("publishedAt").description("?????????"),
                                fieldWithPath("pageCount").description("????????? ???"),
                                fieldWithPath("salesPrice").description("????????????"),
                                fieldWithPath("priority").description("????????????")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("ISBN??? 10??? ?????? 13????????????.")
                        )
                ));
    }

    @Test
    @DisplayName("?????? ?????? ?????? ?????? ????????? (isbn ??????)")
    void modifyProductInfo_fail_test_null_isbn() throws Exception {
        // given
        ModifyProductInfoRequestDto dto = new ModifyProductInfoRequestDto();
        ReflectionTestUtils.setField(dto, "productIsbn", null);
        ReflectionTestUtils.setField(dto, "productPrice", 1000L);
        ReflectionTestUtils.setField(dto, "salesRate", 10);
        ReflectionTestUtils.setField(dto, "productPublisher", "?????????");
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
                                parameterWithName("productNo").description("?????? ??????")
                        ),
                        requestFields(
                                fieldWithPath("productIsbn").description("?????? Isbn"),
                                fieldWithPath("productPrice").description("??????"),
                                fieldWithPath("salesRate").description("?????????"),
                                fieldWithPath("productPublisher").description("?????????"),
                                fieldWithPath("publishedAt").description("?????????"),
                                fieldWithPath("pageCount").description("????????? ???"),
                                fieldWithPath("salesPrice").description("????????????"),
                                fieldWithPath("priority").description("????????????")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("ISBN??? ???????????????.")
                        )
                ));
    }

    @Test
    @DisplayName("?????? ?????? ?????? ?????? ????????? (null price)")
    void modifyProductInfo_fail_test_null_price() throws Exception {
        // given
        ModifyProductInfoRequestDto dto = new ModifyProductInfoRequestDto();
        ReflectionTestUtils.setField(dto, "productIsbn", "1234561232");
        ReflectionTestUtils.setField(dto, "productPrice", null);
        ReflectionTestUtils.setField(dto, "salesRate", 10);
        ReflectionTestUtils.setField(dto, "productPublisher", "?????????");
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
                                parameterWithName("productNo").description("?????? ??????")
                        ),
                        requestFields(
                                fieldWithPath("productIsbn").description("?????? Isbn"),
                                fieldWithPath("productPrice").description("??????"),
                                fieldWithPath("salesRate").description("?????????"),
                                fieldWithPath("productPublisher").description("?????????"),
                                fieldWithPath("publishedAt").description("?????????"),
                                fieldWithPath("pageCount").description("????????? ???"),
                                fieldWithPath("salesPrice").description("????????????"),
                                fieldWithPath("priority").description("????????????")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("????????? ????????? ???????????????.")
                        )
                ));
    }

    @Test
    @DisplayName("?????? ?????? ?????? ?????? ????????? (null productPublisher)")
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
                                parameterWithName("productNo").description("?????? ??????")
                        ),
                        requestFields(
                                fieldWithPath("productIsbn").description("?????? Isbn"),
                                fieldWithPath("productPrice").description("??????"),
                                fieldWithPath("salesRate").description("?????????"),
                                fieldWithPath("productPublisher").description("?????????"),
                                fieldWithPath("publishedAt").description("?????????"),
                                fieldWithPath("pageCount").description("????????? ???"),
                                fieldWithPath("salesPrice").description("????????????"),
                                fieldWithPath("priority").description("????????????")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("????????? ??? ????????? ???????????????.")
                        )
                ));
    }

    @Test
    @DisplayName("?????? ?????? ?????? ?????? ????????? (over productPublisher)")
    void modifyProductInfo_fail_test_over_productPublisher() throws Exception {
        // given
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            sb.append("?????????");
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
                                parameterWithName("productNo").description("?????? ??????")
                        ),
                        requestFields(
                                fieldWithPath("productIsbn").description("?????? Isbn"),
                                fieldWithPath("productPrice").description("??????"),
                                fieldWithPath("salesRate").description("?????????"),
                                fieldWithPath("productPublisher").description("?????????"),
                                fieldWithPath("publishedAt").description("?????????"),
                                fieldWithPath("pageCount").description("????????? ???"),
                                fieldWithPath("salesPrice").description("????????????"),
                                fieldWithPath("priority").description("????????????")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("50?????? ?????? ??? ????????????.")
                        )
                ));
    }

    @Test
    @DisplayName("?????? ?????? ?????? ?????? ????????? (null salesPrice)")
    void modifyProductInfo_fail_test_null_salesPrice() throws Exception {
        // given
        ModifyProductInfoRequestDto dto = new ModifyProductInfoRequestDto();
        ReflectionTestUtils.setField(dto, "productIsbn", "1234561232");
        ReflectionTestUtils.setField(dto, "productPrice", 100L);
        ReflectionTestUtils.setField(dto, "salesRate", 10);
        ReflectionTestUtils.setField(dto, "productPublisher", "?????????");
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
                                parameterWithName("productNo").description("?????? ??????")
                        ),
                        requestFields(
                                fieldWithPath("productIsbn").description("?????? Isbn"),
                                fieldWithPath("productPrice").description("??????"),
                                fieldWithPath("salesRate").description("?????????"),
                                fieldWithPath("productPublisher").description("?????????"),
                                fieldWithPath("publishedAt").description("?????????"),
                                fieldWithPath("pageCount").description("????????? ???"),
                                fieldWithPath("salesPrice").description("????????????"),
                                fieldWithPath("priority").description("????????????")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("???????????? ???????????????.")
                        )
                ));
    }

    @Test
    @DisplayName("?????? ???????????? ?????? api ?????? ?????????")
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
                                parameterWithName("productNo").description("?????? ??????")
                        ),
                        requestFields(
                                fieldWithPath("categoriesNo").description("???????????? ??????")
                        )
                ));

        verify(productService, times(1))
                .modifyProductCategory(anyLong(), any(ModifyProductCategoryRequestDto.class));
    }

    @Test
    @DisplayName("?????? ???????????? ?????? ?????? ????????? (null category No) ")
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
                                parameterWithName("productNo").description("?????? ??????")
                        ),
                        requestFields(
                                fieldWithPath("categoriesNo").description("???????????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("???????????? ????????? ???????????????.")
                        )
                ));
    }

    @Test
    @DisplayName("?????? ?????? ?????? API ?????? ?????????")
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
                                parameterWithName("productNo").description("?????? ??????")
                        ),
                        requestFields(
                                fieldWithPath("authors").description("?????? ??????")
                        )
                ));

        verify(productService, times(1))
                .modifyProductAuthor(anyLong(), any(ModifyProductAuthorRequestDto.class));
    }

    @Test
    @DisplayName("?????? ?????? ?????? ?????? ????????? (null authorsNo)")
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
                                parameterWithName("productNo").description("?????? ??????")
                        ),
                        requestFields(
                                fieldWithPath("authors").description("?????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("?????? ????????? ???????????????.")
                        )
                ));
    }

    @Test
    @DisplayName("?????? ?????? ?????? API ?????? ?????????")
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
                                parameterWithName("productNo").description("?????? ??????")
                        ),
                        requestFields(
                                fieldWithPath("tags").description("?????? ??????")
                        )
                ));

        verify(productService, times(1))
                .modifyProductTag(anyLong(), any(ModifyProductTagRequestDto.class));
    }

    @Test
    @DisplayName("?????? ?????? ?????? ?????? ????????? (null tagNo)")
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
                                parameterWithName("productNo").description("?????? ??????")
                        ),
                        requestFields(
                                fieldWithPath("tags").description("?????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("?????? ????????? ???????????????.")
                        )
                ));
    }

    @Test
    @DisplayName("?????? ?????? ?????? API ?????? ?????????")
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
                                parameterWithName("productNo").description("?????? ??????")
                        ),
                        requestParameters(
                                parameterWithName("no").description("?????? ?????? ??????")
                        )
                ));

        verify(productService, times(1))
                .modifyProductType(anyLong(), anyInt());
    }

    @Test
    @DisplayName("?????? ?????? ?????? ?????? API ?????? ?????????")
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
                                parameterWithName("productNo").description("?????? ??????")
                        ),
                        requestParameters(
                                parameterWithName("no").description("?????? ?????? ?????? ??????")
                        )
                ));

        verify(productService, times(1))
                .modifyProductSale(anyLong(), anyInt());
    }

    @Test
    @DisplayName("?????? ????????? ?????? ?????? API ?????? ?????????")
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
                                parameterWithName("productNo").description("?????? ??????")
                        ),
                        requestParameters(
                                parameterWithName("no").description("?????? ????????? ?????? ??????")
                        )
                ));

        verify(productService, times(1))
                .modifyProductPolicy(anyLong(), anyInt());
    }

    @Test
    @DisplayName("E-Book ?????? ?????? ?????? API ?????????")
    void getEBookInfo_success_test() throws Exception {
        // given
        GetDownloadInfo info = new GetDownloadInfo("object storage path", "token", "?????? ??????", "?????? ?????????", "?????? ?????????");

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
                                parameterWithName("productNo").description("?????? ??????"),
                                parameterWithName("memberNo").description("?????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("path").description("???????????? ???????????? ??????"),
                                fieldWithPath("token").description("???????????? ???????????? ?????? ??????"),
                                fieldWithPath("nameOrigin").description("?????? ??????"),
                                fieldWithPath("nameSaved").description("?????? ?????????"),
                                fieldWithPath("fileExtension").description("?????? ?????????")
                        )
                ));

        verify(productService, times(1))
                .getEbookInfo(anyLong());
    }

    @Test
    @DisplayName("?????? ?????? ?????? ?????? API ?????????")
    void modifyProductDescription_success_test() throws Exception {
        // given
        ModifyProductDescriptionRequestDto dto = new ModifyProductDescriptionRequestDto();
        ReflectionTestUtils.setField(dto, "description", "# ??????");

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
                                parameterWithName("productNo").description("?????? ??????")
                        ),
                        requestFields(
                                fieldWithPath("description").description("?????? ??????")
                        )
                ));

        verify(productService, times(1))
                .modifyProductDescription(anyLong(), any(ModifyProductDescriptionRequestDto.class));
    }

    @Test
    @DisplayName("?????? ?????? ?????? ?????? ????????? (over description)")
    void modifyProductDescription_fail_test_over_description() throws Exception {
        // given
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            sb.append("????????? ?????? ??????");
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
                                parameterWithName("productNo").description("?????? ??????")
                        ),
                        requestFields(
                                fieldWithPath("description").description("?????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("5000?????? ?????? ??? ????????????.")
                        )
                ));
    }

    @Test
    @DisplayName("?????? ?????? ?????? ?????? ????????? (null description)")
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
                                parameterWithName("productNo").description("?????? ??????")
                        ),
                        requestFields(
                                fieldWithPath("description").description("?????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("????????? ??????????????? ????????????.")
                        )
                ));
    }

    @Test
    @DisplayName("E-Book ?????? API ?????????")
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
                                partWithName("ebook").description("E-Book ??????")
                        )
                ));
    }

    @Test
    @DisplayName("????????? ?????? ?????? API ?????????")
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
                                partWithName("image").description("????????? ????????? ??????")
                        )
                ));
    }

    @Test
    @DisplayName("?????? ??????????????? ?????? ?????? API ?????????")
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
                                partWithName("detailImage").description("?????? ????????? ??????")
                        )
                ));
    }


    @Test
    @DisplayName("?????? ????????? ????????? ?????? ?????? API ?????????")
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
                                partWithName("image").description("????????? ????????? ??????")
                        )
                ));
    }

    @Test
    @DisplayName("?????? ?????? ????????? ?????? ?????? API ?????????")
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
                                partWithName("detailImage").description("?????? ????????? ??????")
                        )
                ));
    }

}
