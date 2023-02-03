package com.nhnacademy.bookpubshop.file.entity;

import com.nhnacademy.bookpubshop.base.BaseCreateTimeEntity;
import com.nhnacademy.bookpubshop.coupontemplate.entity.CouponTemplate;
import com.nhnacademy.bookpubshop.customersupport.entity.CustomerService;
import com.nhnacademy.bookpubshop.personalinquiry.entity.PersonalInquiry;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.review.entity.Review;
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
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 파일개체 입니다.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@Table(name = "file")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class File extends BaseCreateTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_number")
    private Long fileNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_number")
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personal_inquiry_number")
    private PersonalInquiry personalInquiry;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_template_number")
    private CouponTemplate couponTemplate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_number")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_service_number")
    private CustomerService customerService;

    @NotNull
    @Column(name = "file_category")
    private String fileCategory;

    @NotNull
    @Column(name = "file_path")
    private String filePath;

    @NotNull
    @Column(name = "file_extension")
    private String fileExtension;

    @NotNull
    @Column(name = "file_name_origin")
    private String nameOrigin;

    @NotNull
    @Column(name = "file_name_saved")
    private String nameSaved;
}
