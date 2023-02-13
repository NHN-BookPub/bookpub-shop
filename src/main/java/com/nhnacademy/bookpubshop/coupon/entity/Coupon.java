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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
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

    @ManyToOne
    @JoinColumn(name = "order_number")
    private BookpubOrder order;

    @ManyToOne
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

    @Builder
    public Coupon(CouponTemplate couponTemplate, Member member) {
        this.couponTemplate = couponTemplate;
        this.member = member;
    }

    /**
     * 쿠폰 사용 여부를 바꿀 시 사용되는 메소드입니다.
     */
    public void modifyCouponUsed() {
        this.couponUsed = !this.couponUsed;
    }

    /**
     * 쿠폰의 사용시각, 주문 등을 초기화해주는 메소드입니다.
     * 쿠폰 사용 여부가 0으로 바뀔 경우 함께 사용될 메소드입니다.
     */
    public void transferEmpty() {
        this.order = null;
        this.orderProduct = null;
        this.usedAt = null;
    }


    /**
     * 주문에 사용 된 쿠폰을 사용 된 상태로 만드는 메소드 입니다.
     *
     * @param order 주문.
     * @param orderProduct 주문상품.
     */
    public void updateUsed(BookpubOrder order, OrderProduct orderProduct) {
        this.order = order;
        this.orderProduct = orderProduct;
        this.couponUsed = true;
    }

    /**
     * 주문에 사용 된 쿠폰을 다시 미사용 쿠폰으로 돌려놓는 메소드 입니다.
     */
    public void modifyNotUsedCoupon() {
        this.order = null;
        this.orderProduct = null;
        this.couponUsed = false;
    }
}
