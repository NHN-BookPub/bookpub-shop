package com.nhnacademy.bookpubshop.point.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 포인트내역을 반환하기위한 클래스입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class GetPointAdminResponseDto {
    private String memberId;
    private Long pointHistoryAmount;
    private String pointHistoryReason;
    private LocalDateTime createdAt;
    private boolean increased;
}
