package com.nhnacademy.bookpubshop.product.dto.response;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 상품에 관한 간단한 정보들을 반환하는 Dto입니다.
 * 회원의 작성 가능한 리뷰를 보여줄 때 상품정보를 함께 보여주기 위해 사용됩니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetProductSimpleResponseDto {
    private Long productNo;
    private String title;
    private String productIsbn;
    private String productPublisher;
    private List<String> productAuthorNames = new ArrayList<>();
    private String productImagePath;

    public void setAuthorNames(List<String> authorNames) {
        this.productAuthorNames = authorNames;
    }
}
