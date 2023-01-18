package com.nhnacademy.bookpubshop.review.entity;

import com.nhnacademy.bookpubshop.base.BaseCreateTimeEntity;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.reviewpolicy.entity.ReviewPolicy;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
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
public class Review extends BaseCreateTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_number")
    private Long reviewNo;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "member_number")
    private Member member;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "product_number")
    private Product product;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "product_review_policy_number")
    private ReviewPolicy reviewPolicy;

    @Column(name = "review_star")
    private Long reviewStar;

    @NotNull
    @Column(name = "review_content")
    private String reviewContent;

    @NotNull
    @Column(name = "review_image_path")
    private String imagePath;
}
