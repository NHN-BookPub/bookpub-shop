package com.nhnacademy.bookpubshop.category.repository.impl;

import com.nhnacademy.bookpubshop.category.dto.response.GetCategoryResponseDto;
import com.nhnacademy.bookpubshop.category.entity.Category;
import com.nhnacademy.bookpubshop.category.entity.QCategory;
import com.nhnacademy.bookpubshop.category.repository.CategoryRepositoryCustom;
import com.querydsl.core.types.Projections;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

/**
 * 카테고리 Custom Repository 구현체.
 *
 * @author : 김서현
 * @since : 1.0
 **/
public class CategoryRepositoryImpl extends QuerydslRepositorySupport implements
        CategoryRepositoryCustom {

    public CategoryRepositoryImpl() {
        super(Category.class);
    }
    QCategory parent = new QCategory("parent");

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<GetCategoryResponseDto> findCategory(Integer categoryNo) {
        QCategory category = QCategory.category;

        return Optional.ofNullable(from(category, parent)
                .where(category.categoryNo.eq(categoryNo))
                .select(Projections.constructor(GetCategoryResponseDto.class, category.categoryNo,
                        category.categoryName,
                        Projections.constructor(GetCategoryResponseDto.class, parent.categoryNo,
                                parent.categoryName), category.categoryPriority,
                        category.categoryDisplayed))
                .leftJoin(category.parentCategory, parent).on(parent.eq(category.parentCategory))
                .fetchFirst());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetCategoryResponseDto> findCategories() {

        QCategory category = QCategory.category;

        return from(category)
                .select(Projections.constructor(GetCategoryResponseDto.class,
                        category.categoryNo,
                        category.categoryName,
                        Projections.constructor(GetCategoryResponseDto.class, parent.categoryNo,
                                parent.categoryName),category.categoryPriority,
                        category.categoryDisplayed))
                .leftJoin(category.parentCategory, parent).on(parent.eq(category.parentCategory))
                .orderBy(category.categoryPriority.desc()).orderBy(category.categoryName.asc())
                .fetch();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetCategoryResponseDto> findCategoriesDisplayedTrue() {
        QCategory category = QCategory.category;

        return from(category)
                .where(category.categoryDisplayed.isTrue())
                .select(Projections.constructor(GetCategoryResponseDto.class,
                        category.categoryNo,
                        category.categoryName,
                        Projections.constructor(GetCategoryResponseDto.class, parent.categoryNo,
                                parent.categoryName),category.categoryPriority,
                        category.categoryDisplayed))
                .leftJoin(category.parentCategory, parent).on(parent.eq(category.parentCategory))
                .orderBy(category.categoryPriority.desc()).orderBy(category.categoryName.asc())
                .fetch();
    }

}
