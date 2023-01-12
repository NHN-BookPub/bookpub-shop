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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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

    @ManyToOne
    @JoinColumn(name = "coupon_policy_number")
    @NotNull
    private CouponPolicy couponPolicy;

    @ManyToOne
    @JoinColumn(name = "coupon_type_number")
    @NotNull
    private CouponType couponType;

    @ManyToOne
    @JoinColumn(name = "product_number")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "category_number")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "coupon_state_code_number")
    @NotNull
    private CouponStateCode couponStateCode;

    @Column(name = "coupon_template_name")
    @NotBlank
    @Size(max = 50)
    private String templateName;

    @Column(name = "coupon_template_image")
    private String templateImage;

    @Column(name = "coupon_template_finished_at")
    private LocalDateTime finishedAt;

    @Column(name = "coupon_template_issued_at")
    @NotNull
    private LocalDateTime issuedAt;

    @Column(name = "coupon_template_overlapped")
    @NotNull
    private boolean templateOverlapped;

    @Column(name = "coupon_template_bundled")
    private boolean templateBundled;
}
