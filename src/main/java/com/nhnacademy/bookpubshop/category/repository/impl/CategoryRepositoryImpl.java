package com.nhnacademy.bookpubshop.category.repository.impl;

import com.nhnacademy.bookpubshop.category.dto.response.GetCategoryInfoResponseDto;
import com.nhnacademy.bookpubshop.category.dto.response.GetCategoryResponseDto;
import com.nhnacademy.bookpubshop.category.dto.response.GetChildCategoryResponseDto;
import com.nhnacademy.bookpubshop.category.dto.response.GetParentCategoryWithChildrenResponseDto;
import com.nhnacademy.bookpubshop.category.entity.Category;
import com.nhnacademy.bookpubshop.category.entity.QCategory;
import com.nhnacademy.bookpubshop.category.repository.CategoryRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;

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
    public Page<GetCategoryResponseDto> findCategories(Pageable pageable) {

        QCategory category = QCategory.category;

        JPQLQuery<Long> count = from(category).select(category.count());

        List<GetCategoryResponseDto> content = from(category)
                .select(Projections.constructor(GetCategoryResponseDto.class,
                        category.categoryNo,
                        category.categoryName,
                        Projections.constructor(GetCategoryResponseDto.class, parent.categoryNo,
                                parent.categoryName), category.categoryPriority,
                        category.categoryDisplayed))
                .leftJoin(category.parentCategory, parent).on(parent.eq(category.parentCategory))
                .orderBy(category.categoryPriority.asc()).orderBy(category.categoryName.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);

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
                .orderBy(parent.categoryPriority.asc()).orderBy(parent.categoryName.asc())
                .fetch();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetParentCategoryWithChildrenResponseDto> findParentCategoryWithChildren() {
        QCategory child = QCategory.category;

        List<GetParentCategoryWithChildrenResponseDto> parentList = from(parent)
                .where(parent.parentCategory.isNull(), parent.categoryDisplayed.isTrue())
                .select(Projections.constructor(GetParentCategoryWithChildrenResponseDto.class,
                        parent.categoryNo,
                        parent.categoryName))
                .orderBy(parent.categoryPriority.asc(), parent.categoryName.asc())
                .fetch();

        parentList.forEach(p -> {
            List<GetChildCategoryResponseDto> childList = from(child)
                    .select(Projections.constructor(GetChildCategoryResponseDto.class,
                            child.categoryNo, child.categoryName))
                    .where(child.parentCategory.categoryNo.eq(p.getCategoryNo()),
                            child.categoryDisplayed.isTrue())
                    .orderBy(child.categoryPriority.asc(), child.categoryName.asc())
                    .fetch();
            p.setChildList(childList);
        });

        return parentList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> findChildNameByParentNo(Integer parentCategoryNo) {
        QCategory category = QCategory.category;

        return from(category)
                .select(category.categoryName)
                .where(category.parentCategory.categoryNo.eq(parentCategoryNo))
                .fetch();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetCategoryInfoResponseDto> findCategoriesInfo() {
        QCategory category = QCategory.category;

        return from(category)
                .select(Projections.constructor(GetCategoryInfoResponseDto.class,
                        category.categoryNo,
                        category.categoryName))
                .distinct()
                .fetch();
    }
}
