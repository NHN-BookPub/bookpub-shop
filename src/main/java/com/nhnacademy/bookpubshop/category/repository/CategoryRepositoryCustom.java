package com.nhnacademy.bookpubshop.category.repository;

import com.nhnacademy.bookpubshop.category.dto.response.GetCategoryInfoResponseDto;
import com.nhnacademy.bookpubshop.category.dto.response.GetCategoryResponseDto;
import com.nhnacademy.bookpubshop.category.dto.response.GetParentCategoryWithChildrenResponseDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
     * 전체 카테고리 반환 ( 관리자용 - 노출 여부 상관없이 전체 반환 , 우선 순위 높은 순, 동일 시 이름 순).
     *
     * @return 전체 카테고리 정보 반환.
     */
    Page<GetCategoryResponseDto> findCategories(Pageable pageable);


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

    /**
     * 부모 카테고리 번호로 하위 카테고리 조회.
     *
     * @param parentCategoryNo 부모 카테고리 번호
     * @return 하위 카테고리 이름 리스트
     */
    List<String> findChildNameByParentNo(Integer parentCategoryNo);


    /**
     * 카테고리 기본 정보 조회.
     *
     * @return 카테고리 번호 이름 반환.
     */
    List<GetCategoryInfoResponseDto> findCategoriesInfo();

}
