package com.nhnacademy.bookpubshop.inquiry.entity;

import com.nhnacademy.bookpubshop.product.entity.Product;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
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
    @Column(name = "inquiry_number")
    private Long inquiryNo;

    @ManyToOne
    @JoinColumn(name = "inquiry_number")
    private Inquiry inquiry;

    //Todo 회원번호 join

    @ManyToOne
    @JoinColumn(name = "product_number")
    private Product product;

    //Todo 문의상태코드번호 join

    @Column(name = "inquiry_content")
    private String inquiryContent;

    @Column(name = "inquiry_displayed")
    private boolean inquiryDisplayed;

    @Column(name = "inquiry_created_at")
    private LocalDateTime createdAt;

    @Column(name = "inquiry_answered")
    private boolean inquiryAnswered;
}
