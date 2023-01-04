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
    @Column(name = "point_history_number", nullable = false, unique = true)
    private Long pointHistoryNo;

    @ManyToOne
    @JoinColumn(name = "member_number", nullable = false)
    private Member member;

    @Column(name = "point_history_amount", nullable = false)
    private Long pointHistoryAmount;

    @Column(name = "point_history_increased", nullable = false)
    private boolean pointHistoryIncreased;

    @Column(name = "point_history_reason", nullable = false)
    private String pointHistoryReason;
}
