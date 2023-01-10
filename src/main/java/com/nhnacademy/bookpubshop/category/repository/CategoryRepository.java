package com.nhnacademy.bookpubshop.category.repository;

import com.nhnacademy.bookpubshop.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 카테고리(Category) 테이블 레포지토리.
 *
 * @author : 박경서
 * @since : 1.0
 **/
public interface CategoryRepository extends JpaRepository<Category, Integer>,
        CategoryRepositoryCustom {

    boolean existsByCategoryName(String categoryName);
}