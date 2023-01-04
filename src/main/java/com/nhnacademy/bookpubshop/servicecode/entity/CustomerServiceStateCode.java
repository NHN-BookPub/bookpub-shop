package com.nhnacademy.bookpubshop.servicecode.entity;

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
 * 고객서비스 상태 코드입니다.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@Table(name = "customer_service_state_code")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CustomerServiceStateCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_service_state_code_number", nullable = false, unique = true)
    private Integer serviceCodeNo;

    @Column(name = "customer_service_state_code_name", nullable = false, unique = true)
    private String serviceCodeName;

    @Column(name = "customer_service_state_code_used", nullable = false)
    private boolean serviceCodeUsed;

    @Column(name = "customer_service_state_code_info")
    private String serviceCodeInfo;
}
