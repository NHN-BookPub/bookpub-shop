package com.nhnacademy.bookpubshop.order.dto.response;

import java.util.ArrayList;
import java.util.List;

/**
 * 마이페이지에서 이북 리스트를 보여주기 위한 dto 입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
public class GetEbooksResponseDto {
    private Long productNo;
    private String title;
    private String thumbnail;
    private Long salesPrice;
    private Integer salesRate;
    private String ebook;
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
