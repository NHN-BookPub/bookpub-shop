package com.nhnacademy.bookpubshop.subscribe.relationship.entity;

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
 * 구독상태코드(order_subscribe_state_code) 테이블.
 *
 * @author : 박경서
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "order_subscribe_state_code")
public class OrderSubscribeStateCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_subscribe_state_code_number", nullable = false, unique = true)
    private Integer codeNo;

    @Column(name = "order_subscribe_state_code_name", nullable = false, unique = true)
    private String codeName;

    @Column(name = "order_subscribe_state_used", nullable = false)
    private boolean codeUsed;

    @Column(name = "order_subscribe_state_code_info")
    private String codeInfo;
}
