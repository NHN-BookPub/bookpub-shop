package com.nhnacademy.bookpubshop.review.entity;

import com.nhnacademy.bookpubshop.base.BaseCreateTimeEntity;
import com.nhnacademy.bookpubshop.file.entity.File;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.review.dto.request.ModifyReviewRequestDto;
import com.nhnacademy.bookpubshop.reviewpolicy.entity.ReviewPolicy;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_number")
    private Member member;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_number")
    private Product product;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_review_policy_number")
    private ReviewPolicy reviewPolicy;

    @Column(name = "review_star")
    private Integer reviewStar;

    @NotNull
    @Column(name = "review_content")
    private String reviewContent;

    @NotNull
    @Column(name = "review_deleted")
    private boolean reviewDeleted;

    @OneToOne(mappedBy = "review", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private File file;

    public void setFile(File file) {
        this.file = file;
    }

    @Builder
    public Review(Member member, Product product, ReviewPolicy reviewPolicy,
                  Integer reviewStar, String reviewContent) {
        this.member = member;
        this.product = product;
        this.reviewPolicy = reviewPolicy;
        this.reviewStar = reviewStar;
        this.reviewContent = reviewContent;
    }

    public void modifyReview(ModifyReviewRequestDto dto) {
        this.reviewStar = dto.getReviewStar();
        this.reviewContent = dto.getReviewContent();
    }

    public void deleteReview() {
        this.reviewDeleted = true;
    }

    public void deleteFile() {
        this.file = null;
    }
}
