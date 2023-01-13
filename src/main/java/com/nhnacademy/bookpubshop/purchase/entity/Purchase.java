package com.nhnacademy.bookpubshop.purchase.entity;

import com.nhnacademy.bookpubshop.base.BaseCreateTimeEntity;
import com.nhnacademy.bookpubshop.product.entity.Product;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.*;

/**
 * 매입이력(purchase) 테이블.
 *
 * @author : 박경서
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Builder
@Table(name = "purchase")
public class Purchase extends BaseCreateTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purchase_number")
    private Long purchaseNo;

    @NotNull
    @ManyToOne()
    @JoinColumn(name = "product_number")
    private Product product;

    @NotNull
    @Column(name = "purchase_price")
    private Long purchasePrice;

    @NotNull
    @Column(name = "purchase_amount")
    private Integer purchaseAmount;
}
