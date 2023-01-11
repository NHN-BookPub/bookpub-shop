package com.nhnacademy.bookpubshop.category.repository.impl;

import com.nhnacademy.bookpubshop.category.dto.response.GetCategoryResponseDto;
import com.nhnacademy.bookpubshop.category.dto.response.GetChildCategoryResponseDto;
import com.nhnacademy.bookpubshop.category.dto.response.GetParentCategoryWithChildrenResponseDto;
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
                                parent.categoryName), category.categoryPriority,
                        category.categoryDisplayed))
                .leftJoin(category.parentCategory, parent).on(parent.eq(category.parentCategory))
                .orderBy(category.categoryPriority.desc()).orderBy(category.categoryName.asc())
                .fetch();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetCategoryResponseDto> findParentCategories() {
        return from(parent)
                .where(parent.parentCategory.isNull())
                .select(Projections.constructor(GetCategoryResponseDto.class,
                        parent.categoryNo,
                        parent.categoryName,
                        parent.categoryPriority,
                        parent.categoryDisplayed))
                .orderBy(parent.categoryPriority.desc()).orderBy(parent.categoryName.asc())
                .fetch();
    }

    @Override
    public List<GetParentCategoryWithChildrenResponseDto> findParentCategoryWithChildren() {
        QCategory child = QCategory.category;

        //1. 부모 리스트 가져오기 - 공개, 순서
        List<GetParentCategoryWithChildrenResponseDto> parentList = from(parent)
                .where(parent.parentCategory.isNull(), parent.categoryDisplayed.isTrue())
                .select(Projections.constructor(GetParentCategoryWithChildrenResponseDto.class,
                        parent.categoryNo,
                        parent.categoryName))
                .orderBy(parent.categoryPriority.desc(), parent.categoryName.asc())
                .fetch();

        // 2. For 문 돌면서 쿼리 결과값을 setter 이용해서 집어넣기.
        parentList.forEach(p -> {
            List<GetChildCategoryResponseDto> childList = from(child)
                    .select(Projections.constructor(GetChildCategoryResponseDto.class,
                            child.categoryNo, child.categoryName))
                    .where(child.parentCategory.categoryNo.eq(p.getCategoryNo()),
                            child.categoryDisplayed.isTrue())
                    .orderBy(child.categoryPriority.desc(), child.categoryName.asc())
                    .fetch();
            p.setChildList(childList);
        });

        return parentList;
    }
}
