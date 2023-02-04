package com.nhnacademy.bookpubshop.reviewpolicy.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 상품평 정책 테이블입니다.
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

    @Column(name = "product_review_policy_used")
    private boolean policyUsed;

    /**
     * 상품평 정책 builder
     *
     * @param sendPoint 상품리뷰지급포인트
     */
    @Builder
    public ReviewPolicy(Long sendPoint) {
        this.sendPoint = sendPoint;
    }

    /**
     * 상품평 정책 지급 포인트를 수정하기 위한 메서드입니다.
     *
     * @param sendPoint 상품리뷰지급포인트
     */
    public void modifySendPoint(Long sendPoint) {
        this.sendPoint = sendPoint;
    }

    /**
     * 상품평 정책 사용여부를 수정하기 위한 메서드입니다.
     *
     * @param policyUsed 정책 사용 여부
     */
    public void modifyUsed(boolean policyUsed) {
        this.policyUsed = policyUsed;
    }
}
