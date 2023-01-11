package com.nhnacademy.bookpubshop.product.controller;

import com.nhnacademy.bookpubshop.product.dto.CreateProductRequestDto;
import com.nhnacademy.bookpubshop.product.dto.GetProductDetailResponseDto;
import com.nhnacademy.bookpubshop.product.dto.GetProductListResponseDto;
import com.nhnacademy.bookpubshop.product.service.ProductService;
import com.nhnacademy.bookpubshop.utils.PageResponse;
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
 * @author : 여운석
 * @since : 1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class ProductController {
    private final ProductService productService;

    /**
     * 페이징 처리 된
     * 모든 상품을 반환합니다.
     *
     * @param pageable the pageable
     * @return the all products
     */
    @GetMapping
    public ResponseEntity<PageResponse<GetProductListResponseDto>> getAllProducts(
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
     * @author : 여운석
     */
    @PostMapping
    public ResponseEntity<GetProductDetailResponseDto> createProduct(
            CreateProductRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(productService.createProduct(request));
    }

    /**
     * 상품 상세 페이지에서 상품을 보여주기 위한 메서드입니다.
     *
     * @param id 상품 번호를 파라미터로 받습니다.
     * @return 상품 정보를 반환합니다.
     */
    @GetMapping("/{id}")
    public ResponseEntity<GetProductDetailResponseDto> getProductDetailById(@PathVariable Long id) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(productService.getProductDetailById(id));
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
     * @param id 상품번호입니다.
     * @param request 수정할 내용의 상품 Dto입니다.
     * @return 성공시 200을 반환합니다.
     * @author : 여운석
     */
    @PutMapping("/{id}")
    public ResponseEntity<GetProductDetailResponseDto> modifyProduct(
            @PathVariable Long id,
            @RequestBody CreateProductRequestDto request) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(productService.modifyProduct(request, id));
    }

    /**
     * 상품 삭제 여부만을 수정합니다.
     *
     * @param id 상품 번호입니다.
     * @param deleted 삭제 여부입니다.
     * @return 성공시 200을 반환합니다.
     * @author : 여운석
     */
    @PostMapping("/{id}")
    public ResponseEntity<Void> setDeletedProduct(
            @PathVariable Long id,
            @RequestParam boolean deleted) {
        productService.setDeleteProduct(id, deleted);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(null);
    }
}
