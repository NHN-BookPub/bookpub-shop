package com.nhnacademy.bookpubshop.product.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import java.util.ArrayList;
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
        doNothing().when(productService).createProduct(requestDto);

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
                .setDeleteProduct(product.getProductNo());

        mockMvc.perform(put(url + "/deleted/{id}", 1L)
                        .param("deleted", "false")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());

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
        mockMvc.perform(get(url + "/types/{typeNo}", 5)
                        .param("limit", "5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$[0].salesPrice").value(9000))
                .andExpect(jsonPath("$[0].productPrice").value(10000))
                .andExpect(jsonPath("$[0].salesRate").value(10))
                .andDo(print());
    }

}