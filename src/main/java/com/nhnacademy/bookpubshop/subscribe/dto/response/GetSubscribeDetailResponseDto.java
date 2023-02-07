package com.nhnacademy.bookpubshop.subscribe.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 구독의 상세정보들을 받아오기위한 클래스입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
@Getter
@Setter
public class GetSubscribeDetailResponseDto {
//    private Long subscribeNo;
//    private String subscribeName;
//    private Long price;
//    private Long salePrice;
//    private Integer salesRate;
//    private Long viewCnt;
//    private boolean deleted;
//    private boolean renewed;
//    private String imagePath;
//
//    private Set<SubscribeProduct> subscribeProducts = new LinkedHashSet<>();
//
//    /**
//     * Instantiates a new Get subscribe detail response dto.
//     *
//     * @param subscribeNo   the subscribe no
//     * @param subscribeName the subscribe name
//     * @param price         the price
//     * @param salePrice     the sale price
//     * @param salesRate     the sales rate
//     * @param viewCnt       the view cnt
//     * @param deleted       the deleted
//     * @param renewed       the renewed
//     * @param imagePath     the image path
//     * @param products      the products
//     */
//    @QueryProjection
//    public GetSubscribeDetailResponseDto(Long subscribeNo, String subscribeName,
//                                         Long price, Long salePrice,
//                                         Integer salesRate, Long viewCnt,
//                                         boolean deleted, boolean renewed,
//                                         String imagePath,
//                                         Set<SubscribeProduct> products) {
//        this.subscribeNo = subscribeNo;
//        this.subscribeName = subscribeName;
//        this.price = price;
//        this.salePrice = salePrice;
//        this.salesRate = salesRate;
//        this.viewCnt = viewCnt;
//        this.deleted = deleted;
//        this.renewed = renewed;
//        this.imagePath = imagePath;
//        subscribeProducts = products;
//    }
//
//    /**
//     * The type Subscribe product.
//     */
//    @Getter
//    @Setter
//    @EqualsAndHashCode
//    public static class SubscribeProduct {
//        private Long productNo;
//        private String title;
//        private String thumbnail;
//
//        /**
//         * Instantiates a new Subscribe product.
//         *
//         * @param productNo the product no
//         * @param title     the title
//         * @param thumbnail the thumbnail
//         */
//        @QueryProjection
//        public SubscribeProduct(Long productNo, String title,
//                                String thumbnail) {
//            this.productNo = productNo;
//            this.title = title;
//            this.thumbnail = thumbnail;
//        }
//    }
}
