package com.nhnacademy.bookpubshop.category.controller;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
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
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

/**
 * CategoryRestController 테스트 입니다.
 *
 * @author : 김서현
 * @since : 1.0
 **/
@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CategoryService categoryService;

    ObjectMapper objectMapper;

    GetCategoryResponseDto getCategoryResponseDto;

    CreateCategoryRequestDto createCategoryRequestDto;

    ModifyCategoryRequestDto modifyCategoryRequestDto;

    String path = "/api/categories";

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        createCategoryRequestDto = new CreateCategoryRequestDto();
        modifyCategoryRequestDto = new ModifyCategoryRequestDto();
        getCategoryResponseDto = new GetCategoryResponseDto();

    }

    @Test
    @DisplayName("카테고리 등록 Valid Exception 으로 생성 실패")
    void addCategoryFailTest() throws Exception {
        ReflectionTestUtils.setField(createCategoryRequestDto, "categoryName", "");
        ReflectionTestUtils.setField(createCategoryRequestDto, "categoryPriority", 0);
        ReflectionTestUtils.setField(createCategoryRequestDto, "categoryDisplayed", true);
        doNothing().when(categoryService).addCategory(createCategoryRequestDto);

        mockMvc.perform(post(path)
                        .content(objectMapper.writeValueAsString(createCategoryRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andDo(print());

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

        mockMvc.perform(post(path)
                        .content(objectMapper.writeValueAsString(createCategoryRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());

        verify(categoryService, times(1)).addCategory(captor.capture());
        CreateCategoryRequestDto result = captor.getValue();
        assertThat(result.getCategoryName()).isEqualTo(createCategoryRequestDto.getCategoryName());
        assertThat(result.getCategoryPriority()).isEqualTo(
                createCategoryRequestDto.getCategoryPriority());
        assertThat(result.isCategoryDisplayed()).isTrue();
    }

    @Test
    @DisplayName("카테고리 수정 validation Exception 로 인한 실패 테스트.")
    void modifyCategoryFailTest() throws Exception {

        ReflectionTestUtils.setField(modifyCategoryRequestDto, "categoryNo", 1);
        ReflectionTestUtils.setField(modifyCategoryRequestDto, "categoryName", "");
        ReflectionTestUtils.setField(modifyCategoryRequestDto, "categoryPriority", 0);
        ReflectionTestUtils.setField(modifyCategoryRequestDto, "categoryDisplayed", true);
        doNothing().when(categoryService).modifyCategory(modifyCategoryRequestDto);

        mockMvc.perform(put(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifyCategoryRequestDto)))
                .andExpect(status().is4xxClientError())
                .andDo(print());

        verify(categoryService, times(0)).modifyCategory(modifyCategoryRequestDto);
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

        mockMvc.perform(put(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifyCategoryRequestDto)))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());

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

        ReflectionTestUtils.setField(getCategoryResponseDto, "categoryName", "국내도서");

        when(categoryService.getCategory(anyInt())).thenReturn(getCategoryResponseDto);

        mockMvc.perform(get(path + "/{categoryNo}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.categoryName").value(getCategoryResponseDto.getCategoryName()))
                .andDo(print());

        verify(categoryService, times(1)).getCategory(anyInt());

    }

    @Test
    @DisplayName("카테고리 리스트 조회")
    void getCategoryListTest() throws Exception {

        ReflectionTestUtils.setField(getCategoryResponseDto, "categoryName", "국내도서");

        when(categoryService.getCategories()).thenReturn(List.of(getCategoryResponseDto));

        mockMvc.perform(get(path)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].categoryName").value(
                        getCategoryResponseDto.getCategoryName()))
                .andDo(print());

        verify(categoryService, times(1)).getCategories();
    }

    @Test
    @DisplayName("최상의 카테고리 조회")
    void getParentCategoriesTest() throws Exception {
        ReflectionTestUtils.setField(getCategoryResponseDto, "categoryNo", 1);
        ReflectionTestUtils.setField(getCategoryResponseDto, "categoryName", "국내도서");
        ReflectionTestUtils.setField(getCategoryResponseDto, "parent", null);
        ReflectionTestUtils.setField(getCategoryResponseDto, "categoryDisplayed", true);
        ReflectionTestUtils.setField(getCategoryResponseDto, "categoryPriority", 0);

        when(categoryService.getParentCategories()).thenReturn(List.of(getCategoryResponseDto));

        mockMvc.perform(get(path + "/parent"))
                .andExpect(jsonPath("$[0].categoryName").value(
                        getCategoryResponseDto.getCategoryName()))
                .andExpect(status().isOk())
                .andDo(print());

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
                .andExpect(jsonPath("$[0].categoryName").value(responseDto.getCategoryName()))
                .andDo(print());

    }
}