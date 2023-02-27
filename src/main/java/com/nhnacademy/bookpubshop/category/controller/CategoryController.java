package com.nhnacademy.bookpubshop.category.controller;

import com.nhnacademy.bookpubshop.annotation.AdminAuth;
import com.nhnacademy.bookpubshop.category.dto.request.CreateCategoryRequestDto;
import com.nhnacademy.bookpubshop.category.dto.request.ModifyCategoryRequestDto;
import com.nhnacademy.bookpubshop.category.dto.response.GetCategoryInfoResponseDto;
import com.nhnacademy.bookpubshop.category.dto.response.GetCategoryResponseDto;
import com.nhnacademy.bookpubshop.category.dto.response.GetParentCategoryWithChildrenResponseDto;
import com.nhnacademy.bookpubshop.category.service.CategoryService;
import com.nhnacademy.bookpubshop.utils.PageResponse;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 카테고리 RestController 입니다.
 *
 * @author : 김서현
 * @since : 1.0
 **/
@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * 카테고리 등록 요청 시 사용되는 메소드입니다.
     *
     * @param createCategoryRequestDto 카테고리 생성 위한 값.
     * @return 성공했을 때 응답코드 CREATED 201 반환.
     */
    @AdminAuth
    @PostMapping("/token/categories")
    public ResponseEntity<Void> categoryAdd(
            @Valid @RequestBody CreateCategoryRequestDto createCategoryRequestDto) {
        categoryService.addCategory(createCategoryRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    /**
     * 카테고리 수정을 요청 시 사용되는 메소드입니다.
     *
     * @param modifyCategoryRequestDto 카테고리 수정을 위한 값.
     * @return 성공했을 때 응답코드 CREATED 201이 반환.
     */
    @AdminAuth
    @PutMapping("/token/categories")
    public ResponseEntity<Void> categoryModify(
            @Valid @RequestBody ModifyCategoryRequestDto modifyCategoryRequestDto) {
        categoryService.modifyCategory(modifyCategoryRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .build();
    }

    /**
     * 카테고리 단건 조회시 사용되는 메소드입니다.
     *
     * @param categoryNo 카테고리 단건 조회를 위한 값.
     * @return 성공했을 때 응답코드 OK 200이 반환.
     */
    @GetMapping("/api/categories/{categoryNo}")
    public ResponseEntity<GetCategoryResponseDto> categoryDetail(@PathVariable Integer categoryNo) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(categoryService.getCategory(categoryNo));
    }

    /**
     * 최상위 카테고리 조회시 사용되는 메소드입니다.
     *
     * @return 최상위 카테고리 리스트 반환.
     */
    @GetMapping("/api/categories/parent")
    public ResponseEntity<List<GetCategoryResponseDto>> parentCategoryList() {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(categoryService.getParentCategories());
    }

    /**
     * 카테고리 다건 조회시 사용되는 메소드입니다.
     *
     * @return 성공했을 때 응답코드 OK 200이 반환.
     */
    @GetMapping("/api/categories")
    public ResponseEntity<PageResponse<GetCategoryResponseDto>> categoryList(Pageable pageable) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new PageResponse<>(categoryService.getCategories(pageable)));
    }

    /**
     * 메인에서 상위, 하위 카테고리 조회시 사용되는 메소드입니다.
     *
     * @return 성공했을 때 응답코드 OK 200이 반환.
     */
    @GetMapping("/api/categories/parent-child")
    public ResponseEntity<List<GetParentCategoryWithChildrenResponseDto>> parentWithChildList() {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(categoryService.getParentCategoryWithChildren());
    }

    /**
     * 카테고리 리스트 조회.
     *
     * @return 카테고리 반환.
     */
    @GetMapping("/api/category-list")
    public ResponseEntity<List<GetCategoryInfoResponseDto>> getCategories() {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(categoryService.getAllCategories());
    }
}
