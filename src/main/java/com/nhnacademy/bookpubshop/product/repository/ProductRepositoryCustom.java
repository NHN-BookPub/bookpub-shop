package com.nhnacademy.bookpubshop.product.repository;

import com.nhnacademy.bookpubshop.product.dto.response.GetProductByCategoryResponseDto;
import com.nhnacademy.bookpubshop.product.dto.response.GetProductByTypeResponseDto;
import com.nhnacademy.bookpubshop.product.dto.response.GetProductDetailResponseDto;
import com.nhnacademy.bookpubshop.product.dto.response.GetProductListForOrderResponseDto;
import com.nhnacademy.bookpubshop.product.dto.response.GetProductListResponseDto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 상품 레포지토리에서 쿼리 dsl 을 사용하기 위한 커스텀 레포입니다.
 *
 * @author : 여운석, 박경서
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

    /**
     * 상품 타입과 제한 갯수를 가지고 상품을 조회.
     *
     * @param typeNo 타입 번호
     * @param limit  제한 갯수
     * @return 타입을 기준으로 상품 반환
     */
    List<GetProductByTypeResponseDto> findProductListByType(Integer typeNo, Integer limit);

    /**
     * 주문번호로 상품리스트를 조회합니다.
     *
     * @param orderNo 주문번호입니다.
     * @return 주문에서 보여질 상품 Dto를 반환합니다.
     */
    List<GetProductListForOrderResponseDto> getProductListByOrderNo(Long orderNo);

    /**
     * 카트에 담긴 상품들 조회.
     *
     * @param productsNo 카트에 담긴 상품들 번호
     * @return 카트에 담긴 상품들 정보
     */
    List<GetProductDetailResponseDto> getProductsInCart(List<Long> productsNo);

    /**
     * 카테고리별 상품들 조회.
     *
     * @param categoryNo 카테고리 번호
     * @param pageable   페이징 정보
     * @return 페이징을 담은 상품들
     */
    Page<GetProductByCategoryResponseDto> getProductsByCategory(
            Integer categoryNo, Pageable pageable);

    /**
     * 모든 Ebook 상품들을 반환합니다.
     *
     * @param pageable 페이징 정보
     * @return 페이징 상품들
     */
    Page<GetProductByCategoryResponseDto> getEbooks(Pageable pageable);

    /**
     * 회원이 구매한 이북들을 조회합니다.
     *
     * @param pageable 페이징
     * @param memberNo 회원번호
     * @return 이북
     */
    Page<GetProductByCategoryResponseDto> getEbooksByMember(Pageable pageable, Long memberNo);

    /**
     * E-book 파일 경로 조회.
     *
     * @param productNo 상품 번호
     * @return E-Book 파일 경로
     */
    String getFilePath(Long productNo);

    /**
     * 상품유형으로 상품들을 조회합니다.
     *
     * @param typeNo 유형번호
     * @param pageable 페이징
     * @return 상품
     */
    Page<GetProductByCategoryResponseDto> getProductsByTypes(Integer typeNo, Pageable pageable);

    /**
     * 상품을 구매한 적 있는 유저인지 확인 메소드.
     *
     * @param memberNo 회원번호.
     * @param productNo 상품번호.
     * @return 구매했나 안했나.
     */
    boolean isPurchaseUser(String memberNo, String productNo);

}
