package com.nhnacademy.bookpubshop.category.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import com.nhnacademy.bookpubshop.category.dto.request.CreateCategoryRequestDto;
import com.nhnacademy.bookpubshop.category.dto.request.ModifyCategoryRequestDto;
import com.nhnacademy.bookpubshop.category.dto.response.GetCategoryResponseDto;
import com.nhnacademy.bookpubshop.category.dto.response.GetParentCategoryWithChildrenResponseDto;
import com.nhnacademy.bookpubshop.category.entity.Category;
import com.nhnacademy.bookpubshop.category.exception.CategoryAlreadyExistsException;
import com.nhnacademy.bookpubshop.category.exception.CategoryNotFoundException;
import com.nhnacademy.bookpubshop.category.repository.CategoryRepository;
import com.nhnacademy.bookpubshop.category.service.impl.CategoryServiceImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * CategoryService 테스트 입니다.
 *
 * @author : 김서현
 * @since : 1.0
 **/
class CategoryServiceTest {

    private CategoryService categoryService;

    private CategoryRepository categoryRepository;

    Category category;

    CreateCategoryRequestDto createCategoryRequestDto;

    GetCategoryResponseDto getCategoryResponseDto;

    ModifyCategoryRequestDto modifyCategoryRequestDto;

    ArgumentCaptor<Category> captor;

    @BeforeEach
    void setUp() {
        categoryRepository = Mockito.mock(CategoryRepository.class);
        categoryService = new CategoryServiceImpl(categoryRepository);
        category = new Category(null, null, "외국도서", 0, true);
        createCategoryRequestDto = new CreateCategoryRequestDto();
        getCategoryResponseDto = new GetCategoryResponseDto();
        modifyCategoryRequestDto = new ModifyCategoryRequestDto();
        captor = ArgumentCaptor.forClass(Category.class);
    }

    @Test
    @DisplayName("카테고리 등록시 카테고리명 중복으로 실패한 경우")
    void addCategoryFailTest() {

        String categoryName = "외국도서";

        ReflectionTestUtils.setField(createCategoryRequestDto, "categoryName", categoryName);
        ReflectionTestUtils.setField(createCategoryRequestDto, "parentCategoryNo", null);
        ReflectionTestUtils.setField(createCategoryRequestDto, "categoryPriority", 0);
        ReflectionTestUtils.setField(createCategoryRequestDto, "categoryDisplayed", true);

        when(categoryRepository.existsByCategoryName(categoryName)).thenReturn(true);

        assertThatThrownBy(() -> categoryService.addCategory(createCategoryRequestDto))
                .isInstanceOf(CategoryAlreadyExistsException.class)
                .hasMessageContaining(" 은 이미 존재하는 카테고리입니다.");

    }

    @Test
    @DisplayName("카테고리 등록시 존재하지 않는 부모카테고리 입력으로 실패")
    void addCategoryNotExistParentCategoryFailTest() {

        String categoryName = "외국도서";

        ReflectionTestUtils.setField(createCategoryRequestDto, "categoryName", categoryName);
        ReflectionTestUtils.setField(createCategoryRequestDto, "parentCategoryNo", 2);
        ReflectionTestUtils.setField(createCategoryRequestDto, "categoryPriority", 0);
        ReflectionTestUtils.setField(createCategoryRequestDto, "categoryDisplayed", true);

        when(categoryRepository.existsByCategoryName(categoryName)).thenReturn(false);
        when(categoryRepository.findById(2)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.addCategory(createCategoryRequestDto))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessageContaining("카테고리가 존재하지않습니다.");

    }

    @Test
    @DisplayName("카테고리 등록 성공 테스트.")
    void addCategorySuccessTest() {

        String categoryName = "국내도서";

        ReflectionTestUtils.setField(createCategoryRequestDto, "categoryName", categoryName);
        ReflectionTestUtils.setField(createCategoryRequestDto, "parentCategoryNo", null);
        ReflectionTestUtils.setField(createCategoryRequestDto, "categoryPriority", 0);
        ReflectionTestUtils.setField(createCategoryRequestDto, "categoryDisplayed", true);

        when(categoryRepository.existsByCategoryName(anyString())).thenReturn(false);

        categoryService.addCategory(createCategoryRequestDto);

        verify(categoryRepository, times(1))
                .save(captor.capture());

        Category result = captor.getValue();
        assertThat(result.getCategoryName()).isEqualTo(createCategoryRequestDto.getCategoryName());

    }

    @Test
    @DisplayName("카테고리 수정시 조회 실패 테스트.")
    void modifyCategoryFindFailTest() {
        ReflectionTestUtils.setField(modifyCategoryRequestDto, "categoryName", "외국도서");

        when(categoryRepository.findById(anyInt()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.modifyCategory(modifyCategoryRequestDto))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessageContaining("카테고리가 존재하지않습니다.");

    }

    @Test
    @DisplayName("카테고리 수정시 카테고리명 중복으로 실패 테스트.")
    void modifyCategoryNameFailTest() {

        ReflectionTestUtils.setField(modifyCategoryRequestDto, "categoryNo", 1);
        ReflectionTestUtils.setField(modifyCategoryRequestDto, "categoryName", "국내도서");

        when(categoryRepository.findById(anyInt()))
                .thenReturn(Optional.of(category));

        when(categoryRepository.existsByCategoryName(anyString())).thenReturn(true);

        assertThatThrownBy(() -> categoryService.modifyCategory(modifyCategoryRequestDto))
                .isInstanceOf(CategoryAlreadyExistsException.class)
                .hasMessageContaining(" 은 이미 존재하는 카테고리입니다.");
    }

    @Test
    @DisplayName("카테고리 수정시 존재하지 않는 부모카테고리 입력으로 실패")
    void modifyCategoryNotExistParentCategoryFailTest() {

        String categoryName = "외국도서";

        ReflectionTestUtils.setField(modifyCategoryRequestDto, "categoryNo", 1);
        ReflectionTestUtils.setField(modifyCategoryRequestDto, "parentCategoryNo", 2);
        ReflectionTestUtils.setField(modifyCategoryRequestDto, "categoryName", categoryName);

        when(categoryRepository.existsByCategoryName(categoryName)).thenReturn(false);
        when(categoryRepository.findById(2)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.modifyCategory(modifyCategoryRequestDto))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessageContaining("카테고리가 존재하지않습니다.");

    }

    @Test
    @DisplayName("카테고리 기존 이름으로 수정 성공 테스트.")
    void modifyCategorySuccessTest() {

        ReflectionTestUtils.setField(modifyCategoryRequestDto, "categoryNo", 1);
        ReflectionTestUtils.setField(modifyCategoryRequestDto, "categoryName", "외국도서");

        when(categoryRepository.findById(anyInt()))
                .thenReturn(Optional.of(category));

        categoryService.modifyCategory(modifyCategoryRequestDto);

        verify(categoryRepository, times(1)).findById(anyInt());

    }

    @Test
    @DisplayName("카테고리 단건 조회 실패 테스트.")
    void getCategoryFailTest() {

        when(categoryRepository.findById(anyInt()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.getCategory(1))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessageContaining("카테고리가 존재하지않습니다.");
    }


    @Test
    @DisplayName("카테고리 단건 조회 성공 테스트.")
    void getCategorySuccessTest() {

        String categoryName = "판타지도서";

        categoryRepository.save(category);
        ReflectionTestUtils.setField(getCategoryResponseDto, "categoryName", categoryName);

        when(categoryRepository.save(any())).thenReturn(category);
        when(categoryRepository.findById(anyInt())).thenReturn(
                Optional.of(category));

        categoryService.getCategory(anyInt());

        verify(categoryRepository, times(1)).findById(anyInt());

    }

    @Test
    @DisplayName("카테고리 다건 조회 성공 테스트.")
    void getCategoriesSuccessTest() {
        List<GetCategoryResponseDto> responses = new ArrayList<>();
        responses.add(getCategoryResponseDto);

        Pageable pageable = Pageable.ofSize(5);
        Page<GetCategoryResponseDto> page = PageableExecutionUtils.getPage(responses, pageable,
                () -> 1L);

        when(categoryRepository.findCategories(pageable)).thenReturn(page);

        assertThat(categoryService.getCategories(pageable).getContent().get(0)
                .getCategoryName()).isEqualTo(
                getCategoryResponseDto.getCategoryName());

        verify(categoryRepository, times(1)).findCategories(pageable);
    }


    @Test
    @DisplayName("최상위 카테고리만 조회 성공 테스트")
    void getParentCategoriesSuccessTest() {

        when(categoryRepository.findParentCategories()).thenReturn(List.of(getCategoryResponseDto));
        List<GetCategoryResponseDto> categories = categoryService.getParentCategories();
        assertThat(categories.get(0).getCategoryName()).isEqualTo(
                getCategoryResponseDto.getCategoryName());

        verify(categoryRepository, times(1)).findParentCategories();

    }

    @Test
    @DisplayName("최상위 카레고리와 그 하위 카테고리 조회 성공 테스트")
    void getParentCategoryWithChildrenSuccessTest() {
        GetParentCategoryWithChildrenResponseDto dto = mock(
                GetParentCategoryWithChildrenResponseDto.class);

        when(categoryRepository.findParentCategoryWithChildren()).thenReturn(List.of(dto));
        List<GetParentCategoryWithChildrenResponseDto> parentCategoryWithChildren = categoryService.getParentCategoryWithChildren();
        assertThat(parentCategoryWithChildren.get(0)).isEqualTo(dto);

        verify(categoryRepository, times(1)).findParentCategoryWithChildren();

    }

}