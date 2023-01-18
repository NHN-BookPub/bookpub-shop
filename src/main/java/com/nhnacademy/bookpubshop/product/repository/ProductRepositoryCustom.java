package com.nhnacademy.bookpubshop.product.repository;

import com.nhnacademy.bookpubshop.product.dto.GetProductDetailResponseDto;
import com.nhnacademy.bookpubshop.product.dto.GetProductListForOrderResponseDto;
import com.nhnacademy.bookpubshop.product.dto.GetProductListResponseDto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 상품 레포지토리에서 쿼리 dsl 을 사용하기 위한 커스텀 레포입니다.
 *
 * @author : 여운석
 * @since : 1.0
 */
@NoRepositoryBean
public interface ProductRepositoryCustom {
    /**
     * 페이징 처리된 모든 상품을 얻습니다.
     *
     * @param pageable pageable 객체를 받습니다.
     * @return 전체 상품을 반환합니다.
     */
    Page<GetProductListResponseDto> getAllProducts(Pageable pageable);

    /**
     * 제목으로 상품을 검색합니다(like).
     *
     * @param title    제목입니다.
     * @param pageable pageable 객체를 받습니다.
     * @return 제목이 비슷한 모든 상품을 반환합니다.
     */
    Page<GetProductListResponseDto> getProductListLikeTitle(String title, Pageable pageable);

    /**
     * 상품 상세 정보를 번호로 조회합니다.
     *
     * @param id 상품번호입니다.
     * @return 상세정보 dto를 반환합니다.
     */
    Optional<GetProductDetailResponseDto> getProductDetailById(Long id);

    List<GetProductListForOrderResponseDto> getProductListByOrderNo(Long orderNo);
}
