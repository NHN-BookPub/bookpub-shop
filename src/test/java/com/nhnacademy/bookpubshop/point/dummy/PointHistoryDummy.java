package com.nhnacademy.bookpubshop.point.dummy;

import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.point.entity.PointHistory;
import java.time.LocalDateTime;

/**
 * 포인트내역 더미 클래스
 * 테스트시 해당 클래스를 쉽게 생성하기 위한 클래스 입니다.
 * 동료들과 협력하여 코딩할 때 유용합니다.
 *
 * @author : 임태원
 * @since : 1.0
 **/
public class PointHistoryDummy {
    public static PointHistory dummy(Member member) {
        return new PointHistory(
                null,
                member,
                981008L,
                true,
                "최초가입"
        );
    }
}
