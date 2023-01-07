package com.nhnacademy.bookpubshop.category.repository;

import com.nhnacademy.bookpubshop.category.dto.response.GetCategoryResponseDto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * category 가 querydsl 을 사용하기위한 interface 입니다.
 *
 * @author : 김서현
 * @since : 1.0
 **/
@NoRepositoryBean
public interface CategoryRepositoryCustom {

    Optional<GetCategoryResponseDto> findCategory(Integer categoryNo);

    List<GetCategoryResponseDto> findCategories();

}
