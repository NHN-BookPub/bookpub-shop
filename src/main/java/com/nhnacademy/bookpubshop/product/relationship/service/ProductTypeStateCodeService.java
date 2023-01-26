package com.nhnacademy.bookpubshop.product.relationship.service;

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

    /**
     * 사용중인 유형상태코드 전체를 조회합니다.
     *
     * @return 사용중인 유형코드
     */
    List<GetProductTypeStateCodeResponseDto> getAllTypeStateCodesUsed();
}
