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
 * CategoryRestController 테스트 입니다.
 *
 * @author : 김서현
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
    @DisplayName("카테고리 등록 Valid Exception 으로 생성 실패_카테고리 이름 없음")
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
                                fieldWithPath("[].message").description("등록할 카테고리명을 기입해주세요.")
                        )));

        verify(categoryService, times(0)).addCategory(createCategoryRequestDto);
    }

    @Test
    @DisplayName("카테고리 등록 Valid Exception 으로 생성 실패_카테고리 이름 길이 초과")
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
                                fieldWithPath("[].message").description("카테고리명의 길이가 맞지않습니다.")
                        )));

        verify(categoryService, times(0)).addCategory(createCategoryRequestDto);
    }


    @Test
    @DisplayName("카테고리 등록 성공 테스트")
    void addCategorySuccessTest() throws Exception {
        ReflectionTestUtils.setField(createCategoryRequestDto, "categoryName", "국내도서");
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
                                fieldWithPath("categoryName").type(JsonFieldType.STRING).description("카테고리 이름"),
                                fieldWithPath("parentCategoryNo").type(JsonFieldType.NUMBER).description("상위 카테고리 번호").optional(),
                                fieldWithPath("categoryPriority").type(JsonFieldType.NUMBER).description("카테고리 우선순위(숫자가 클수록 우선순위가 높음)").optional(),
                                fieldWithPath("categoryDisplayed").type(JsonFieldType.BOOLEAN).description("카테고리 노출여부").optional()
                        )));

        verify(categoryService, times(1)).addCategory(captor.capture());

        CreateCategoryRequestDto result = captor.getValue();
        assertThat(result.getCategoryName()).isEqualTo(createCategoryRequestDto.getCategoryName());
        assertThat(result.getCategoryPriority()).isEqualTo(
                createCategoryRequestDto.getCategoryPriority());
        assertThat(result.isCategoryDisplayed()).isTrue();
    }

    @Test
    @DisplayName("카테고리 수정 Valid Exception 으로 수정 실패_카테고리 번호 없음")
    void modifyCategoryFailTest_categoryNoIsNull() throws Exception {
        ReflectionTestUtils.setField(modifyCategoryRequestDto, "categoryNo", null);
        ReflectionTestUtils.setField(modifyCategoryRequestDto, "categoryName", "국내도서");
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
                                fieldWithPath("[].message").description("수정할 카테고리 번호를 기입해주세요.")
                        )));

        verify(categoryService, times(0)).modifyCategory(modifyCategoryRequestDto);
    }

    @Test
    @DisplayName("카테고리 수정 Valid Exception 으로 수정 실패_카테고리 이름 없음")
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
                                fieldWithPath("[].message").description("수정할 카테고리명을 기입해주세요")
                        )));

        verify(categoryService, times(0)).modifyCategory(modifyCategoryRequestDto);
    }

    @Test
    @DisplayName("카테고리 수정 Valid Exception 으로 수정 실패_카테고리 이름 길이 초과")
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
                                fieldWithPath("[].message").description("카테고리명의 길이가 맞지않습니다.")
                        )));

    }

    @Test
    @DisplayName("카테고리 수정 성공 테스트.")
    void modifyCategorySuccessTest() throws Exception {

        ReflectionTestUtils.setField(modifyCategoryRequestDto, "categoryNo", 1);
        ReflectionTestUtils.setField(modifyCategoryRequestDto, "categoryName", "국내도서");
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
                                fieldWithPath("categoryNo").type(JsonFieldType.NUMBER).description("수정할 카테고리 번호"),
                                fieldWithPath("parentCategoryNo").type(JsonFieldType.NUMBER).description("상위 카테고리 번호").optional(),
                                fieldWithPath("categoryName").type(JsonFieldType.STRING).description("카테고리 이름"),
                                fieldWithPath("categoryPriority").type(JsonFieldType.NUMBER).description("카테고리 우선순위(숫자가 클수록 우선순위가 높음)").optional(),
                                fieldWithPath("categoryDisplayed").type(JsonFieldType.BOOLEAN).description("카테고리 노출여부").optional()
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
    @DisplayName("카테고리에 대한 단일값 조회 테스트.")
    void getCategoryDetailsTest() throws Exception {
        GetCategoryResponseDto parentDto = new GetCategoryResponseDto(1, "도서", null, 6, true);
        GetCategoryResponseDto dto = new GetCategoryResponseDto(2, "국내도서", parentDto, 5, true);

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
                                parameterWithName("categoryNo").description("조회할 카테고리 번호")
                        ),
                        responseFields(
                                fieldWithPath("categoryNo").description("카테고리 번호"),
                                fieldWithPath("categoryName").description("카테고리 이름"),
                                fieldWithPath("parent.categoryNo").description("상위 카테고리 번호"),
                                fieldWithPath("parent.categoryName").description("상위 카테고리 이름"),
                                fieldWithPath("parent.parent").description("상위 카테고리의 상위 카테고리(depth=2이므로 null)"),
                                fieldWithPath("parent.categoryPriority").description("상위 카테고리 우선순위"),
                                fieldWithPath("parent.categoryDisplayed").description("상위 카테고리 노출 여부"),
                                fieldWithPath("categoryPriority").description("카테고리 우선순위(숫자가 클수록 우선순위가 높음)"),
                                fieldWithPath("categoryDisplayed").description("카테고리 노출 여부")
                        )));

        verify(categoryService, times(1)).getCategory(anyInt());

    }


    @Test
    @DisplayName("카테고리 리스트 조회")
    void getCategoryListTest() throws Exception {
        GetCategoryResponseDto dto = new GetCategoryResponseDto(2, "국내도서", null, 5, true);
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
                                parameterWithName("page").description("페이지 정보 기입"),
                                parameterWithName("size").description("페이지 사이즈 기입")
                        ),
                        responseFields(
                                fieldWithPath("content[].categoryNo").description("카테고리 번호"),
                                fieldWithPath("content[].categoryName").description("카테고리 이름"),
                                fieldWithPath("content[].parent").description("최상위 카테고리"),
                                fieldWithPath("content[].categoryPriority").description(
                                        "카테고리 우선순위(숫자가 작을수록 우선순위가 높음)"),
                                fieldWithPath("content[].categoryDisplayed").description(
                                        "카테고리 노출 여부"),
                                fieldWithPath("totalPages").description("총 페이지 수 반환"),
                                fieldWithPath("number").description("현재 페이지 반환"),
                                fieldWithPath("previous").description("이전 페이지 여부"),
                                fieldWithPath("next").description("다음 페이지 여부")

                        )));

        verify(categoryService, times(1)).getCategories(any());
    }

    @Test
    @DisplayName("최상의 카테고리 조회")
    void getParentCategoriesTest() throws Exception {
        GetCategoryResponseDto dto = new GetCategoryResponseDto(2, "국내도서", null, 5, true);

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
                                fieldWithPath("[].categoryNo").description("카테고리 번호"),
                                fieldWithPath("[].parent").description("상위 카테고리"),
                                fieldWithPath("[].categoryName").description("카테고리 이름(depth=2이므로 null)"),
                                fieldWithPath("[].categoryPriority").description("카테고리 우선순위(숫자가 클수록 우선순위가 높음)"),
                                fieldWithPath("[].categoryDisplayed").description("카테고리 노출 여부")
                        )));

        verify(categoryService, times(1)).getParentCategories();
    }

    @Test
    @DisplayName("메인페이지 에서 카테고리 조회")
    void getParentWithChildrenTest() throws Exception {

        GetParentCategoryWithChildrenResponseDto responseDto = new GetParentCategoryWithChildrenResponseDto(
                1, "국내 도서");

        GetChildCategoryResponseDto childDto = new GetChildCategoryResponseDto(2, "추리소설");

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
                                fieldWithPath("[].categoryNo").description("카테고리 번호"),
                                fieldWithPath("[].categoryName").description("상위 카테고리 이름"),
                                fieldWithPath("[].childList[].categoryNo").description("하위 카테고리 번호"),
                                fieldWithPath("[].childList[].categoryName").description("하위 카테고리 이름")
                        )));

    }
}