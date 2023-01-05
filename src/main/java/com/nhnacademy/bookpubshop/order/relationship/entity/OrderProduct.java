package com.nhnacademy.bookpubshop.order.relationship.entity;

import com.nhnacademy.bookpubshop.product.entity.Product;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "order_and_product")
public class OrderProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_product_number", nullable = false, unique = true)
    private Long orderProductNo;

    @ManyToOne
    @JoinColumn(name = "product_number", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn
    private Order order;

    @ManyToOne
    @JoinColumn(name = "order_product_code_number", nullable = false)
    private OrderProductStateCode orderProductStateCode;

    @Column(name = "order_product_amount", nullable = false)
    private Integer productAmount;

    @Column(name = "order_product_coupon_amount")
    private Long couponAmount;

    @Column(name = "order_product_price", nullable = false)
    private Long productPrice;

    @Column(name = "order_product_reason_name")
    private String reasonName;
}
