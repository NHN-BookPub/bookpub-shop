package com.nhnacademy.bookpubshop.coupon.entity;

import com.nhnacademy.bookpubshop.member.entity.Member;
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

    @ManyToOne
    @JoinColumn(name = "member_number")
    private Member member;

    @Column(name = "coupon_used")
    private boolean couponUsed;

    @Column(name = "coupon_used_at")
    private LocalDateTime usedAt;
}
