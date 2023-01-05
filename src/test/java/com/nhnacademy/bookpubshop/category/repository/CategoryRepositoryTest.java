package com.nhnacademy.bookpubshop.category.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.category.entity.Category;
import java.util.Optional;
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

    @Test
    @DisplayName(value = "카테고리(Category) 레포지토리 save 테스트")
    void categorySaveTest() {
        Category testCategory = new Category(null, null, "소설", 0, true);
        categoryRepository.save(testCategory);

        Optional<Category> optional = categoryRepository.findById(testCategory.getCategoryNo());
        assertThat(optional).isPresent();
        assertThat(optional.get().getCategoryNo()).isEqualTo(testCategory.getCategoryNo());
        assertThat(optional.get().getCategoryName()).isEqualTo(testCategory.getCategoryName());
        assertThat(optional.get().getCategoryPriority()).isEqualTo(testCategory.getCategoryPriority());
        assertThat(optional.get().isCategoryDisplayed()).isEqualTo(testCategory.isCategoryDisplayed());

        entityManager.clear();
    }
}