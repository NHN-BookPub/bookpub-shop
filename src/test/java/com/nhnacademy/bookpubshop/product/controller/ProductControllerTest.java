package com.nhnacademy.bookpubshop.product.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nhnacademy.bookpubshop.error.ShopAdviceController;
import com.nhnacademy.bookpubshop.product.dto.CreateProductRequestDto;
import com.nhnacademy.bookpubshop.product.dto.GetProductDetailResponseDto;
import com.nhnacademy.bookpubshop.product.dto.GetProductListResponseDto;
import com.nhnacademy.bookpubshop.product.dummy.ProductDummy;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductPolicyDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductSaleStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductTypeStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import com.nhnacademy.bookpubshop.product.service.ProductService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * ProductController 테스트.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@WebMvcTest(ProductController.class)
@Import(ShopAdviceController.class)
@MockBean(JpaMetamodelMappingContext.class)
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

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper().registerModule(new JavaTimeModule());

        productPolicy = new ProductPolicy(1, "test", true, 10);
        typeStateCode = new ProductTypeStateCode(1, "test", true, "test");
        saleStateCode = new ProductSaleStateCode(1, "test", true, "test");
        product = ProductDummy.dummy(productPolicy, typeStateCode, saleStateCode);
        requestDto = new CreateProductRequestDto();
        responseDto = new GetProductDetailResponseDto(
                1L,
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

        List<Long> relation = new ArrayList<>();

        for (Product relationProduct : product.getRelationProduct()) {
            relation.add(relationProduct.getProductNo());
        }

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
        ReflectionTestUtils.setField(requestDto, "relationProducts", relation);
    }

    @Test
    @DisplayName("전체 상품 조회 성공")
    void getAllProductsSuccess() throws Exception {
        List<GetProductListResponseDto> responses = new ArrayList<>();
        responses.add(listResponseDto);

        Pageable pageable = Pageable.ofSize(5);
        Page<GetProductListResponseDto> page =
                PageableExecutionUtils.getPage(responses, pageable, () -> 1L);

        when(productService.getAllProducts(pageable))
                .thenReturn(page);

        mockMvc.perform(get(url + "?page=0&size=5")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(page)))
                .andExpect(status().isOk())
                .andDo(print());

        verify(productService, times(1)).getAllProducts(pageable);

        assertThat((long) productService.getAllProducts(pageable)
                .getContent().size())
                .isEqualTo(responses.size());
        assertThat(productService.getAllProducts(pageable).getContent().get(0).getProductNo())
                .isEqualTo(listResponseDto.getProductNo());
        assertThat(productService.getAllProducts(pageable).getContent().get(0).getPublishedAt())
                .isEqualTo(listResponseDto.getPublishedAt());
        assertThat(productService.getAllProducts(pageable).getContent().get(0).getThumbnailPath())
                .isEqualTo(listResponseDto.getThumbnailPath());
        assertThat(productService.getAllProducts(pageable).getContent().get(0).getSaleRate())
                .isEqualTo(listResponseDto.getSaleRate());
        assertThat(productService.getAllProducts(pageable).getContent().get(0).getTitle())
                .isEqualTo(listResponseDto.getTitle());
        assertThat(productService.getAllProducts(pageable).getContent().get(0).getProductStock())
                .isEqualTo(listResponseDto.getProductStock());
        assertThat(productService.getAllProducts(pageable).getContent().get(0).getSalesPrice())
                .isEqualTo(listResponseDto.getSalesPrice());
        assertThat(productService.getAllProducts(pageable).getContent().get(0).isDeleted())
                .isEqualTo(listResponseDto.isDeleted());
    }

    @Test
    @DisplayName("상품 생성 성공")
    void createProduct() throws Exception {
        when(productService.createProduct(requestDto))
                .thenReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.post(url)
                        .content(mapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());

        then(productService).should().createProduct(any(CreateProductRequestDto.class));
    }

    @Test
    @DisplayName("상품 상세 조회 성공")
    void getProductDetailById() throws Exception {
        when(productService.getProductDetailById(anyLong()))
                .thenReturn(responseDto);

        mockMvc.perform(get("/api/products/{productNo}", anyLong())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productNo").value(responseDto.getProductNo()))
                .andExpect(jsonPath("$.productIsbn").value(responseDto.getProductIsbn()))
                .andExpect(jsonPath("$.title").value(responseDto.getTitle()))
                .andExpect(jsonPath("$.pageCount").value(responseDto.getPageCount()))
                .andExpect(jsonPath("$.productDescription").value(responseDto.getProductDescription()))
                .andExpect(jsonPath("$.productThumbnail").value(responseDto.getProductThumbnail()))
                .andExpect(jsonPath("$.salesPrice").value(responseDto.getSalesPrice()))
                .andExpect(jsonPath("$.salesRate").value(responseDto.getSalesRate()))
                .andExpect(jsonPath("$.productPriority").value(responseDto.getProductPriority()))
                .andExpect(jsonPath("$.productStock").value(responseDto.getProductStock()))
                .andExpect(jsonPath("$.deleted").value(responseDto.isDeleted()))
                .andExpect(jsonPath("$.productSubscribed").value(responseDto.isProductSubscribed()))
                .andDo(print());

    }

    @Test
    @DisplayName("상품 제목 검색 성공")
    void getProductLikeTitle() throws Exception {
        List<GetProductListResponseDto> responses = new ArrayList<>();
        responses.add(listResponseDto);

        Pageable pageable = Pageable.ofSize(5);
        Page<GetProductListResponseDto> page =
                PageableExecutionUtils.getPage(responses, pageable, () -> 1L);

        when(productService.getProductListLikeTitle("test", pageable))
                .thenReturn(page);

        mockMvc.perform(get(url + "/search?title=test&page=0&size=5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(page)))
                .andExpect(status().isOk())
                .andDo(print());

        verify(productService, times(1)).getProductListLikeTitle("test", pageable);

        assertThat((long) productService.getProductListLikeTitle("test", pageable)
                .getContent().size())
                .isEqualTo(responses.size());
    }

    @Test
    @DisplayName("상품 수정 성공")
    void modifyProduct() throws Exception {
        doNothing()
                .when(productService)
                        .modifyProduct(requestDto, 1L);

        mockMvc.perform(put(url + "/{productNo}", 1L)
                        .content(mapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());

        verify(productService, times(1))
                .modifyProduct(any(), anyLong());
    }

    @Test
    @DisplayName("상품 삭제 여부 설정 성공")
    void setDeletedProduct() throws Exception {
        doNothing().when(productService)
                .setDeleteProduct(product.getProductNo(), false);

        mockMvc.perform(delete(url+"/{id}", 1L)
                        .param("deleted", "false")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());

        verify(productService, times(1))
                .setDeleteProduct(anyLong(), anyBoolean());
    }
}