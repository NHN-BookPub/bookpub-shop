package com.nhnacademy.bookpubshop.service.entity;

import com.nhnacademy.bookpubshop.member.entity.Member;
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
    @Column(name = "customer_service_number")
    private Integer serviceNo;

    @ManyToOne
    @JoinColumn(name = "member_number")
    private Member member;

    @Column(name = "customer_service_category")
    private String serviceCategory;

    @Column(name = "customer_service_title")
    private String serviceTitle;

    @Column(name = "customer_service_content")
    private String serviceContent;

    @Column(name = "customer_service_created_at")
    private LocalDateTime createdAt;
}
