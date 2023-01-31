package com.nhnacademy.bookpubshop.product.controller;

import com.nhnacademy.bookpubshop.product.dto.request.CreateProductRequestDto;
import com.nhnacademy.bookpubshop.product.dto.response.GetProductByCategoryResponseDto;
import com.nhnacademy.bookpubshop.product.dto.response.GetProductByTypeResponseDto;
import com.nhnacademy.bookpubshop.product.dto.response.GetProductDetailResponseDto;
import com.nhnacademy.bookpubshop.product.dto.response.GetProductListResponseDto;
import com.nhnacademy.bookpubshop.product.service.ProductService;
import com.nhnacademy.bookpubshop.utils.PageResponse;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 상품을 다루는 컨트롤러입니다.
 *
 * @author : 여운석, 박경서
 * @since : 1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    /**
     * 페이징 처리 된
     * 모든 상품을 반환합니다.
     *
     * @param pageable pageable 객체를 받습니다.
     * @return 모든 상품을 반환합니다.
     */
    @GetMapping
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
     * @param request 상품을 생성하기 위한 Dto 클래스.
     * @return 상품상세정보가 담긴 클래스를 반환합니다. 성공시 Created 반환합니다.
     */
    @PostMapping
    public ResponseEntity<Void> productAdd(
            @Valid @RequestBody CreateProductRequestDto request) {
        productService.createProduct(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(null);
    }

    /**
     * 상품 상세 페이지에서 상품을 보여주기 위한 메서드입니다.
     *
     * @param productNo 상품 번호를 파라미터로 받습니다.
     * @return 상품 정보를 반환합니다.
     */
    @GetMapping("/{productNo}")
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
    @GetMapping("/search")
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
    @PutMapping("/{productNo}")
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
     * @param id 상품 번호입니다.
     * @return 성공시 201을 반환합니다.
     */
    @PutMapping("/deleted/{id}")
    public ResponseEntity<Void> setDeletedProduct(
            @PathVariable Long id) {
        productService.setDeleteProduct(id);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .build();
    }

    /**
     * 상품 유형 번호를 가지고 상품 리스트 조회.
     *
     * @param typeNo 유형 번호
     * @param limit  제한 갯수
     * @return 상품 유형별 리스트
     */
    @GetMapping("/types/{typeNo}")
    public ResponseEntity<List<GetProductByTypeResponseDto>>
    getProductsByType(@PathVariable Integer typeNo,
                      @RequestParam(name = "limit") Integer limit) {

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(productService.getProductsByType(typeNo, limit));
    }

    /**
     * 카트에 담긴 상품들만 조회.
     *
     * @param productsNo 카트에 담긴 상품들 번호
     * @return 카트에 담긴 상품들 정보
     */
    @GetMapping("/cart")
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
    @GetMapping("/product/categories/{categoryNo}")
    public ResponseEntity<PageResponse<GetProductByCategoryResponseDto>>
    getProductsByCategory(@PathVariable("categoryNo") Integer categoryNo, Pageable pageable) {
        Page<GetProductByCategoryResponseDto> content =
                productService.getProductsByCategory(categoryNo, pageable);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new PageResponse<>(content));
    }
}
