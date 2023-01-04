package com.nhnacademy.bookpubshop.order.relationship.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 주문상품상태코드(order_product_state_code) 테이블.
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
public class OrderProductStateCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_product_state_code_number", nullable = false, unique = true)
    private Integer codeNo;

    @Column(name = "order_product_state_code_name", nullable = false, unique = true)
    private String codeName;

    @Column(name = "order_product_state_code_used", nullable = false)
    private boolean codeUsed;

    @Column(name = "order_product_state_code_info")
    private String codeInfo;
}
