package com.nhnacademy.bookpubshop.product.service;

import com.nhnacademy.bookpubshop.filemanager.dto.response.GetDownloadInfo;
import com.nhnacademy.bookpubshop.product.dto.request.CreateProductRequestDto;
import com.nhnacademy.bookpubshop.product.dto.request.CreateRelationProductRequestDto;
import com.nhnacademy.bookpubshop.product.dto.request.ModifyProductAuthorRequestDto;
import com.nhnacademy.bookpubshop.product.dto.request.ModifyProductCategoryRequestDto;
import com.nhnacademy.bookpubshop.product.dto.request.ModifyProductDescriptionRequestDto;
import com.nhnacademy.bookpubshop.product.dto.request.ModifyProductInfoRequestDto;
import com.nhnacademy.bookpubshop.product.dto.request.ModifyProductTagRequestDto;
import com.nhnacademy.bookpubshop.product.dto.response.GetProductByCategoryResponseDto;
import com.nhnacademy.bookpubshop.product.dto.response.GetProductByTypeResponseDto;
import com.nhnacademy.bookpubshop.product.dto.response.GetProductDetailResponseDto;
import com.nhnacademy.bookpubshop.product.dto.response.GetProductListResponseDto;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

/**
 * ProductRepositoru에 접근하기 위한 Service 클래스입니다.
 *
 * @author : 여운석, 박경서
 * @since : 1.0
 */
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
     */
    void createProduct(CreateProductRequestDto request,
                       Map<String, MultipartFile> fileMap) throws IOException;

    /**
     * 모든 상품을 페이징 처리하여 조회합니다. 등록기준 asc 입니다.
     *
     * @param pageable 페이징을 위한 객체
     * @return 페이징에 따라 모든 상품을 반환합니다.
     */
    Page<GetProductListResponseDto> getAllProducts(Pageable pageable);

    /**
     * 제목과 비슷한 상품 리스트를 페이징하여 조회합니다.
     *
     * @param title    상품 제목입니다.
     * @param pageable pageable 객체를 받습니다.
     * @return 제목이 비슷한 모든 상품을 반환합니다.
     */
    Page<GetProductListResponseDto> getProductListLikeTitle(String title, Pageable pageable);

    /**
     * 상품을 수정합니다.
     *
     * @param request 수정시 사용하는 dto.
     * @param id      상품 번호입니다.
     * @author : 여운석, 박경서
     */
    void modifyProduct(CreateProductRequestDto request, Long id);

    /**
     * 상품 삭제 여부를 설정합니다.
     *
     * @param id 상품번호입니다.
     */
    void setDeleteProduct(Long id);

    /**
     * 상품 유형 번호를 가지고 상품 리스트 조회.
     *
     * @param typeNo 유형 번호
     * @param limit  제한 갯수
     * @return 유형별 상품 리스트
     */
    List<GetProductByTypeResponseDto> getProductsByType(Integer typeNo, Integer limit);

    /**
     * 카트에 담긴 상품 번호를 가지고 상품들 조회.
     *
     * @param productsNo 카트에 담긴 상품들 번호
     * @return 카트에 담긴 상품들 정보
     */
    List<GetProductDetailResponseDto> getProductsInCart(List<Long> productsNo);

    /**
     * 카테고리별 상품들 조회.
     *
     * @param categoryNo 카테고리번호
     * @param pageable   페이징정보
     * @return 카텍리별 상품들
     */
    Page<GetProductByCategoryResponseDto> getProductsByCategory(
            Integer categoryNo, Pageable pageable);

    /**
     * 모든 ebook 상품들 조회.
     *
     * @param pageable 페이징
     * @return 상품이 담긴 페이지 객체
     */
    Page<GetProductByCategoryResponseDto> getEbooks(Pageable pageable);

    /**
     * 멤버가 구매한 모든 이북이 반환됩니다.
     *
     * @param pageable 페이징
     * @param memberNo 멤버번호
     * @return 이북리스트
     */
    Page<GetProductByCategoryResponseDto> getEbooksByMember(Pageable pageable, Long memberNo);

    /**
     * 상품 정보를 수정.
     *
     * @param productNo 상품 번호
     * @param request   수정할 상품 정보
     */
    void modifyProductInfo(Long productNo, ModifyProductInfoRequestDto request);

    /**
     * 상품 카테고리를 수정.
     *
     * @param productNo 상품 번호
     * @param request   수정할 카테고리 번호
     */
    void modifyProductCategory(Long productNo, ModifyProductCategoryRequestDto request);

    /**
     * 상품 저자를 수정.
     *
     * @param productNo 상품 번호
     * @param request   수정할 저자 번호
     */
    void modifyProductAuthor(Long productNo, ModifyProductAuthorRequestDto request);

    /**
     * 상품 태그 수정.
     *
     * @param productNo 상품 번호
     * @param request   수정할 태그 번호
     */
    void modifyProductTag(Long productNo, ModifyProductTagRequestDto request);

    /**
     * 상품 유형 수정.
     *
     * @param productNo   상품 번호
     * @param typeStateNo 상품 유형 번호
     */
    void modifyProductType(Long productNo, Integer typeStateNo);

    /**
     * 상품 판매 유형 수정.
     *
     * @param productNo   상품 번호
     * @param saleStateNo 상품 한매 유형 번호
     */
    void modifyProductSale(Long productNo, Integer saleStateNo);

    /**
     * 상품 포인트 정책 수정.
     *
     * @param productNo 상품 번호
     * @param policyNo  포인트 정책 번호
     */
    void modifyProductPolicy(Long productNo, Integer policyNo);

    /**
     * Ebook 파일 정보를 get.
     *
     * @param productNo 상품 번호
     * @return E-Book 정보
     */
    GetDownloadInfo getEbookInfo(Long productNo);

    /**
     * 상품 설명 수정.
     *
     * @param productNo 상품 번호
     * @param request   상품 설명 정보
     */
    void modifyProductDescription(Long productNo, ModifyProductDescriptionRequestDto request);

    /**
     * E-Book 파일 수정.
     *
     * @param productNo 상품 번호
     * @param ebook     변경할 E-Book
     */
    void modifyProductEbook(Long productNo, MultipartFile ebook);

    /**
     * Image 파일 수정.
     *
     * @param productNo 상품 번호
     * @param image     변경할 Image
     */
    void modifyProductImage(Long productNo, MultipartFile image);

    /**
     * Detail Image 수정.
     *
     * @param productNo 상품 번호
     * @param detail    변경할 Detail Image
     */
    void modifyProductDetailImage(Long productNo, MultipartFile detail);

    /**
     * 상품 Image 추가.
     *
     * @param productNo 상품 번호
     * @param image     추가할 상품 Image
     */
    void addProductImage(Long productNo, MultipartFile image);

    /**
     * 상품 상세 Detail Image 추가.
     *
     * @param productNo   상품 번호
     * @param detailImage 추가할 상품 Detail Image
     */
    void addProductDetailImage(Long productNo, MultipartFile detailImage);

    /**
     * 연관 관계 등록 추가.
     *
     * @param productNo 상품 번호
     * @param request   연관 관계 상품 번
     */
    void addRelationProduct(Long productNo, CreateRelationProductRequestDto request);

    /**
     * 연관 관계 삭제 메서드.
     *
     * @param childNo 자식 상품 번호
     */
    void deleteRelationProduct(Long childNo);

    /**
     * 상품 유형 번호를 가지고 상품 리스트 조회. (전체)
     *
     * @param typeNo 유형 번호
     * @param pageable 페이징
     * @return 상품 유형별 리스트
     */
    Page<GetProductByCategoryResponseDto> getProductsByTypes(Integer typeNo, Pageable pageable);

    /**
     * ebook 구매 이력이 있는지 없는지 확인.
     *
     * @param memberNo 회원번호.
     * @param productNo 상품번호.
     * @return boolean.
     */
    boolean isPurchaseUser(String memberNo, String productNo);
}
