package com.nhnacademy.bookpubshop.reviewpolicy.entity;

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
 * 리뷰 정책 테이블입니다.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@Table(name = "product_review_policy")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ReviewPolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_review_policy_number", nullable = false)
    private Integer policyNo;

    @Column(name = "product_review_policy_send_point", nullable = false)
    private Long sendPoint;

}
