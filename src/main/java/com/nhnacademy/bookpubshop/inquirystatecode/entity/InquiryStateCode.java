package com.nhnacademy.bookpubshop.inquirystatecode.entity;

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
 * 상품문의코드 테이블.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@Table(name = "inquiry_state_code")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class InquiryStateCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inquiry_state_code_number", nullable = false)
    private Integer inquiryCodeNo;

    @Column(name = "inquiry_state_code_name", nullable = true, unique = true)
    private String inquiryCodeName;

    @Column(name = "inquiry_state_code_used", nullable = false)
    private boolean inquiryCodeUsed;

    @Column(name = "inquiry_state_code_info", nullable = true)
    private String inquiryCodeInfo;
}
