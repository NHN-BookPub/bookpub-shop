package com.nhnacademy.bookpubshop.product.dto.response;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Some description here.
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
