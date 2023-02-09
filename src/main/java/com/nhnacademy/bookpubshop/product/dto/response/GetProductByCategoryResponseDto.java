package com.nhnacademy.bookpubshop.product.dto.response;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 카테고리로 상품을 조회하는 dto.
 *
 * @author : 박경서
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
public class GetProductByCategoryResponseDto {
    private Long productNo;
    private String title;
    private String thumbnail;
    private String ebook;
    private Long salesPrice;
    private Integer salesRate;
    private List<String> categories = new ArrayList<>();
    private List<String> authors = new ArrayList<>();

    /**
     * 카테고리 세팅.
     *
     * @param categories 카테고리들
     */
    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    /**
     * 저자값 세팅.
     *
     * @param authors 저자들
     */
    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    /**
     * 썸네일 세팅.
     *
     * @param thumbnail 썸네일
     */
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
