package com.nhnacademy.bookpubshop.couponmonth.entity;

import com.nhnacademy.bookpubshop.coupontemplate.entity.CouponTemplate;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 이달의 쿠폰(coupon_month) 테이블.
 *
 * @author : 김서현
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "coupon_month")
public class CouponMonth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_month_number", nullable = false)
    private Long monthNumber;

    @OneToOne
    @JoinColumn(name = "coupon_number", nullable = false)
    private CouponTemplate couponTemplate;

    @Column(name = "coupon_month_opened_at", nullable = false)
    private LocalDateTime openedAt;

    @Column(name = "coupon_month_quantity", nullable = false)
    private Integer monthQuantity;

}
