package com.nhnacademy.bookpubshop.product.controller;

import com.nhnacademy.bookpubshop.annotation.AdminAuth;
import com.nhnacademy.bookpubshop.annotation.MemberAndAuth;
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
import com.nhnacademy.bookpubshop.product.service.ProductService;
import com.nhnacademy.bookpubshop.utils.PageResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 상품을 다루는 컨트롤러입니다.
 *
 * @author : 여운석, 박경서
 * @since : 1.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * 페이징 처리 된
     * 모든 상품을 반환합니다.
     *
     * @param pageable pageable 객체를 받습니다.
     * @return 모든 상품을 반환합니다.
     */
    @GetMapping("/api/products")
    public ResponseEntity<PageResponse<GetProductListResponseDto>> productList(
            Pageable pageable) {
        Page<GetProductListResponseDto> content =
                productService.getAllProducts(pageable);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new PageResponse<>(content));
    }

    /**
     * 상품을 생성합니다.
     *
     * @param requestDto 상품을 생성하기 위한 Dto 클래스.
     * @return 상품상세정보가 담긴 클래스를 반환합니다. 성공시 Created 반환합니다.
     */
    @PostMapping("/token/products")
    @AdminAuth
    public ResponseEntity<Void> productAdd(
            @Valid @RequestPart CreateProductRequestDto requestDto,
            @RequestPart(required = false) MultipartFile thumbnail,
            @RequestPart(required = false) MultipartFile detail,
            @RequestPart(required = false) MultipartFile ebook)
            throws IOException {

        Map<String, MultipartFile> files = new HashMap<>();
        if (thumbnail != null) {
            files.put("thumbnail", thumbnail);
        }
        if (detail != null) {
            files.put("detail", detail);
        }
        if (ebook != null) {
            files.put("ebook", ebook);
        }

        productService.createProduct(requestDto, files);

        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    /**
     * 상품 상세 페이지에서 상품을 보여주기 위한 메서드입니다.
     *
     * @param productNo 상품 번호를 파라미터로 받습니다.
     * @return 상품 정보를 반환합니다.
     */
    @GetMapping("/api/products/{productNo}")
    public ResponseEntity<GetProductDetailResponseDto> getProductDetailById(
            @PathVariable Long productNo) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(productService.getProductDetailById(productNo));
    }

    /**
     * Like 함수를 이용한
     * 제목을 기준으로 상품들을 반환합니다.
     *
     * @param title    상품 제목입니다.
     * @param pageable 페이징을 위한 객체입니다.
     * @return 상품리스트가 담겨있습니다.
     */
    @GetMapping("/api/products/search")
    public ResponseEntity<PageResponse<GetProductListResponseDto>> getProductLikeTitle(
            @RequestParam String title, Pageable pageable) {
        Page<GetProductListResponseDto> content =
                productService.getProductListLikeTitle(title, pageable);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new PageResponse<>(content));
    }

    /**
     * 상품을 수정합니다.
     *
     * @param productNo 상품번호입니다.
     * @param request   수정할 내용의 상품 Dto입니다.
     * @return 성공시 201을 반환합니다.
     */
    @PutMapping("/token/products/{productNo}")
    @AdminAuth
    public ResponseEntity<Void> modifyProduct(
            @PathVariable(name = "productNo") Long productNo,
            @Valid @RequestBody CreateProductRequestDto request) {
        productService.modifyProduct(request, productNo);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .build();
    }

    /**
     * 상품 삭제 여부만을 수정합니다.
     *
     * @param productNo 상품 번호입니다.
     * @return 성공시 201을 반환합니다.
     */
    @DeleteMapping("/token/products/{productNo}")
    @AdminAuth
    public ResponseEntity<Void> setDeletedProduct(
            @PathVariable Long productNo) {
        productService.setDeleteProduct(productNo);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .build();
    }

    /**
     * 상품 유형 번호를 가지고 상품 리스트 조회.(개수)
     *
     * @param typeNo 유형 번호
     * @param limit  제한 갯수
     * @return 상품 유형별 리스트
     */
    @GetMapping("/api/products/types/{typeNo}")
    public ResponseEntity<List<GetProductByTypeResponseDto>> getProductsByType(
            @PathVariable Integer typeNo,
            @RequestParam(name = "limit") Integer limit) {

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(productService.getProductsByType(typeNo, limit));
    }

    /**
     * 상품 유형 번호를 가지고 상품 리스트 조회. (전체)
     *
     * @param typeNo   유형 번호
     * @param pageable 페이징
     * @return 상품 유형별 리스트
     */
    @GetMapping("/api/products/type/{typeNo}")
    public ResponseEntity<PageResponse<GetProductByCategoryResponseDto>> getProductsByType(
            @PathVariable Integer typeNo, @PageableDefault Pageable pageable) {

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new PageResponse<>(productService.getProductsByTypes(typeNo, pageable)));
    }

    /**
     * 카트에 담긴 상품들만 조회.
     *
     * @param productsNo 카트에 담긴 상품들 번호
     * @return 카트에 담긴 상품들 정보
     */
    @GetMapping("/api/products/cart")
    public ResponseEntity<List<GetProductDetailResponseDto>> getProductInCart(
            @RequestParam(name = "productNo") List<Long> productsNo) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(productService.getProductsInCart(productsNo));
    }

    /**
     * 상품의 카테고리 번호를 통해 상품들 조회.
     *
     * @param categoryNo 카테고리 번호
     * @param pageable   페이징 정보
     * @return 페이징 정보를 담은 상품들
     */
    @GetMapping("/api/products/product/categories/{categoryNo}")
    public ResponseEntity<PageResponse<GetProductByCategoryResponseDto>> getProductsByCategory(
            @PathVariable("categoryNo") Integer categoryNo, @PageableDefault Pageable pageable) {
        Page<GetProductByCategoryResponseDto> content =
                productService.getProductsByCategory(categoryNo, pageable);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new PageResponse<>(content));
    }

    /**
     * 모든 ebook 상품들 조회.
     *
     * @param pageable 페이징
     * @return 성공시 200, 페이징된 상품들 반환.
     */
    @GetMapping("/api/products/ebooks")
    public ResponseEntity<PageResponse<GetProductByCategoryResponseDto>> getEbooks(
            @PageableDefault Pageable pageable) {
        Page<GetProductByCategoryResponseDto> content = productService.getEbooks(pageable);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new PageResponse<>(content));
    }

    /**
     * 멤버가 구매한 이북이 반환됩니다.
     *
     * @param pageable 페이징
     * @param memberNo 멤버번호
     * @return 200 코드
     */
    @GetMapping("/token/product/{memberNo}/ebooks")
    @MemberAndAuth
    public ResponseEntity<PageResponse<GetProductByCategoryResponseDto>> getEbooks(
            @PageableDefault Pageable pageable, @PathVariable Long memberNo) {
        Page<GetProductByCategoryResponseDto> content =
                productService.getEbooksByMember(pageable, memberNo);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new PageResponse<>(content));
    }


    /**
     * 상품 정보 수정 API.
     *
     * @param productNo 상품 번호
     * @param request   상품 수정 정보
     * @return 201 코드
     */
    @AdminAuth
    @PutMapping("/token/products/{productNo}/info")
    public ResponseEntity<Void> modifyProductInfo(@PathVariable("productNo") Long productNo,
                                                  @Valid @RequestBody
                                                  ModifyProductInfoRequestDto request) {
        productService.modifyProductInfo(productNo, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    /**
     * 상품 카테고리 수정 API.
     *
     * @param productNo 상품 번호
     * @param request   카테고리 수정 정보
     * @return 201 코드
     */
    @AdminAuth
    @PutMapping("/token/products/{productNo}/category")
    public ResponseEntity<Void> modifyProductCategory(@PathVariable("productNo") Long productNo,
                                                      @Valid @RequestBody
                                                      ModifyProductCategoryRequestDto request) {
        productService.modifyProductCategory(productNo, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    /**
     * 상품 저자 수정 API.
     *
     * @param productNo 상품 번호
     * @param request   상품 저자 정보
     * @return 201 코드
     */
    @AdminAuth
    @PutMapping("/token/products/{productNo}/author")
    public ResponseEntity<Void> modifyProductAuthor(@PathVariable("productNo") Long productNo,
                                                    @Valid @RequestBody
                                                    ModifyProductAuthorRequestDto request) {
        productService.modifyProductAuthor(productNo, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    /**
     * 상품 태그 수정 API.
     *
     * @param productNo 상품 번호
     * @param request   상품 태그 정보
     * @return 201 코드
     */
    @AdminAuth
    @PutMapping("/token/products/{productNo}/tag")
    public ResponseEntity<Void> modifyProductTag(@PathVariable("productNo") Long productNo,
                                                 @Valid @RequestBody
                                                 ModifyProductTagRequestDto request) {
        productService.modifyProductTag(productNo, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    /**
     * 상품 유형 수정 API.
     *
     * @param productNo   상품 번호
     * @param typeStateNo 상품 유형 번호
     * @return 201 코드
     */
    @AdminAuth
    @PutMapping("/token/products/{productNo}/type")
    public ResponseEntity<Void> modifyProductType(@PathVariable("productNo") Long productNo,
                                                  @RequestParam("no") Integer typeStateNo) {
        productService.modifyProductType(productNo, typeStateNo);

        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    /**
     * 상품 판매 유형 수정 API.
     *
     * @param productNo   상품 번호
     * @param saleStateNo 상품 판매 유형 번호
     * @return 201 코드
     */
    @AdminAuth
    @PutMapping("/token/products/{productNo}/sale")
    public ResponseEntity<Void> modifyProductSale(@PathVariable("productNo") Long productNo,
                                                  @RequestParam("no") Integer saleStateNo) {
        productService.modifyProductSale(productNo, saleStateNo);

        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    /**
     * 상품 포인트 정책 수정 API.
     *
     * @param productNo 상품 번호
     * @param policyNo  포인트 정책 번호
     * @return 201코드
     */
    @AdminAuth
    @PutMapping("/token/products/{productNo}/policy")
    public ResponseEntity<Void> modifyProductPolicy(@PathVariable("productNo") Long productNo,
                                                    @RequestParam("no") Integer policyNo) {
        productService.modifyProductPolicy(productNo, policyNo);

        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    /**
     * E-Book 파일 다운로드 API.
     *
     * @param productNo 상품 번호
     * @return 200 코드
     */
    @MemberAndAuth
    @GetMapping("/token/downloads/e-book/{productNo}/{memberNo}")
    public ResponseEntity<GetDownloadInfo> ebookDownload(
            @PathVariable("productNo") Long productNo,
            @PathVariable("memberNo") Long memberNo) {
        GetDownloadInfo ebookInfo = productService.getEbookInfo(productNo);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(ebookInfo);
    }

    /**
     * 상품 설명 수정 API.
     *
     * @param productNo 상품 번호
     * @param request   상품 설명
     * @return 201 코드
     */
    @AdminAuth
    @PutMapping("/token/products/{productNo}/description")
    public ResponseEntity<Void> modifyProductDescription(
            @PathVariable("productNo") Long productNo,
            @Valid @RequestBody ModifyProductDescriptionRequestDto request) {
        productService.modifyProductDescription(productNo, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    /**
     * E-Book File 변경 API.
     *
     * @param productNo 상품 번호
     * @param ebook     E-Book
     * @return 201 코드
     */
    @AdminAuth
    @PutMapping("/token/products/{productNo}/e-book")
    public ResponseEntity<Void> modifyProductEbook(@PathVariable("productNo") Long productNo,
                                                   @RequestPart MultipartFile ebook) {
        productService.modifyProductEbook(productNo, ebook);

        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    /**
     * 썸네일 Image File 변경 API.
     *
     * @param productNo 상품 번호
     * @param image     Image
     * @return 201 코드
     */
    @AdminAuth
    @PutMapping("/token/products/{productNo}/thumbnail")
    public ResponseEntity<Void> modifyProductImage(@PathVariable("productNo") Long productNo,
                                                   @RequestPart("image") MultipartFile image) {
        productService.modifyProductImage(productNo, image);

        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    /**
     * 상품 상세 File 이미지 변경 API.
     *
     * @param productNo   상품 번호
     * @param detailImage Image
     * @return 201 코드
     */
    @AdminAuth
    @PutMapping("/token/products/{productNo}/detail-image")
    public ResponseEntity<Void> modifyProductDetailImage(@PathVariable("productNo") Long productNo,
                                                         @RequestPart MultipartFile detailImage) {
        productService.modifyProductDetailImage(productNo, detailImage);

        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    /**
     * 상품 Image 추가 API.
     *
     * @param productNo 상품 번호
     * @param image     Image
     * @return 201 코드
     */
    @AdminAuth
    @PutMapping("/token/products/{productNo}/new-thumbnail")
    public ResponseEntity<Void> addProductImage(@PathVariable("productNo") Long productNo,
                                                @RequestPart("image") MultipartFile image) {
        productService.addProductImage(productNo, image);

        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    /**
     * 상품 상세 File 이미지 추가 API.
     *
     * @param productNo   상품 번호
     * @param detailImage Image
     * @return 201 코드
     */
    @AdminAuth
    @PutMapping("/token/products/{productNo}/new-detail-image")
    public ResponseEntity<Void> addProductDetailImage(@PathVariable("productNo") Long productNo,
                                                      @RequestPart MultipartFile detailImage) {
        productService.addProductDetailImage(productNo, detailImage);

        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    /**
     * 연관관계 상품을 등록하는 API.
     *
     * @param productNo 상품 번호
     * @param request   연관관계 상품들
     * @return 201 코드
     */
    @AdminAuth
    @PutMapping("/token/products/{productNo}/relation")
    public ResponseEntity<Void> addRelationProduct(
            @PathVariable("productNo") Long productNo,
            @Valid @RequestBody CreateRelationProductRequestDto request) {
        productService.addRelationProduct(productNo, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    /**
     * 연관관계 삭제 API.
     *
     * @param childNo 자식 상풍 번호
     * @return 200 코드
     */
    @AdminAuth
    @DeleteMapping("/token/products/{childNo}/relation")
    public ResponseEntity<Void> disconnectRelation(@PathVariable("childNo") Long childNo) {
        productService.deleteRelationProduct(childNo);

        return ResponseEntity.ok()
                .build();
    }

    /**
     * ebook 구매 이력이 있는 사용자인지 확인하는 컨트롤러.
     *
     * @param memberNo 회원번호.
     * @param productNo 상품번호.
     * @return boolean.
     */
    @GetMapping("/api/products/{productNo}/{memberNo}")
    public ResponseEntity<Boolean> isPurchaseUser(@PathVariable String memberNo,
                                                  @PathVariable String productNo) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(productService.isPurchaseUser(memberNo, productNo));
    }
}
