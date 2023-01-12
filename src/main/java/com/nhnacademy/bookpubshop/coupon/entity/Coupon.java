package com.nhnacademy.bookpubshop.coupon.entity;

import com.nhnacademy.bookpubshop.coupontemplate.entity.CouponTemplate;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.order.entity.BookpubOrder;
import com.nhnacademy.bookpubshop.order.relationship.entity.OrderProduct;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
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
 * 쿠폰개체 입니다.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@Table(name = "coupon")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_number")
    private Long couponNo;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "coupon_template_number")
    private CouponTemplate couponTemplate;

    @OneToOne
    @JoinColumn(name = "order_number")
    private BookpubOrder order;

    @OneToOne
    @JoinColumn(name = "order_product_number")
    private OrderProduct orderProduct;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "member_number")
    private Member member;

    @Column(name = "coupon_used")
    private boolean couponUsed;

    @Column(name = "coupon_used_at")
    private LocalDateTime usedAt;

    public void modifyCouponUsed(boolean couponUsed) {
        this.couponUsed = couponUsed;
    }

    public void modifyCouponUsedAt(LocalDateTime usedAt) {
        this.usedAt = usedAt;
    }
}
