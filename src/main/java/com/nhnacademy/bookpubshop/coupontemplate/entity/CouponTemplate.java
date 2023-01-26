package com.nhnacademy.bookpubshop.coupontemplate.entity;

import com.nhnacademy.bookpubshop.category.entity.Category;
import com.nhnacademy.bookpubshop.couponpolicy.entity.CouponPolicy;
import com.nhnacademy.bookpubshop.couponstatecode.entity.CouponStateCode;
import com.nhnacademy.bookpubshop.coupontype.entity.CouponType;
import com.nhnacademy.bookpubshop.file.entity.File;
import com.nhnacademy.bookpubshop.product.entity.Product;
import java.time.LocalDateTime;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_policy_number")
    private CouponPolicy couponPolicy;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_type_number")
    private CouponType couponType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_number")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_number")
    private Category category;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_state_code_number")
    private CouponStateCode couponStateCode;

    @NotNull
    @Column(name = "coupon_template_name")
    private String templateName;

    @Column(name = "coupon_template_finished_at")
    private LocalDateTime finishedAt;

    @Column(name = "coupon_template_bundled")
    private boolean templateBundled;


    @OneToOne(mappedBy = "couponTemplate", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private File file;

    public void setFile(File file) {
        this.file = file;
    }

    /**
     * 쿠폰템플릿 빌더.
     *
     * @param couponPolicy       the coupon policy
     * @param couponType         the coupon type
     * @param product            the product
     * @param category           the category
     * @param couponStateCode    the coupon state code
     * @param templateName       the template name
     * @param finishedAt         the finished at
     * @param templateBundled    the template bundled
     */
    @Builder
    public CouponTemplate(CouponPolicy couponPolicy, CouponType couponType,
                          Product product, Category category,
                          CouponStateCode couponStateCode, String templateName,
                          LocalDateTime finishedAt, boolean templateBundled) {
        this.couponPolicy = couponPolicy;
        this.couponType = couponType;
        this.product = product;
        this.category = category;
        this.couponStateCode = couponStateCode;
        this.templateName = templateName;
        this.finishedAt = finishedAt;
        this.templateBundled = templateBundled;
    }

    /**
     * 쿠폰템플릿 수정을 위한 메소드.
     *
     * @param couponPolicy       the coupon policy
     * @param couponType         the coupon type
     * @param product            the product
     * @param category           the category
     * @param couponStateCode    the coupon state code
     * @param templateName       the template name
     * @param finishedAt         the finished at
     * @param templateBundled    the template bundled
     */
    public void modifyCouponTemplate(CouponPolicy couponPolicy,
                                     CouponType couponType, Product product,
                                     Category category, CouponStateCode couponStateCode,
                                     String templateName, LocalDateTime finishedAt,
                                     boolean templateBundled) {
        this.couponPolicy = couponPolicy;
        this.couponType = couponType;
        this.product = product;
        this.category = category;
        this.couponStateCode = couponStateCode;
        this.templateName = templateName;
        this.finishedAt = finishedAt;
        this.templateBundled = templateBundled;
    }
}
