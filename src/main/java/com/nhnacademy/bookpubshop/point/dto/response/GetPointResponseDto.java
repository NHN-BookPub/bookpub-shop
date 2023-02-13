package com.nhnacademy.bookpubshop.point.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 포인트 내역 조회 반환 dto.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@AllArgsConstructor
@Getter
public class GetPointResponseDto {
    private Long pointHistoryAmount;
    private String pointHistoryReason;
    private LocalDateTime createdAt;
    private boolean increased;
}
