package com.nhnacademy.bookpubshop.subscribe.dto.response;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 구독에 대한 상세내용을 받기위한 클래스입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
public class GetSubscribeDetailResponseDto {
    private Long subscribeNo;
    private String subscribeName;
    private Long price;
    private Long salePrice;
    private Integer salesRate;
    private Long viewCnt;
    private boolean deleted;
    private boolean renewed;
    private String imagePath;

    private List<GetSubscribeProductListDto> productLists = new ArrayList<>();

    /**
     * 생성자.
     *
     * @param subscribeNo   구독번호
     * @param subscribeName 구독이름
     * @param price         상품 가격
     * @param salePrice     할인 가격정보
     * @param salesRate     할인률
     * @param viewCnt       view 카운트
     * @param deleted       삭제여부
     * @param renewed       갱신여부
     * @param imagePath     이미지 경로
     */
    public GetSubscribeDetailResponseDto(Long subscribeNo, String subscribeName,
                                         Long price, Long salePrice, Integer salesRate,
                                         Long viewCnt, boolean deleted, boolean renewed,
                                         String imagePath) {
        this.subscribeNo = subscribeNo;
        this.subscribeName = subscribeName;
        this.price = price;
        this.salePrice = salePrice;
        this.salesRate = salesRate;
        this.viewCnt = viewCnt;
        this.deleted = deleted;
        this.renewed = renewed;
        this.imagePath = imagePath;
    }

    public void inputProductLists(List<GetSubscribeProductListDto> productLists) {
        this.productLists = productLists;
    }
}
