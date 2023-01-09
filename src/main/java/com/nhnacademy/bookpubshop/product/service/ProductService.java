package com.nhnacademy.bookpubshop.product.service;

import com.nhnacademy.bookpubshop.product.dto.CreateProductRequestDto;
import com.nhnacademy.bookpubshop.product.dto.GetProductDetailResponseDto;
import com.nhnacademy.bookpubshop.product.dto.GetProductListResponseDto;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * ProductRepositoru에 접근하기 위한 Service 클래스입니다.
 *
 * @author : 여운석
 * @since : 1.0
 */
@Service
public interface ProductService {
    /**
     * 상품의 번호로 상품을 조회하는 메서드입니다.
     *
     * @param id 상품번호입니다.
     * @return 조회된 상품 상세정보를 반환합니다.
     */
    GetProductDetailResponseDto getProductDetailById(Long id);

    /**
     * 상품을 생성합니다.
     *
     * @param request 상품 생성시 필요한 dto.
     * @return 생성된 상품의 상세 정보를 반환합니다.
     */
    GetProductDetailResponseDto createProduct(CreateProductRequestDto request);

    /**
     * 모든 상품을 페이징 처리하여 조회합니다. 등록기준 asc 입니다.
     *
     * @param pageable 페이징을 위한 객체
     * @return 페이징에 따라 모든 상품을 반환합니다.
     */
    List<GetProductListResponseDto> getAllProducts(Pageable pageable);

    /**
     * 제목과 비슷한 상품 리스트를 페이징하여 조회합니다.
     *
     * @param title    the title
     * @param pageable the pageable
     * @return the product list like title
     */
    List<GetProductListResponseDto> getProductListLikeTitle(String title, Pageable pageable);

    /**
     * 상품을 수정합니다.
     *
     * @param request 수정시 사용하는 dto.
     * @param id 상품 번호입니다.
     * @return 수정된 상품의 상세정보를 반환합니다.
     * @author : 여운석
     */
    GetProductDetailResponseDto modifyProduct(CreateProductRequestDto request, Long id);

    /**
     * 상품 삭제 여부를 설정합니다.
     *
     * @param id      상품번호입니다.
     * @param deleted 삭제여부입니다.
     */
    void setDeleteProduct(Long id, boolean deleted);
}
