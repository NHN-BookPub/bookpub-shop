package com.nhnacademy.bookpubshop.paymenttypestatecode.entity;

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
 * 결제유형상태코드(payment_type_state_code) 테이블.
 *
 * @author : 김서현
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "payment_type_state_code")
public class PaymentTypeStateCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_type_state_code_number", nullable = false)
    private Integer codeNo;

    @NotNull
    @Column(name = "payment_type_state_code_name", unique = true)
    private String codeName;

    @Column(name = "payment_type_state_code_used", nullable = false)
    private boolean codeUsed;

    @NotNull
    @Column(name = "payment_type_state_code_info")
    private String codeInfo;

}
