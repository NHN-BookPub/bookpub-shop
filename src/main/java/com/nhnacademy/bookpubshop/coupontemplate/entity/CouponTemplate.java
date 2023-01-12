package com.nhnacademy.bookpubshop.coupontemplate.entity;

import com.nhnacademy.bookpubshop.category.entity.Category;
import com.nhnacademy.bookpubshop.couponpolicy.entity.CouponPolicy;
import com.nhnacademy.bookpubshop.couponstatecode.entity.CouponStateCode;
import com.nhnacademy.bookpubshop.coupontype.entity.CouponType;
import com.nhnacademy.bookpubshop.product.entity.Product;
import java.time.LocalDateTime;
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
 * 쿠폰 템플릿 (coupon_template) 테이블.
 *
 * @author : 김서현
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "coupon_template")
public class CouponTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_template_number")
    private Long templateNo;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "coupon_policy_number")
    private CouponPolicy couponPolicy;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "coupon_type_number")
    private CouponType couponType;

    @ManyToOne
    @JoinColumn(name = "product_number")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "category_number")
    private Category category;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "coupon_state_code_number")
    private CouponStateCode couponStateCode;

    @NotNull
    @Column(name = "coupon_template_name")
    private String templateName;

    @Column(name = "coupon_template_image")
    private String templateImage;

    @Column(name = "coupon_template_finished_at")
    private LocalDateTime finishedAt;

    @NotNull
    @Column(name = "coupon_template_issued_at")
    private LocalDateTime issuedAt;

    @NotNull
    @Column(name = "coupon_template_overlapped")
    private boolean templateOverlapped;

    @Column(name = "coupon_template_bundled")
    private boolean templateBundled;
}
