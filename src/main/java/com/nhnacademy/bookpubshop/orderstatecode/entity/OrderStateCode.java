package com.nhnacademy.bookpubshop.orderstatecode.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
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
    @Column(name = "order_state_code_number")
    private Integer codeNo;

    @NotNull
    @Column(name = "order_state_code_name", unique = true)
    private String codeName;

    @Column(name = "order_state_code_used")
    private boolean codeUsed;

    @Column(name = "order_state_code_info")
    private String codeInfo;

    public void modifyUsed() {
        this.codeUsed = !this.codeUsed;
    }
}
