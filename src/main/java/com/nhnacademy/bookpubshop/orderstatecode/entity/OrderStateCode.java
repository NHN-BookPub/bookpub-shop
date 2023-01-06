package com.nhnacademy.bookpubshop.orderstatecode.entity;

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

/**
 * 주문상태코드(order_state_code) 테이블.
 *
 * @author : 김서현
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "order_state_code")
public class OrderStateCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_state_code_number", nullable = false)
    private Integer codeNo;

    @Column(name = "order_state_code_name", nullable = false, unique = true)
    private String codeName;

    @Column(name = "order_state_code_used", nullable = false)
    private boolean codeUsed;

    @Column(name = "order_state_code_info")
    private String codeInfo;
}
