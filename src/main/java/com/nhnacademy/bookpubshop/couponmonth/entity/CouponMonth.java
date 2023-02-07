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
import javax.validation.constraints.NotNull;
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
    @Column(name = "coupon_month_number")
    private Long monthNo;

    @OneToOne
    @NotNull
    @JoinColumn(name = "coupon_template_number")
    private CouponTemplate couponTemplate;

    @NotNull
    @Column(name = "coupon_month_opened_at")
    private LocalDateTime openedAt;

    @NotNull
    @Column(name = "coupon_month_quantity")
    private Integer monthQuantity;

    public void modifyCouponMonth(LocalDateTime openedAt, Integer monthQuantity) {
        this.openedAt = openedAt;
        this.monthQuantity = monthQuantity;
    }

    public void minusCouponMonthQuantity() {
        this.monthQuantity -= 1;
    }

}
