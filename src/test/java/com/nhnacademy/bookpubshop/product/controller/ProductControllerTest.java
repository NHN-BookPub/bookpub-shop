package com.nhnacademy.bookpubshop.product.controller;

import static com.nhnacademy.bookpubshop.state.ProductTypeState.BEST_SELLER;
import static com.nhnacademy.bookpubshop.state.ProductTypeState.NEW;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nhnacademy.bookpubshop.error.ShopAdviceController;
import com.nhnacademy.bookpubshop.product.dto.CreateProductRequestDto;
import com.nhnacademy.bookpubshop.product.dto.GetProductDetailResponseDto;
import com.nhnacademy.bookpubshop.product.dto.GetProductListResponseDto;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import com.nhnacademy.bookpubshop.product.service.ProductService;
import java.time.LocalDateTime;
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
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.http.MediaType;
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
    String url;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        url = "/api/product";

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
        when(productService.getProductDetailById(product.getProductNo()))
                .thenReturn(responseDto);

        mockMvc.perform(get(url + "/" + product.getProductNo())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(responseDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productNo").value(product.getProductNo()))
                .andExpect(jsonPath("$.productIsbn").value(product.getProductIsbn()))
                .andExpect(jsonPath("$.productPriority").value(product.getProductPriority()))
                .andExpect(jsonPath("$.productThumbnail").value(product.getProductThumbnail()))
                .andExpect(jsonPath("$.title").value(product.getTitle()))
                .andExpect(jsonPath("$.salesPrice").value(product.getSalesPrice()))
                .andExpect(jsonPath("$.pageCount").value(product.getPageCount()))
                .andDo(print());

        verify(productService, times(1))
                .getProductDetailById(product.getProductNo());
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
        when(productService.modifyProduct(requestDto, product.getProductNo()))
                .thenReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.put(url + "/" + product.getProductNo())
                        .content(mapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());
    }

    @Test
    @DisplayName("상품 삭제 여부 설정 성공")
    void setDeletedProduct() throws Exception {
        doNothing().when(productService).setDeleteProduct(product.getProductNo(), false);

        mockMvc.perform(MockMvcRequestBuilders.post(url + "/" + product.getProductNo() + "?deleted=false")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());

        then(productService).should().setDeleteProduct(product.getProductNo(), false);
    }
}