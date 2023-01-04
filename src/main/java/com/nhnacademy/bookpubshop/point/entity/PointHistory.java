package com.nhnacademy.bookpubshop.point.entity;

import com.nhnacademy.bookpubshop.member.entity.Member;
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
 * 포인트 사용내역 개체입니다.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@Table(name = "point_history")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PointHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_number")
    private Long historyNo;

    @ManyToOne
    @JoinColumn(name = "member_number")
    private Member member;

    @Column(name = "point_amount")
    private Long pointAmount;

    @Column(name = "point_state")
    private boolean pointState;

    @Column(name = "point_reason")
    private String pointReason;
}
