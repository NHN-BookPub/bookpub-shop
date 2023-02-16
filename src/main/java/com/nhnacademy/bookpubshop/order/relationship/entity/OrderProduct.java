package com.nhnacademy.bookpubshop.order.relationship.entity;

import com.nhnacademy.bookpubshop.order.entity.BookpubOrder;
import com.nhnacademy.bookpubshop.product.entity.Product;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 주문상품(order_product) 테이블.
 *
 * @author : 박경서
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Builder
@Table(name = "order_and_product")
public class OrderProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_product_number")
    private Long orderProductNo;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_number")
    private Product product;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_number")
    private BookpubOrder order;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_product_code_number")
    private OrderProductStateCode orderProductStateCode;

    @Column(name = "order_product_amount")
    private Integer productAmount;

    @Column(name = "order_product_coupon_amount")
    private Long couponAmount;

    @NotNull
    @Column(name = "order_product_price")
    private Long productPrice;

    @Column(name = "order_product_reason_name")
    private String reasonName;

    @Column(name = "order_product_point_save")
    private Long pointSave;

    @Column(name = "order_product_exchange_reason")
    private String exchangeReason;

    /**
     * 주문상품의 상태를 변경하는 메소드 입니다.
     *
     * @param orderProductStateCode 주문상태코드.
     */
    public void modifyOrderProductState(
            OrderProductStateCode orderProductStateCode) {
        this.orderProductStateCode = orderProductStateCode;
    }

    public void updateExchangeReason(String exchangeReason) {
        this.exchangeReason = exchangeReason;
    }
}
