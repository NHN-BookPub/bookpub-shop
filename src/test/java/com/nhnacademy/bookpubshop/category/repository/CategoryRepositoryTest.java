package com.nhnacademy.bookpubshop.category.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.category.dto.response.GetCategoryResponseDto;
import com.nhnacademy.bookpubshop.category.dto.response.GetParentCategoryWithChildrenResponseDto;
import com.nhnacademy.bookpubshop.category.entity.Category;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
        category = new Category(null, null, "국내도서", 0, true);

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
    @DisplayName("카테고리 다건 조회 테스트 입니다.")
    void categoriesGetTest() {
        String categoryName = "로맨스소설";
        Category romanceCategory = new Category(null, category, categoryName, 0, true);
        Pageable pageable = PageRequest.of(0, 10);

        categoryRepository.save(category);
        categoryRepository.save(romanceCategory);

        Page<GetCategoryResponseDto> result = categoryRepository.findCategories(pageable);

        assertThat(result)
                .isNotEmpty()
                .hasSize(2);

        assertThat(result.getContent().get(0).getCategoryName()).isEqualTo(
                category.getCategoryName());
        assertThat(result.getContent().get(1).getCategoryName()).isEqualTo(
                romanceCategory.getCategoryName());
        assertThat(result.getContent().get(1).getParent().getCategoryName()).isEqualTo(
                category.getCategoryName());

    }

    @Test
    @DisplayName("최상위 카테고리만 조회하는 테스트입니다.")
    void parentCategoryGetTest() {
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

    @Test
    @DisplayName("메인메이지에서 조회할 공개여부 true, 우선순위 카테고리(상,하위) 조회 테스트입니다.")
    void parentCategoryWithChildrenGetTest() {
        Category foreignCategory = new Category(null, null, "외국도서", 1, true);
        Category romanceCategory = new Category(null, foreignCategory, "로맨스소설", 0, true);
        Category newspaperCategory = new Category(null, foreignCategory, "신문", 0, false);
        Category detectiveCategory = new Category(null, foreignCategory, "추리소설", 1, true);
        Category fantasyCategory = new Category(null, category, "판타지소설", 0, true);
        categoryRepository.save(category);
        categoryRepository.save(foreignCategory);
        categoryRepository.save(newspaperCategory);
        categoryRepository.save(detectiveCategory);
        categoryRepository.save(fantasyCategory);
        categoryRepository.save(romanceCategory);

        List<GetParentCategoryWithChildrenResponseDto> result = categoryRepository.findParentCategoryWithChildren();

        assertThat(result).hasSize(2);

        assertThat(result.get(0).getCategoryName()).isEqualTo(category.getCategoryName());
        assertThat(result.get(0).getChildList()).hasSize(1);
        assertThat(result.get(1).getChildList()).hasSize(2);
        assertThat(result.get(1).getChildList().get(0).getCategoryName()).isEqualTo(
                romanceCategory.getCategoryName());

    }
}