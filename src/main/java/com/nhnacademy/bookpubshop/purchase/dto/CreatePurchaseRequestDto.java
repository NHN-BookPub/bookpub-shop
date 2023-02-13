package com.nhnacademy.bookpubshop.purchase.dto;

import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.purchase.entity.Purchase;
import lombok.Getter;
import lombok.NoArgsConstructor;


/**
 * 구매이력 저장시 사용하는 dto class.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
public class CreatePurchaseRequestDto {
    private Long productNo;
    private Long purchasePrice;
    private Integer purchaseAmount;
    private Integer productType;

    /**
     * Dto to Entity 메서드입니다.
     *
     * @param product product를 인자로 받습니다.
     * @return Purchase 엔티티를 반환합니다.
     */
    public Purchase toEntity(Product product) {
        return Purchase.builder()
                .purchaseNo(null)
                .purchasePrice(purchasePrice)
                .purchaseAmount(purchaseAmount)
                .product(product)
                .build();
    }
}
