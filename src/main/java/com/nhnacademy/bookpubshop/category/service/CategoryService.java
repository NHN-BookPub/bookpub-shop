package com.nhnacademy.bookpubshop.category.service;

import com.nhnacademy.bookpubshop.category.dto.request.CreateCategoryRequestDto;
import com.nhnacademy.bookpubshop.category.dto.request.ModifyCategoryRequestDto;
import com.nhnacademy.bookpubshop.category.dto.response.GetCategoryResponseDto;
import java.util.List;

/**
 * 카테고리 서비스 입니다.
 *
 * @author : 김서현
 * @since : 1.0
 **/

public interface CategoryService {

    /**
     * 카테고리를 생성하기 위한 메소드입니다.
     *
     * @param createCategoryRequestDto 카테고리 생성을 위한 정보들이 기입됩니다.
     */
    void addCategory(CreateCategoryRequestDto createCategoryRequestDto);


    /**
     * 카테고리를 수정하기 위한 메소드입니다.
     *
     * @param modifyCategoryRequestDto 카테고리 수정을 위한 정보들이 기입됩니다.
     */
    void modifyCategory(ModifyCategoryRequestDto modifyCategoryRequestDto);

    /**
     * 카테고리에대한 단건조회를 위한 메소드입니다.
     *
     * @param categoryNo 조회할 카테고리 번호입니다.
     * @return GetCategoryResponseDto 카테고리 정보 반환됩니다.
     */
    GetCategoryResponseDto getCategory(Integer categoryNo);

    /**
     * 카테고리에 대한 전체 조회를 위한 메소드입니다.
     *
     * @return 전체 카테고리가 반환됩니다.
     */
    List<GetCategoryResponseDto> getCategories();

    /**
     * 노출 여부 true 인 카테고리에 대한 전체 조회를 위한 메소드입니다.
     *
     * @return 노출 여부 true 인 전체 카테고리가 반환됩니다.
     */
    List<GetCategoryResponseDto> getCategoriesDisplayedTrue();

    /**
     * 최상위 카테고리 조회를 위한 메소드입니다.
     *
     * @return 최상위 카테고리 정보 반환됩니다.
     */
    List<GetCategoryResponseDto> getParentCategories();
}
