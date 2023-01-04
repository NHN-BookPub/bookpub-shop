package com.nhnacademy.bookpubshop.file.entity;

import com.nhnacademy.bookpubshop.coupon.entity.Coupon;
import com.nhnacademy.bookpubshop.personalinquiry.entity.PersonalInquiry;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.service.entity.CustomerService;
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
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_number", nullable = false)
    private Long fileNo;

    /**
     *  TODO 리뷰 매핑해야합니다.
     */

    @ManyToOne
    @JoinColumn(name = "personal_inquiry_number")
    private PersonalInquiry personalInquiry;

    @ManyToOne
    @JoinColumn(name = "coupon_number")
    private Coupon coupon;

    @ManyToOne
    @JoinColumn(name = "product_number")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "customer_service_number")
    private CustomerService customerService;

    @Column(name = "file_category", nullable = false)
    private String fileCategory;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "file_extension", nullable = false)
    private String fileExtension;

    @Column(name = "file_name_origin", nullable = false)
    private String nameOrigin;

    @Column(name = "file_name_saved", nullable = false)
    private String nameSaved;

    @Column(name = "file_created_at", nullable = false)
    private LocalDateTime createdAt;
}
