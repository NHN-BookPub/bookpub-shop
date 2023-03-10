package com.nhnacademy.bookpubshop.category.controller;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookpubshop.category.dto.request.CreateCategoryRequestDto;
import com.nhnacademy.bookpubshop.category.dto.request.ModifyCategoryRequestDto;
import com.nhnacademy.bookpubshop.category.dto.response.GetCategoryResponseDto;
import com.nhnacademy.bookpubshop.category.dto.response.GetChildCategoryResponseDto;
import com.nhnacademy.bookpubshop.category.dto.response.GetParentCategoryWithChildrenResponseDto;
import com.nhnacademy.bookpubshop.category.service.CategoryService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

/**
 * CategoryRestController ????????? ?????????.
 *
 * @author : ?????????
 * @since : 1.0
 **/
@WebMvcTest(CategoryController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
class CategoryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CategoryService categoryService;

    ObjectMapper objectMapper;

    CreateCategoryRequestDto createCategoryRequestDto;

    ModifyCategoryRequestDto modifyCategoryRequestDto;

    String path = "/api/categories";
    String authPath = "/token/categories";

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        createCategoryRequestDto = new CreateCategoryRequestDto();
        modifyCategoryRequestDto = new ModifyCategoryRequestDto();
    }

    @Test
    @DisplayName("???????????? ?????? Valid Exception ?????? ?????? ??????_???????????? ?????? ??????")
    void addCategoryFailTest_categoryNameIsBlank() throws Exception {
        ReflectionTestUtils.setField(createCategoryRequestDto, "categoryName", "");
        ReflectionTestUtils.setField(createCategoryRequestDto, "categoryPriority", 0);
        ReflectionTestUtils.setField(createCategoryRequestDto, "categoryDisplayed", true);
        doNothing().when(categoryService).addCategory(createCategoryRequestDto);

        mockMvc.perform(post(authPath)
                        .content(objectMapper.writeValueAsString(createCategoryRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andDo(document("category-create-categoryNameFail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].message").description("????????? ?????????????????? ??????????????????.")
                        )));

        verify(categoryService, times(0)).addCategory(createCategoryRequestDto);
    }

    @Test
    @DisplayName("???????????? ?????? Valid Exception ?????? ?????? ??????_???????????? ?????? ?????? ??????")
    void addCategoryFailTest_categoryNameTooLong() throws Exception {
        ReflectionTestUtils.setField(createCategoryRequestDto, "categoryName", "asdfasdfasdfasdf");
        ReflectionTestUtils.setField(createCategoryRequestDto, "categoryPriority", 0);
        ReflectionTestUtils.setField(createCategoryRequestDto, "categoryDisplayed", true);
        doNothing().when(categoryService).addCategory(createCategoryRequestDto);

        mockMvc.perform(post(authPath)
                        .content(objectMapper.writeValueAsString(createCategoryRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andDo(document("category-create-categoryNameFail-tooLong",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].message").description("?????????????????? ????????? ??????????????????.")
                        )));

        verify(categoryService, times(0)).addCategory(createCategoryRequestDto);
    }


    @Test
    @DisplayName("???????????? ?????? ?????? ?????????")
    void addCategorySuccessTest() throws Exception {
        ReflectionTestUtils.setField(createCategoryRequestDto, "categoryName", "????????????");
        ReflectionTestUtils.setField(createCategoryRequestDto, "categoryPriority", 0);
        ReflectionTestUtils.setField(createCategoryRequestDto, "categoryDisplayed", true);
        ArgumentCaptor<CreateCategoryRequestDto> captor = ArgumentCaptor.forClass(
                CreateCategoryRequestDto.class);
        doNothing().when(categoryService).addCategory(createCategoryRequestDto);

        mockMvc.perform(post(authPath)
                        .content(objectMapper.writeValueAsString(createCategoryRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("category-create",
                        preprocessRequest(prettyPrint()),
                        requestFields(
                                fieldWithPath("categoryName").type(JsonFieldType.STRING).description("???????????? ??????"),
                                fieldWithPath("parentCategoryNo").type(JsonFieldType.NUMBER).description("?????? ???????????? ??????").optional(),
                                fieldWithPath("categoryPriority").type(JsonFieldType.NUMBER).description("???????????? ????????????(????????? ????????? ??????????????? ??????)").optional(),
                                fieldWithPath("categoryDisplayed").type(JsonFieldType.BOOLEAN).description("???????????? ????????????").optional()
                        )));

        verify(categoryService, times(1)).addCategory(captor.capture());

        CreateCategoryRequestDto result = captor.getValue();
        assertThat(result.getCategoryName()).isEqualTo(createCategoryRequestDto.getCategoryName());
        assertThat(result.getCategoryPriority()).isEqualTo(
                createCategoryRequestDto.getCategoryPriority());
        assertThat(result.isCategoryDisplayed()).isTrue();
    }

    @Test
    @DisplayName("???????????? ?????? Valid Exception ?????? ?????? ??????_???????????? ?????? ??????")
    void modifyCategoryFailTest_categoryNoIsNull() throws Exception {
        ReflectionTestUtils.setField(modifyCategoryRequestDto, "categoryNo", null);
        ReflectionTestUtils.setField(modifyCategoryRequestDto, "categoryName", "????????????");
        ReflectionTestUtils.setField(modifyCategoryRequestDto, "categoryPriority", 0);
        ReflectionTestUtils.setField(modifyCategoryRequestDto, "categoryDisplayed", true);
        doNothing().when(categoryService).modifyCategory(modifyCategoryRequestDto);

        mockMvc.perform(put(authPath)
                        .content(objectMapper.writeValueAsString(modifyCategoryRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andDo(document("category-modify-categoryNoFail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].message").description("????????? ???????????? ????????? ??????????????????.")
                        )));

        verify(categoryService, times(0)).modifyCategory(modifyCategoryRequestDto);
    }

    @Test
    @DisplayName("???????????? ?????? Valid Exception ?????? ?????? ??????_???????????? ?????? ??????")
    void modifyCategoryFailTest_categoryNameIsNull() throws Exception {
        ReflectionTestUtils.setField(modifyCategoryRequestDto, "categoryNo", 1);
        ReflectionTestUtils.setField(modifyCategoryRequestDto, "categoryName", "");
        ReflectionTestUtils.setField(modifyCategoryRequestDto, "categoryPriority", 0);
        ReflectionTestUtils.setField(modifyCategoryRequestDto, "categoryDisplayed", true);
        doNothing().when(categoryService).modifyCategory(modifyCategoryRequestDto);

        mockMvc.perform(put(authPath)
                        .content(objectMapper.writeValueAsString(modifyCategoryRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andDo(document("category-modify-categoryNameFail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].message").description("????????? ?????????????????? ??????????????????")
                        )));

        verify(categoryService, times(0)).modifyCategory(modifyCategoryRequestDto);
    }

    @Test
    @DisplayName("???????????? ?????? Valid Exception ?????? ?????? ??????_???????????? ?????? ?????? ??????")
    void modifyCategoryFailTest_categoryNoTooLong() throws Exception {
        ReflectionTestUtils.setField(modifyCategoryRequestDto, "categoryNo", null);
        ReflectionTestUtils.setField(modifyCategoryRequestDto, "categoryName", "asdfasdfasdfasdf");
        ReflectionTestUtils.setField(modifyCategoryRequestDto, "categoryPriority", 0);
        ReflectionTestUtils.setField(modifyCategoryRequestDto, "categoryDisplayed", true);
        doNothing().when(categoryService).modifyCategory(modifyCategoryRequestDto);

        mockMvc.perform(put(authPath)
                        .content(objectMapper.writeValueAsString(modifyCategoryRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andDo(document("category-modify-categoryNameFail-tooLong",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].message").description("?????????????????? ????????? ??????????????????.")
                        )));

    }

    @Test
    @DisplayName("???????????? ?????? ?????? ?????????.")
    void modifyCategorySuccessTest() throws Exception {

        ReflectionTestUtils.setField(modifyCategoryRequestDto, "categoryNo", 1);
        ReflectionTestUtils.setField(modifyCategoryRequestDto, "categoryName", "????????????");
        ReflectionTestUtils.setField(modifyCategoryRequestDto, "categoryPriority", 0);
        ReflectionTestUtils.setField(modifyCategoryRequestDto, "categoryDisplayed", true);

        ArgumentCaptor<ModifyCategoryRequestDto> captor = ArgumentCaptor.forClass(
                ModifyCategoryRequestDto.class);

        doNothing().when(categoryService).modifyCategory(modifyCategoryRequestDto);

        mockMvc.perform(put(authPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifyCategoryRequestDto)))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("category-modify",
                        preprocessRequest(prettyPrint()),
                        requestFields(
                                fieldWithPath("categoryNo").type(JsonFieldType.NUMBER).description("????????? ???????????? ??????"),
                                fieldWithPath("parentCategoryNo").type(JsonFieldType.NUMBER).description("?????? ???????????? ??????").optional(),
                                fieldWithPath("categoryName").type(JsonFieldType.STRING).description("???????????? ??????"),
                                fieldWithPath("categoryPriority").type(JsonFieldType.NUMBER).description("???????????? ????????????(????????? ????????? ??????????????? ??????)").optional(),
                                fieldWithPath("categoryDisplayed").type(JsonFieldType.BOOLEAN).description("???????????? ????????????").optional()
                        )));

        verify(categoryService, times(1)).modifyCategory(captor.capture());
        ModifyCategoryRequestDto result = captor.getValue();
        assertThat(result.getCategoryNo()).isEqualTo(modifyCategoryRequestDto.getCategoryNo());
        assertThat(result.getCategoryName()).isEqualTo(modifyCategoryRequestDto.getCategoryName());
        assertThat(result.getCategoryPriority()).isEqualTo(
                modifyCategoryRequestDto.getCategoryPriority());
        assertThat(result.isCategoryDisplayed()).isTrue();

    }

    @Test
    @DisplayName("??????????????? ?????? ????????? ?????? ?????????.")
    void getCategoryDetailsTest() throws Exception {
        GetCategoryResponseDto parentDto = new GetCategoryResponseDto(1, "??????", null, 6, true);
        GetCategoryResponseDto dto = new GetCategoryResponseDto(2, "????????????", parentDto, 5, true);

        when(categoryService.getCategory(anyInt())).thenReturn(dto);

        mockMvc.perform(RestDocumentationRequestBuilders.get(path + "/{categoryNo}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryNo").value(dto.getCategoryNo()))
                .andExpect(jsonPath("$.categoryName").value(dto.getCategoryName()))
                .andExpect(jsonPath("$.parent.categoryNo").value(dto.getParent().getCategoryNo()))
                .andExpect(jsonPath("$.parent.categoryName").value(dto.getParent().getCategoryName()))
                .andExpect(jsonPath("$.parent.parent").value(dto.getParent().getParent()))
                .andExpect(jsonPath("$.parent.categoryPriority").value(dto.getParent().getCategoryPriority()))
                .andExpect(jsonPath("$.parent.categoryDisplayed").value(dto.getParent().isCategoryDisplayed()))
                .andExpect(jsonPath("$.categoryPriority").value(dto.getCategoryPriority()))
                .andExpect(jsonPath("$.categoryDisplayed").value(dto.isCategoryDisplayed()))
                .andDo(print())
                .andDo(document("get-category",
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("categoryNo").description("????????? ???????????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("categoryNo").description("???????????? ??????"),
                                fieldWithPath("categoryName").description("???????????? ??????"),
                                fieldWithPath("parent.categoryNo").description("?????? ???????????? ??????"),
                                fieldWithPath("parent.categoryName").description("?????? ???????????? ??????"),
                                fieldWithPath("parent.parent").description("?????? ??????????????? ?????? ????????????(depth=2????????? null)"),
                                fieldWithPath("parent.categoryPriority").description("?????? ???????????? ????????????"),
                                fieldWithPath("parent.categoryDisplayed").description("?????? ???????????? ?????? ??????"),
                                fieldWithPath("categoryPriority").description("???????????? ????????????(????????? ????????? ??????????????? ??????)"),
                                fieldWithPath("categoryDisplayed").description("???????????? ?????? ??????")
                        )));

        verify(categoryService, times(1)).getCategory(anyInt());

    }


    @Test
    @DisplayName("???????????? ????????? ??????")
    void getCategoryListTest() throws Exception {
        GetCategoryResponseDto dto = new GetCategoryResponseDto(2, "????????????", null, 5, true);
        List<GetCategoryResponseDto> list = List.of(dto);

        Pageable pageable = PageRequest.of(0, 10);
        Page<GetCategoryResponseDto> page = PageableExecutionUtils.getPage(list, pageable,
                () -> 1L);

        when(categoryService.getCategories(pageable)).thenReturn(page);

        mockMvc.perform(get(path)
                        .param("page", objectMapper.writeValueAsString(pageable.getPageNumber()))
                        .param("size", objectMapper.writeValueAsString(pageable.getPageSize()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].categoryNo").value(dto.getCategoryNo()))
                .andExpect(jsonPath("$.content[0].categoryName").value(dto.getCategoryName()))
                .andExpect(jsonPath("$.content[0].parent").value(dto.getParent()))
                .andExpect(
                        jsonPath("$.content[0].categoryPriority").value(dto.getCategoryPriority()))
                .andExpect(
                        jsonPath("$.content[0].categoryDisplayed").value(dto.isCategoryDisplayed()))
                .andDo(print())
                .andDo(document("get-categories",
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("page").description("????????? ?????? ??????"),
                                parameterWithName("size").description("????????? ????????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("content[].categoryNo").description("???????????? ??????"),
                                fieldWithPath("content[].categoryName").description("???????????? ??????"),
                                fieldWithPath("content[].parent").description("????????? ????????????"),
                                fieldWithPath("content[].categoryPriority").description(
                                        "???????????? ????????????(????????? ???????????? ??????????????? ??????)"),
                                fieldWithPath("content[].categoryDisplayed").description(
                                        "???????????? ?????? ??????"),
                                fieldWithPath("totalPages").description("??? ????????? ??? ??????"),
                                fieldWithPath("number").description("?????? ????????? ??????"),
                                fieldWithPath("previous").description("?????? ????????? ??????"),
                                fieldWithPath("next").description("?????? ????????? ??????")

                        )));

        verify(categoryService, times(1)).getCategories(any());
    }

    @Test
    @DisplayName("????????? ???????????? ??????")
    void getParentCategoriesTest() throws Exception {
        GetCategoryResponseDto dto = new GetCategoryResponseDto(2, "????????????", null, 5, true);

        when(categoryService.getParentCategories()).thenReturn(List.of(dto));

        mockMvc.perform(get(path + "/parent"))
                .andExpect(jsonPath("$[0].categoryNo").value(dto.getCategoryNo()))
                .andExpect(jsonPath("$[0].categoryName").value(dto.getCategoryName()))
                .andExpect(jsonPath("$[0].parent").value(dto.getParent()))
                .andExpect(jsonPath("$[0].categoryPriority").value(dto.getCategoryPriority()))
                .andExpect(jsonPath("$[0].categoryDisplayed").value(dto.isCategoryDisplayed()))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("get-parentCategories",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].categoryNo").description("???????????? ??????"),
                                fieldWithPath("[].parent").description("?????? ????????????"),
                                fieldWithPath("[].categoryName").description("???????????? ??????(depth=2????????? null)"),
                                fieldWithPath("[].categoryPriority").description("???????????? ????????????(????????? ????????? ??????????????? ??????)"),
                                fieldWithPath("[].categoryDisplayed").description("???????????? ?????? ??????")
                        )));

        verify(categoryService, times(1)).getParentCategories();
    }

    @Test
    @DisplayName("??????????????? ?????? ???????????? ??????")
    void getParentWithChildrenTest() throws Exception {

        GetParentCategoryWithChildrenResponseDto responseDto = new GetParentCategoryWithChildrenResponseDto(
                1, "?????? ??????");

        GetChildCategoryResponseDto childDto = new GetChildCategoryResponseDto(2, "????????????");

        responseDto.setChildList(List.of(childDto));

        when(categoryService.getParentCategoryWithChildren()).thenReturn(List.of(responseDto));

        mockMvc.perform(get(path + "/parent-child"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].categoryNo").value(responseDto.getCategoryNo()))
                .andExpect(jsonPath("$[0].categoryName").value(responseDto.getCategoryName()))
                .andExpect(jsonPath("$[0].childList[0].categoryNo").value(childDto.getCategoryNo()))
                .andExpect(jsonPath("$[0].childList[0].categoryName").value(childDto.getCategoryName()))
                .andDo(print())
                .andDo(document("get-parentChildCategories",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].categoryNo").description("???????????? ??????"),
                                fieldWithPath("[].categoryName").description("?????? ???????????? ??????"),
                                fieldWithPath("[].childList[].categoryNo").description("?????? ???????????? ??????"),
                                fieldWithPath("[].childList[].categoryName").description("?????? ???????????? ??????")
                        )));

    }
}