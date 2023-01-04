package com.nhnacademy.bookpubshop.service.entity;

import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.servicecode.entity.CustomerServiceStateCode;
import java.time.LocalDateTime;
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
public class CustomerService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_service_number", nullable = false, unique = true)
    private Integer serviceNo;

    @ManyToOne
    @JoinColumn(name = "customer_service_code_number", nullable = false)
    private CustomerServiceStateCode customerServiceStateCode;

    @ManyToOne
    @JoinColumn(name = "member_number", nullable = false)
    private Member member;

    @Column(name = "customer_service_category", nullable = false)
    private String serviceCategory;

    @Column(name = "customer_service_title", nullable = false)
    private String serviceTitle;

    @Column(name = "customer_service_content", nullable = false)
    private String serviceContent;

    @Column(name = "customer_service_created_at", nullable = false)
    private LocalDateTime createdAt;

    /**
     * pk 를 뺀 생성자입니다.
     *
     * @param customerServiceStateCode 고객상태코드
     * @param member                   멤버
     * @param serviceCategory          고객서비스종류
     * @param serviceTitle             타이틀
     * @param serviceContent           내용
     * @param createdAt                생성일자
     */
    @Builder
    public CustomerService(CustomerServiceStateCode customerServiceStateCode, Member member,
                           String serviceCategory, String serviceTitle, String serviceContent,
                           LocalDateTime createdAt) {
        this.customerServiceStateCode = customerServiceStateCode;
        this.member = member;
        this.serviceCategory = serviceCategory;
        this.serviceTitle = serviceTitle;
        this.serviceContent = serviceContent;
        this.createdAt = createdAt;
    }
}
