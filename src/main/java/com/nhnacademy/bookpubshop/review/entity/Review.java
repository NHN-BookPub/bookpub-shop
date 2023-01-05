package com.nhnacademy.bookpubshop.review.entity;

import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.reviewpolicy.entity.ReviewPolicy;
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
 * 리뷰 테이블.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@Table(name = "review")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_number", nullable = false)
    private Long reviewNo;

    @ManyToOne
    @JoinColumn(name = "member_number", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "product_number", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "product_review_policy_number", nullable = false)
    private ReviewPolicy reviewPolicy;

    @Column(name = "review_star", nullable = false)
    private Long reviewStar;

    @Column(name = "review_content", nullable = false)
    private String reviewContent;

    @Column(name = "review_created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "review_image_path", nullable = false)
    private String imagePath;
}
