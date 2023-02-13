package com.nhnacademy.bookpubshop.point.entity;

import com.nhnacademy.bookpubshop.base.BaseCreateTimeEntity;
import com.nhnacademy.bookpubshop.member.entity.Member;
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
public class PointHistory extends BaseCreateTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_history_number")
    private Long pointHistoryNo;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "member_number")
    private Member member;

    @NotNull
    @Column(name = "point_history_amount")
    private Long pointHistoryAmount;

    @NotNull
    @Column(name = "point_history_increased")
    private boolean pointHistoryIncreased;

    @NotNull
    @Column(name = "point_history_reason")
    private String pointHistoryReason;
    @Builder
    public PointHistory(Member member,
                        Long pointHistoryAmount,
                        boolean pointHistoryIncreased,
                        String pointHistoryReason) {
        this.member = member;
        this.pointHistoryAmount = pointHistoryAmount;
        this.pointHistoryIncreased = pointHistoryIncreased;
        this.pointHistoryReason = pointHistoryReason;
    }
}
