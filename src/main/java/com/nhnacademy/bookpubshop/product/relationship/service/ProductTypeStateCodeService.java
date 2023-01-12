package com.nhnacademy.bookpubshop.product.relationship.service;

import com.nhnacademy.bookpubshop.product.relationship.dto.CreateProductTypeStateCodeRequestDto;
import com.nhnacademy.bookpubshop.product.relationship.dto.GetProductTypeStateCodeResponseDto;
import java.util.List;

/**
 * 상품유형코드 서비스입니다.
 *
 * @author : 여운석
 * @since : 1.0
 */
public interface ProductTypeStateCodeService {
    /**
     * 상품유형코드를 생성합니다.
     *
     * @param requestDto 생성시 필요한 dto class.
     * @return 생성된 객체를 반환합니다.
     */
    GetProductTypeStateCodeResponseDto createTypeStateCode(CreateProductTypeStateCodeRequestDto requestDto);

    /**
     * 상품유형코드를 수정합니다.
     *
     * @param codeNo 상품유형번호입니다.
     * @param requestDto 수정할 dto class.
     * @return 수정된 객체를 반환합니다.
     */
    GetProductTypeStateCodeResponseDto modifyTypeStateCode(Integer codeNo, CreateProductTypeStateCodeRequestDto requestDto);

    /**
     * 유형 번호로 조회합니다.
     *
     * @param codeNo 유형번호입니다.
     * @return 조회된 결과를 반환합니다.
     */
    GetProductTypeStateCodeResponseDto getTypeStateCodeById(Integer codeNo);

    /**
     * 모든 유형 코드를 조회합니다.
     *
     * @return 모든 유형 코드를 반환합니다.
     */
    List<GetProductTypeStateCodeResponseDto> getAllTypeStateCodes();

    /**
     * 유형 번호로 조회한 유형상태코드의 사용 여부를 설정합니다.
     *
     * @param id   유형번호입니다.
     * @param used 사용여부입니다.
     * @return 수정된 유형 코드를 반환합니다.
     */
    GetProductTypeStateCodeResponseDto setUsedTypeCodeById(Integer id, boolean used);
}
