package com.nhnacademy.bookpubshop.customersupport.entity;

import com.nhnacademy.bookpubshop.base.BaseCreateTimeEntity;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.servicecode.entity.CustomerServiceStateCode;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 고객서비스 개체입니다.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@Table(name = "customer_service")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CustomerService extends BaseCreateTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_service_number")
    private Integer serviceNo;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_service_code_number")
    private CustomerServiceStateCode customerServiceStateCode;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_number")
    private Member member;

    @NotNull
    @Column(name = "customer_service_category")
    private String serviceCategory;

    @NotNull
    @Column(name = "customer_service_title")
    private String serviceTitle;

    @NotNull
    @Column(name = "customer_service_content")
    private String serviceContent;

    /**
     * pk 를 뺀 생성자입니다.
     *
     * @param customerServiceStateCode 고객상태코드
     * @param member                   멤버
     * @param serviceCategory          고객서비스종류
     * @param serviceTitle             타이틀
     * @param serviceContent           내용
     */
    @Builder
    public CustomerService(CustomerServiceStateCode customerServiceStateCode, Member member,
                           String serviceCategory, String serviceTitle, String serviceContent) {
        this.customerServiceStateCode = customerServiceStateCode;
        this.member = member;
        this.serviceCategory = serviceCategory;
        this.serviceTitle = serviceTitle;
        this.serviceContent = serviceContent;
    }
}
