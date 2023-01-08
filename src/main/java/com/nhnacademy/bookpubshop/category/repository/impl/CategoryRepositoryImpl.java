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

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<GetCategoryResponseDto> findCategory(Integer categoryNo) {
        QCategory category = QCategory.category;
        QCategory parent = new QCategory("parent");

        return Optional.ofNullable(from(category, parent)
                .where(category.categoryNo.eq(categoryNo))
                .select(Projections.constructor(GetCategoryResponseDto.class, category.categoryNo,
                        category.categoryName,
                        Projections.constructor(GetCategoryResponseDto.class, parent.categoryNo,
                                parent.categoryName)))
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
                        category.categoryName))
                .orderBy(category.categoryNo.asc())
                .fetch();
    }
}
