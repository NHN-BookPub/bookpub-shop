package com.nhnacademy.bookpubshop.product.dto.response;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 상품 타입을 가지고 상품을 받는 DTO.
 *
 * @author : 박경서
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
public class GetProductByTypeResponseDto {
    private Long productNo;
    private String title;
    private String thumbnail;
    private Long salesPrice;
    private Long productPrice;
    private Integer salesRate;
    private final List<String> productCategories = new ArrayList<>();
}
