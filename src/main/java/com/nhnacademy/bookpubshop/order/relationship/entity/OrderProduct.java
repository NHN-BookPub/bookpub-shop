package com.nhnacademy.bookpubshop.order.relationship.entity;

import com.nhnacademy.bookpubshop.product.entity.Product;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 주문상품(order_product_state_code) 테이블.
 *
 * @author : 박경서
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@Entity
@Table(name = "order_product_state_code")
public class OrderProduct {
    @Id
    @Column(name = "order_product_number")
    private Long orderProductNo;

    //Todo 주문 번호 컬럼 조인 필요.

    @ManyToOne
    @JoinColumn(name = "product_number")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "order_product_code_number")
    private OrderProductStateCode orderProductStateCode;

    @Column(name = "order_product_amount")
    private Integer productAmount;

    @Column(name = "order_product_coupon_amount")
    private Long couponAmount;

    @Column(name = "order_product_price")
    private Long productPrice;

    @Column(name = "order_product_reason_name")
    private String reasonName;
}
