package com.nhnacademy.bookpubshop.inquiry.entity;

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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 상품문의(inquiry) 테이블.
 *
 * @author : 박경서
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@Entity
@Table(name = "inquiry")
public class Inquiry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inquiry_number", nullable = false, unique = true)
    private Long inquiryNo;

    @ManyToOne
    @JoinColumn(name = "inquiry_parent_number")
    private Inquiry inquiry;

    //Todo 회원번호 join

    @ManyToOne
    @JoinColumn(name = "product_number", nullable = false)
    private Product product;

    //Todo 문의상태코드번호 join

    @Column(name = "inquiry_content", nullable = false)
    private String inquiryContent;

    @Column(name = "inquiry_displayed", nullable = false)
    private boolean inquiryDisplayed;

    @Column(name = "inquiry_created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "inquiry_answered", nullable = false)
    private boolean inquiryAnswered;
}
