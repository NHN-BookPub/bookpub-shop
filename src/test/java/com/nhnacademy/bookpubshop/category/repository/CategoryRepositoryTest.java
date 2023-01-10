package com.nhnacademy.bookpubshop.category.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.category.dto.response.GetCategoryResponseDto;
import com.nhnacademy.bookpubshop.category.entity.Category;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * 카테고리(Category) 레포지토리 테스트.
 *
 * @author : 박경서
 * @since : 1.0
 **/
@DataJpaTest
class CategoryRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    CategoryRepository categoryRepository;

    Category category;

    @BeforeEach
    void setUp() {
        category = new Category(null, null, "국내소설", 0, true);

    }

    @Test
    @DisplayName(value = "카테고리(Category) 레포지토리 save 테스트")
    void categorySaveTest() {
        categoryRepository.save(category);

        Optional<Category> optional = categoryRepository.findById(category.getCategoryNo());
        assertThat(optional).isPresent();
        assertThat(optional.get().getCategoryNo()).isEqualTo(category.getCategoryNo());
        assertThat(optional.get().getCategoryName()).isEqualTo(category.getCategoryName());
        assertThat(optional.get().getCategoryPriority()).isEqualTo(category.getCategoryPriority());
        assertThat(optional.get().isCategoryDisplayed()).isEqualTo(category.isCategoryDisplayed());

        entityManager.clear();
    }

    @Test
    @DisplayName("카테고리 단건 조회 테스트입니다.")
    void categoryGetTest() {
        String expectedChildName = "판타지소설";
        Category child = new Category(null, category, expectedChildName, 0, true);
        Category savedParent = categoryRepository.save(category);
        Category savedChild = categoryRepository.save(child);

        Optional<GetCategoryResponseDto> result = categoryRepository.findCategory(
                savedChild.getCategoryNo());
        assertThat(result).isPresent();
        assertThat(result.get().getCategoryName()).isEqualTo(child.getCategoryName());
        assertThat(result.get().getParent().getCategoryName()).isEqualTo(
                savedParent.getCategoryName());
        assertThat(result.get().getCategoryPriority()).isEqualTo(savedChild.getCategoryPriority());
        assertThat(result.get().isCategoryDisplayed()).isEqualTo(savedChild.isCategoryDisplayed());
    }

    @Test
    @DisplayName("카테고리 다건 조회 테스트 입니다.")
    void categoriesGetTest() {
        String categoryName = "로맨스소설";
        Category romanceCategory = new Category(null, category, categoryName, 0, true);
        categoryRepository.save(category);
        categoryRepository.save(romanceCategory);

        List<GetCategoryResponseDto> result = categoryRepository.findCategories();

        assertThat(result)
                .isNotEmpty()
                .hasSize(2);

        assertThat(result.get(0).getCategoryName()).isEqualTo(category.getCategoryName());
        assertThat(result.get(1).getCategoryName()).isEqualTo(romanceCategory.getCategoryName());
        assertThat(result.get(1).getParent().getCategoryName()).isEqualTo(
                category.getCategoryName());

    }

    @Test
    @DisplayName("카테고리 다건 조회 노출여부 테스트 입니다.")
    void displayedTrueCategoriesGetTest() {
        String romance = "로맨스소설";
        String fantasy = "판타지소설";
        Category romanceCategory = new Category(null, category, romance, 0, false);
        Category fantasyCategory = new Category(null, category, fantasy, 1, true);
        categoryRepository.save(category);
        categoryRepository.save(fantasyCategory);
        categoryRepository.save(romanceCategory);

        List<GetCategoryResponseDto> result = categoryRepository.findCategoriesDisplayedTrue();

        assertThat(result)
                .isNotEmpty()
                .hasSize(2);

        assertThat(result.get(0).getCategoryName()).isEqualTo(fantasyCategory.getCategoryName());
        assertThat(result.get(0).getParent().getCategoryName()).isEqualTo(
                category.getCategoryName());
        assertThat(result.get(1).getCategoryName()).isEqualTo(category.getCategoryName());
        result.forEach(value -> assertThat(value.isCategoryDisplayed()).isTrue());
    }

    @Test
    @DisplayName("최상위 카테고리만 조회하는 테스트입니다.")
    void parentCategoryGetTest(){
        String romance = "로맨스소설";
        String fantasy = "판타지소설";
        Category romanceCategory = new Category(null, category, romance, 0, true);
        Category fantasyCategory = new Category(null, category, fantasy, 0, true);
        categoryRepository.save(category);
        categoryRepository.save(fantasyCategory);
        categoryRepository.save(romanceCategory);

        List<GetCategoryResponseDto> result = categoryRepository.findParentCategories();

        assertThat(result)
                .isNotEmpty()
                .hasSize(1);

        assertThat(result.get(0).getCategoryName()).isEqualTo(category.getCategoryName());
        assertThat(result.get(0).getParent()).isNull();

    }
}