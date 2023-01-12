package com.nhnacademy.bookpubshop.category.repository;

import com.nhnacademy.bookpubshop.category.dto.response.GetCategoryResponseDto;
import com.nhnacademy.bookpubshop.category.dto.response.GetParentCategoryWithChildrenResponseDto;
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

    /**
     * 카테고리 번호로 관련 카테고리 반환.
     *
     * @param categoryNo 카테고리 번호.
     * @return Optional 로 카테고리 정보 반환.
     */
    Optional<GetCategoryResponseDto> findCategory(Integer categoryNo);

    /**
     * 전체 카테고리 반환 ( 관리자용 - 노출 여부 상관없이 전체 반환 , 우선 순위 높은 순, 동일 시 이름 순).
     *
     * @return 전체 카테고리 정보 반환.
     */
    List<GetCategoryResponseDto> findCategories();


    /**
     * 최상위 카테고리를 조회.
     *
     * @return 최상위 카테고리 반환.
     */
    List<GetCategoryResponseDto> findParentCategories();

    /**
     * 상위 카테고리와 그 아래 하위 카테고리 조회.
     *
     * @return 최상위와 해당 하위 카테고리 리스트 반환.
     */
    List<GetParentCategoryWithChildrenResponseDto> findParentCategoryWithChildren();
}
